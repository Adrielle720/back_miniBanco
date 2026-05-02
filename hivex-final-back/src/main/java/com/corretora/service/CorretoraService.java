package com.corretora.service;

import com.corretora.model.*;
import com.corretora.repository.AtivoRepository;
import com.corretora.repository.CorretoraRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class CorretoraService {

    private final CorretoraRepository corretoraRepository;
    private final AtivoRepository ativoRepository;

    public CorretoraService(CorretoraRepository corretoraRepository, AtivoRepository ativoRepository) {
        this.corretoraRepository = corretoraRepository;
        this.ativoRepository = ativoRepository;
    }

    public Corretora cadastrar(String nome, String cnpj) {
        return corretoraRepository.save(new Corretora(nome, cnpj));
    }

    public Corretora buscarPorId(Long id) {
        return corretoraRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Corretora não encontrada: " + id));
    }

    public void adicionarAtivoAoPortfolio(Long corretoraId, Long ativoId,
                                          int quantidade, BigDecimal precoOferta) {
        Corretora corretora = buscarPorId(corretoraId);
        Ativo ativo = ativoRepository.findById(ativoId)
                .orElseThrow(() -> new IllegalArgumentException("Ativo não encontrado: " + ativoId));
        corretora.adicionarAtivo(ativo, quantidade, precoOferta);
        corretoraRepository.save(corretora);
    }

    public List<AtivoDisponivel> listarPortfolio(Long corretoraId) {
        return buscarPorId(corretoraId).getPortfolio();
    }

    public List<Ordem> listarOrdens(Long corretoraId) {
        return buscarPorId(corretoraId).getOrdens();
    }
}
