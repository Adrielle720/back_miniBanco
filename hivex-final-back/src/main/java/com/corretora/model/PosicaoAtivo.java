package com.corretora.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "posicoes_ativo")
public class PosicaoAtivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ativo_id", nullable = false)
    private Ativo ativo;

    @Column(nullable = false)
    private int quantidade;

    // Preço médio de aquisição (recalculado a cada compra)
    @Column(nullable = false)
    private BigDecimal precoMedio;

    public PosicaoAtivo() {}

    public PosicaoAtivo(Ativo ativo, int quantidade, BigDecimal precoCompra) {
        this.ativo = ativo;
        this.quantidade = quantidade;
        this.precoMedio = precoCompra;
    }

    /**
     * Adiciona mais unidades à posição e recalcula o preço médio.
     * precoMedio = (qtd_atual * preco_atual + qtd_nova * preco_novo) / qtd_total
     */
    public void adicionarQuantidade(int qtd, BigDecimal precoCompra) {
        BigDecimal custoAtual = precoMedio.multiply(BigDecimal.valueOf(this.quantidade));
        BigDecimal custoNovo = precoCompra.multiply(BigDecimal.valueOf(qtd));

        this.quantidade += qtd;
        this.precoMedio = custoAtual.add(custoNovo)
                .divide(BigDecimal.valueOf(this.quantidade), 2, RoundingMode.HALF_UP);
    }

    /**
     * Remove unidades da posição (em venda). Lança exceção se saldo insuficiente.
     */
    public void removerQuantidade(int qtd) {
        if (qtd > this.quantidade) {
            throw new IllegalArgumentException(
                "Quantidade a vender (" + qtd + ") maior que posição atual (" + this.quantidade + ")"
            );
        }
        this.quantidade -= qtd;
    }

    /** Valor atual da posição a preço de mercado. */
    public BigDecimal calcularValorAtual() {
        return ativo.getPrecoAtual().multiply(BigDecimal.valueOf(quantidade));
    }

    /** Lucro ou prejuízo: valor atual - custo de aquisição. */
    public BigDecimal calcularLucro() {
        BigDecimal custoTotal = precoMedio.multiply(BigDecimal.valueOf(quantidade));
        return calcularValorAtual().subtract(custoTotal);
    }

    public boolean isEmpty() { return this.quantidade == 0; }

    public Long getId() { return id; }
    public Ativo getAtivo() { return ativo; }
    public int getQuantidade() { return quantidade; }
    public BigDecimal getPrecoMedio() { return precoMedio; }
}
