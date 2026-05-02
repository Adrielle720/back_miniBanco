package com.corretora.service;

import com.corretora.model.*;
import com.corretora.repository.AtivoRepository;
import com.corretora.repository.ClienteRepository;
import com.corretora.repository.CorretoraRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final AtivoRepository ativoRepository;
    private final CorretoraRepository corretoraRepository;

    public ClienteService(ClienteRepository clienteRepository,
                          AtivoRepository ativoRepository,
                          CorretoraRepository corretoraRepository) {
        this.clienteRepository = clienteRepository;
        this.ativoRepository = ativoRepository;
        this.corretoraRepository = corretoraRepository;
    }

    public Cliente cadastrar(String nome, String cpf, String email, BigDecimal saldoInicial) {
        if (clienteRepository.existsByCpf(cpf)) {
            throw new IllegalArgumentException("CPF já cadastrado: " + cpf);
        }
        return clienteRepository.save(new Cliente(nome, cpf, email, saldoInicial));
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + id));
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    /**
     * Orquestra uma compra: cria a OrdemCompra e delega a execução para a Corretora.
     * A regra de negócio (debitar saldo, reservar ativo) fica no domínio — não aqui.
     */
    public Ordem comprar(Long clienteId, Long ativoId, Long corretoraId, int quantidade) {
        Cliente cliente = buscarPorId(clienteId);
        Ativo ativo = ativoRepository.findById(ativoId)
                .orElseThrow(() -> new IllegalArgumentException("Ativo não encontrado: " + ativoId));
        Corretora corretora = corretoraRepository.findById(corretoraId)
                .orElseThrow(() -> new IllegalArgumentException("Corretora não encontrada: " + corretoraId));

        BigDecimal precoAtual = corretora.buscarAtivoDisponivel(ativo)
                .map(AtivoDisponivel::getPrecoOferta)
                .orElseThrow(() -> new IllegalStateException("Ativo indisponível na corretora"));

        Ordem ordem = new OrdemCompra(cliente, ativo, quantidade, precoAtual);
        corretora.executarOrdem(ordem);

        clienteRepository.save(cliente);
        corretoraRepository.save(corretora);
        return ordem;
    }

    /**
     * Orquestra uma venda: cria a OrdemVenda e delega a execução para a Corretora.
     */
    public Ordem vender(Long clienteId, Long ativoId, Long corretoraId, int quantidade) {
        Cliente cliente = buscarPorId(clienteId);
        Ativo ativo = ativoRepository.findById(ativoId)
                .orElseThrow(() -> new IllegalArgumentException("Ativo não encontrado: " + ativoId));
        Corretora corretora = corretoraRepository.findById(corretoraId)
                .orElseThrow(() -> new IllegalArgumentException("Corretora não encontrada: " + corretoraId));

        Ordem ordem = new OrdemVenda(cliente, ativo, quantidade, ativo.getPrecoAtual());
        corretora.executarOrdem(ordem);

        clienteRepository.save(cliente);
        corretoraRepository.save(corretora);
        return ordem;
    }

    public void depositar(Long clienteId, BigDecimal valor) {
        Cliente cliente = buscarPorId(clienteId);
        cliente.creditarSaldo(valor);
        clienteRepository.save(cliente);
    }
}
