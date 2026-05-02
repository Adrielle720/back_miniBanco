package com.corretora.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.corretora.model.enums.Liquidez;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe base abstrata para todos os tipos de ativos financeiros.
 * Usa JOINED inheritance para manter o banco normalizado:
 * cada subclasse tem sua própria tabela com os campos específicos.
 */
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "ativos")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Ativo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String ticker;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private BigDecimal precoAtual;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Liquidez liquidez;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    // Composição: ativo é dono do seu histórico
    @OneToMany(mappedBy = "ativo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistoricoPreco> historico = new ArrayList<>();

    protected Ativo() {}

    protected Ativo(String ticker, String nome, BigDecimal precoAtual, Liquidez liquidez, Empresa empresa) {
        this.ticker = ticker;
        this.nome = nome;
        this.precoAtual = precoAtual;
        this.liquidez = liquidez;
        this.empresa = empresa;
    }

    /**
     * Atualiza o preço atual e registra no histórico automaticamente.
     * Regra de negócio: o ativo é responsável pela sua própria valorização.
     */
    public void atualizarPreco(BigDecimal novoPreco) {
        this.precoAtual = novoPreco;
        this.historico.add(new HistoricoPreco(novoPreco, this));
    }

    // Getters e setters
    public Long getId() { return id; }
    public String getTicker() { return ticker; }
    public void setTicker(String ticker) { this.ticker = ticker; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public BigDecimal getPrecoAtual() { return precoAtual; }
    public Liquidez getLiquidez() { return liquidez; }
    public void setLiquidez(Liquidez liquidez) { this.liquidez = liquidez; }
    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }
    public List<HistoricoPreco> getHistorico() { return historico; }
}
