package com.corretora.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import java.math.BigDecimal;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@DiscriminatorValue("COMPRA")
public class OrdemCompra extends Ordem {

    public OrdemCompra() {}

    public OrdemCompra(Cliente cliente, Ativo ativo, int quantidade, BigDecimal precoUnitario) {
        super(cliente, ativo, quantidade, precoUnitario);
    }

    /**
     * Executa a compra:
     * 1. Verifica disponibilidade na corretora
     * 2. Debita saldo do cliente
     * 3. Reserva o ativo no portfólio da corretora
     * 4. Registra a posição na carteira do cliente
     */
    @Override
    public void executar(Corretora corretora) {
        AtivoDisponivel disponivel = corretora.buscarAtivoDisponivel(getAtivo())
                .orElseThrow(() -> new IllegalStateException(
                    "Ativo " + getAtivo().getTicker() + " não disponível na corretora"
                ));

        if (!disponivel.isDisponivel(getQuantidade())) {
            throw new IllegalStateException("Quantidade indisponível no portfólio da corretora");
        }

        BigDecimal valorTotal = calcularValorTotal();

        getCliente().debitarSaldo(valorTotal);
        disponivel.reservar(getQuantidade());
        getCliente().getCarteira().registrarCompra(getAtivo(), getQuantidade(), getPrecoUnitario());

        marcarComoExecutada();
    }
}
