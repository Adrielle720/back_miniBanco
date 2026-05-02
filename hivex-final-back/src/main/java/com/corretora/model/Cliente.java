package com.corretora.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import java.math.BigDecimal;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private BigDecimal saldo;

    // Composição: cliente é dono da carteira
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "carteira_id", nullable = false)
    private Carteira carteira;

    public Cliente() {}

    public Cliente(String nome, String cpf, String email, BigDecimal saldoInicial) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.saldo = saldoInicial;
        this.carteira = new Carteira(); // toda conta começa com carteira vazia
    }

    /**
     * Debita saldo do cliente. Lança exceção se saldo insuficiente.
     */
    public void debitarSaldo(BigDecimal valor) {
        if (saldo.compareTo(valor) < 0) {
            throw new IllegalStateException(
                "Saldo insuficiente. Saldo: R$" + saldo + " | Necessário: R$" + valor
            );
        }
        this.saldo = this.saldo.subtract(valor);
    }

    /**
     * Credita saldo ao cliente (após venda ou depósito).
     */
    public void creditarSaldo(BigDecimal valor) {
        this.saldo = this.saldo.add(valor);
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpf() { return cpf; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public BigDecimal getSaldo() { return saldo; }
    public Carteira getCarteira() { return carteira; }
}
