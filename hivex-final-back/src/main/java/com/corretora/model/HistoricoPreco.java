package com.corretora.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "historico_precos")
public class HistoricoPreco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Column(nullable = false)
    private BigDecimal preco;

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
    public Ativo getAtivo() { return ativo; }
}
