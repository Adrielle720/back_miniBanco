package com.corretora.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import java.math.BigDecimal;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "ativos_disponiveis")
public class AtivoDisponivel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ativo_id", nullable = false)
    private Ativo ativo;

    @Column(nullable = false)
    private int quantidadeDisponivel;

    // A corretora pode ter spread: preço de oferta diferente do preço do ativo
    @Column(nullable = false)
    private BigDecimal precoOferta;

    public AtivoDisponivel() {}

    public AtivoDisponivel(Ativo ativo, int quantidadeDisponivel, BigDecimal precoOferta) {
        this.ativo = ativo;
        this.quantidadeDisponivel = quantidadeDisponivel;
        this.precoOferta = precoOferta;
    }

    public boolean isDisponivel(int quantidade) {
        return quantidadeDisponivel >= quantidade;
    }

    public void reservar(int quantidade) {
        if (!isDisponivel(quantidade)) {
            throw new IllegalStateException(
                "Quantidade indisponível. Disponível: " + quantidadeDisponivel + " | Solicitado: " + quantidade
            );
        }
        this.quantidadeDisponivel -= quantidade;
    }

    public void repor(int quantidade) {
        this.quantidadeDisponivel += quantidade;
    }

    public Long getId() { return id; }
    public Ativo getAtivo() { return ativo; }
    public int getQuantidadeDisponivel() { return quantidadeDisponivel; }
    public BigDecimal getPrecoOferta() { return precoOferta; }
    public void setPrecoOferta(BigDecimal precoOferta) { this.precoOferta = precoOferta; }
}
