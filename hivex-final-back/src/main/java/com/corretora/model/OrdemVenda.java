package com.corretora.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import java.math.BigDecimal;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@DiscriminatorValue("VENDA")
public class OrdemVenda extends Ordem {

    public OrdemVenda() {}

    public OrdemVenda(Cliente cliente, Ativo ativo, int quantidade, BigDecimal precoUnitario) {
        super(cliente, ativo, quantidade, precoUnitario);
    }

    /**
     * Executa a venda:
     * 1. Verifica se o cliente tem posição suficiente
     * 2. Remove da carteira do cliente
     * 3. Repõe o ativo no portfólio da corretora
     * 4. Credita saldo ao cliente
     */
    @Override
    public void executar(Corretora corretora) {
        // Verifica se o cliente tem o ativo (lança exceção se não tiver)
        getCliente().getCarteira().buscarPosicao(getAtivo())
                .orElseThrow(() -> new IllegalStateException(
                    "Cliente não possui " + getAtivo().getTicker() + " em carteira"
                ));

        BigDecimal valorTotal = calcularValorTotal();

        getCliente().getCarteira().registrarVenda(getAtivo(), getQuantidade());
        getCliente().creditarSaldo(valorTotal);

        // Recoloca o ativo no portfólio da corretora
        corretora.buscarAtivoDisponivel(getAtivo())
                .ifPresent(disponivel -> disponivel.repor(getQuantidade()));

        marcarComoExecutada();
    }
}
