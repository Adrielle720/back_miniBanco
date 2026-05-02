package com.corretora.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.corretora.model.enums.Liquidez;
import jakarta.persistence.*;
import java.math.BigDecimal;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "acoes")
public class Acao extends Ativo {

    // Percentual do capital da empresa que uma unidade representa
    @Column(nullable = false)
    private BigDecimal percentualCapital;

    // Dividend yield anual (ex: 0.08 = 8% ao ano)
    @Column(nullable = false)
    private BigDecimal dividendYield;

    public Acao() {}

    public Acao(String ticker, String nome, BigDecimal precoAtual, Liquidez liquidez,
                Empresa empresa, BigDecimal percentualCapital, BigDecimal dividendYield) {
        super(ticker, nome, precoAtual, liquidez, empresa);
        this.percentualCapital = percentualCapital;
        this.dividendYield = dividendYield;
    }

    /**
     * Calcula o valor de dividendos esperado para uma quantidade de ações.
     * Regra de negócio: dividendo = preço * dividendYield * quantidade
     */
    public BigDecimal calcularDividendos(int quantidade) {
        return getPrecoAtual()
                .multiply(dividendYield)
                .multiply(BigDecimal.valueOf(quantidade));
    }

    public BigDecimal getPercentualCapital() { return percentualCapital; }
    public void setPercentualCapital(BigDecimal percentualCapital) { this.percentualCapital = percentualCapital; }
    public BigDecimal getDividendYield() { return dividendYield; }
    public void setDividendYield(BigDecimal dividendYield) { this.dividendYield = dividendYield; }
}
