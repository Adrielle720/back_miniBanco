package com.corretora.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import java.math.BigDecimal;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "empresas")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true, length = 14)
    private String cnpj;

    @Column(nullable = false)
    private String setor;

    @Column(nullable = false)
    private BigDecimal capitalMercado;

    public Empresa() {}

    public Empresa(String nome, String cnpj, String setor, BigDecimal capitalMercado) {
        this.nome = nome;
        this.cnpj = cnpj;
        this.setor = setor;
        this.capitalMercado = capitalMercado;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
    public String getSetor() { return setor; }
    public void setSetor(String setor) { this.setor = setor; }
    public BigDecimal getCapitalMercado() { return capitalMercado; }
    public void setCapitalMercado(BigDecimal capitalMercado) { this.capitalMercado = capitalMercado; }
}
