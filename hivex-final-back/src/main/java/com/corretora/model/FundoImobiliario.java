package com.corretora.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.corretora.model.enums.Liquidez;
import jakarta.persistence.*;
import java.math.BigDecimal;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "fundos_imobiliarios")
public class FundoImobiliario extends Ativo {

    // Ex: "Logística", "Shoppings", "Lajes Corporativas"
    @Column(nullable = false)
    private String tipo;

    // Rendimento mensal por cota (em R$)
    @Column(nullable = false)
    private BigDecimal rendimentoMensal;

    public FundoImobiliario() {}

    public FundoImobiliario(String ticker, String nome, BigDecimal precoAtual, Liquidez liquidez,
                            Empresa empresa, String tipo, BigDecimal rendimentoMensal) {
        super(ticker, nome, precoAtual, liquidez, empresa);
        this.tipo = tipo;
        this.rendimentoMensal = rendimentoMensal;
    }

    /**
     * Calcula o rendimento mensal total para uma quantidade de cotas.
     */
    public BigDecimal calcularRendimentoMensal(int quantidade) {
        return rendimentoMensal.multiply(BigDecimal.valueOf(quantidade));
    }

    /**
     * Dividend yield implícito: rendimento / preço da cota.
     */
    public BigDecimal calcularDividendYield() {
        if (getPrecoAtual().compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        return rendimentoMensal.divide(getPrecoAtual(), 4, java.math.RoundingMode.HALF_UP);
    }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public BigDecimal getRendimentoMensal() { return rendimentoMensal; }
    public void setRendimentoMensal(BigDecimal rendimentoMensal) { this.rendimentoMensal = rendimentoMensal; }
}
