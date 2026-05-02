package com.corretora.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.corretora.model.enums.Liquidez;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "titulos_renda_fixa")
public class TituloRendaFixa extends Ativo {

    @Column(nullable = false)
    private LocalDate vencimento;

    // Taxa anual (ex: 0.12 = 12% ao ano)
    @Column(nullable = false)
    private BigDecimal taxaJuros;

    public TituloRendaFixa() {}

    public TituloRendaFixa(String ticker, String nome, BigDecimal precoAtual, Liquidez liquidez,
                           Empresa empresa, LocalDate vencimento, BigDecimal taxaJuros) {
        super(ticker, nome, precoAtual, liquidez, empresa);
        this.vencimento = vencimento;
        this.taxaJuros = taxaJuros;
    }

    /**
     * Calcula o rendimento estimado até o vencimento para uma quantidade de títulos.
     * Usa juros simples para simplicidade no contexto do projeto.
     */
    public BigDecimal calcularRendimentoAteVencimento(int quantidade) {
        long diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), vencimento);
        if (diasRestantes <= 0) return BigDecimal.ZERO;

        BigDecimal anos = BigDecimal.valueOf(diasRestantes)
                .divide(BigDecimal.valueOf(365), 6, RoundingMode.HALF_UP);
        BigDecimal valorTotal = getPrecoAtual().multiply(BigDecimal.valueOf(quantidade));

        return valorTotal.multiply(taxaJuros).multiply(anos)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public boolean isVencido() {
        return LocalDate.now().isAfter(vencimento);
    }

    public LocalDate getVencimento() { return vencimento; }
    public void setVencimento(LocalDate vencimento) { this.vencimento = vencimento; }
    public BigDecimal getTaxaJuros() { return taxaJuros; }
    public void setTaxaJuros(BigDecimal taxaJuros) { this.taxaJuros = taxaJuros; }
}
