package com.corretora.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "corretoras")
public class Corretora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true, length = 14)
    private String cnpj;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "corretora_id")
    private List<AtivoDisponivel> portfolio = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "corretora_id")
    private List<Ordem> ordens = new ArrayList<>();

    public Corretora() {}

    public Corretora(String nome, String cnpj) {
        this.nome = nome;
        this.cnpj = cnpj;
    }

    /**
     * Adiciona um ativo ao portfólio da corretora.
     * Se o ativo já existir, aumenta a quantidade disponível.
     */
    public void adicionarAtivo(Ativo ativo, int quantidade, java.math.BigDecimal precoOferta) {
        Optional<AtivoDisponivel> existente = buscarAtivoDisponivel(ativo);
        if (existente.isPresent()) {
            existente.get().repor(quantidade);
        } else {
            portfolio.add(new AtivoDisponivel(ativo, quantidade, precoOferta));
        }
    }

    /**
     * Registra e executa uma ordem via polimorfismo.
     * OrdemCompra e OrdemVenda têm comportamentos distintos em executar().
     */
    public void executarOrdem(Ordem ordem) {
        ordens.add(ordem);
        ordem.executar(this); // polimorfismo: comportamento depende do tipo de ordem
    }

    public Optional<AtivoDisponivel> buscarAtivoDisponivel(Ativo ativo) {
        return portfolio.stream()
                .filter(a -> a.getAtivo().getId().equals(ativo.getId()))
                .findFirst();
    }

    public List<AtivoDisponivel> getPortfolio() { return portfolio; }
    public List<Ordem> getOrdens() { return ordens; }
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getCnpj() { return cnpj; }
}
