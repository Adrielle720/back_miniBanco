package com.corretora.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "carteiras")
public class Carteira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "carteira_id")
    private List<PosicaoAtivo> posicoes = new ArrayList<>();

    public Carteira() {}

    /**
     * Registra uma compra na carteira.
     * Se o ativo já existe na carteira, atualiza o preço médio.
     * Se não existe, cria uma nova posição.
     */
    public void registrarCompra(Ativo ativo, int quantidade, BigDecimal preco) {
        Optional<PosicaoAtivo> posicaoExistente = buscarPosicao(ativo);

        if (posicaoExistente.isPresent()) {
            posicaoExistente.get().adicionarQuantidade(quantidade, preco);
        } else {
            posicoes.add(new PosicaoAtivo(ativo, quantidade, preco));
        }
    }

    /**
     * Registra uma venda na carteira.
     * Remove a posição completamente se a quantidade chegar a zero.
     */
    public void registrarVenda(Ativo ativo, int quantidade) {
        PosicaoAtivo posicao = buscarPosicao(ativo)
                .orElseThrow(() -> new IllegalStateException(
                    "Ativo " + ativo.getTicker() + " não encontrado na carteira"
                ));
        posicao.removerQuantidade(quantidade);

        if (posicao.isEmpty()) {
            posicoes.remove(posicao);
        }
    }

    /** Soma o valor de mercado atual de todas as posições. */
    public BigDecimal calcularPatrimonio() {
        return posicoes.stream()
                .map(PosicaoAtivo::calcularValorAtual)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /** Lucro/prejuízo total da carteira. */
    public BigDecimal calcularLucroTotal() {
        return posicoes.stream()
                .map(PosicaoAtivo::calcularLucro)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Optional<PosicaoAtivo> buscarPosicao(Ativo ativo) {
        return posicoes.stream()
                .filter(p -> p.getAtivo().getId().equals(ativo.getId()))
                .findFirst();
    }

    public Long getId() { return id; }
    public List<PosicaoAtivo> getPosicoes() { return posicoes; }
}
