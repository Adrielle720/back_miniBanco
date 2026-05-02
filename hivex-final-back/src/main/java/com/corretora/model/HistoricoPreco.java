package com.corretora.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "historico_precos")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class HistoricoPreco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Column(nullable = false)
    private BigDecimal preco;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ativo_id", nullable = false)
    private Ativo ativo;

    public HistoricoPreco() {}

    public HistoricoPreco(BigDecimal preco, Ativo ativo) {
        this.preco = preco;
        this.ativo = ativo;
        this.dataHora = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public LocalDateTime getDataHora() { return dataHora; }
    public BigDecimal getPreco() { return preco; }

    @JsonIgnore
    public Ativo getAtivo() { return ativo; }
}