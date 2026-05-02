package com.corretora.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.corretora.model.enums.StatusOrdem;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Representa uma ordem de compra ou venda emitida por um cliente.
 * Usa SINGLE_TABLE inheritance por simplicidade — ambas as subclasses
 * têm estrutura muito similar, diferindo apenas no comportamento de executar().
 */
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "ordens")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_ordem", discriminatorType = DiscriminatorType.STRING)
public abstract class Ordem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ativo_id", nullable = false)
    private Ativo ativo;

    @Column(nullable = false)
    private int quantidade;

    // Preço no momento em que a ordem foi criada
    @Column(nullable = false)
    private BigDecimal precoUnitario;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusOrdem status;

    protected Ordem() {}

    protected Ordem(Cliente cliente, Ativo ativo, int quantidade, BigDecimal precoUnitario) {
        this.cliente = cliente;
        this.ativo = ativo;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.dataHora = LocalDateTime.now();
        this.status = StatusOrdem.PENDENTE;
    }

    /**
     * Template method: cada subclasse implementa a lógica específica de execução.
     * A corretora é passada para que a ordem possa interagir com o portfólio.
     */
    public abstract void executar(Corretora corretora);

    public BigDecimal calcularValorTotal() {
        return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

    protected void marcarComoExecutada() { this.status = StatusOrdem.EXECUTADA; }
    protected void marcarComoCancelada() { this.status = StatusOrdem.CANCELADA; }

    public Long getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public Ativo getAtivo() { return ativo; }
    public int getQuantidade() { return quantidade; }
    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public LocalDateTime getDataHora() { return dataHora; }
    public StatusOrdem getStatus() { return status; }
}
