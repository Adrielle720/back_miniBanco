package com.corretora.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cotacoes_mercado")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CotacaoMercado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ticker;

    @Column(nullable = false)
    private BigDecimal preco;

    private BigDecimal variacao;
    private BigDecimal variacaoPercent;
    private Long volume;
    private String ultimoPregao;
    private BigDecimal abertura;
    private BigDecimal fechamentoAnterior;

    @Column(nullable = false)
    private LocalDateTime consultadoEm;

    @Column(nullable = false)
    private String fonte;

    public CotacaoMercado() {}

    public CotacaoMercado(String ticker, BigDecimal preco, BigDecimal variacao,
                          BigDecimal variacaoPercent, Long volume, String ultimoPregao,
                          BigDecimal abertura, BigDecimal fechamentoAnterior) {
        this.ticker = ticker;
        this.preco = preco;
        this.variacao = variacao;
        this.variacaoPercent = variacaoPercent;
        this.volume = volume;
        this.ultimoPregao = ultimoPregao;
        this.abertura = abertura;
        this.fechamentoAnterior = fechamentoAnterior;
        this.consultadoEm = LocalDateTime.now();
        this.fonte = "ALPHA_VANTAGE";
    }

    public Long getId() { return id; }
    public String getTicker() { return ticker; }
    public BigDecimal getPreco() { return preco; }
    public BigDecimal getVariacao() { return variacao; }
    public BigDecimal getVariacaoPercent() { return variacaoPercent; }
    public Long getVolume() { return volume; }
    public String getUltimoPregao() { return ultimoPregao; }
    public BigDecimal getAbertura() { return abertura; }
    public BigDecimal getFechamentoAnterior() { return fechamentoAnterior; }
    public LocalDateTime getConsultadoEm() { return consultadoEm; }
    public String getFonte() { return fonte; }
}
