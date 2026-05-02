package com.corretora.service;

import com.corretora.model.*;
import com.corretora.model.enums.Liquidez;
import com.corretora.repository.AtivoRepository;
import com.corretora.repository.EmpresaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class AtivoService {

    private final AtivoRepository ativoRepository;
    private final EmpresaRepository empresaRepository;

    public AtivoService(AtivoRepository ativoRepository, EmpresaRepository empresaRepository) {
        this.ativoRepository = ativoRepository;
        this.empresaRepository = empresaRepository;
    }

    public Acao criarAcao(String ticker, String nome, BigDecimal preco, Liquidez liquidez,
                          Long empresaId, BigDecimal percentualCapital, BigDecimal dividendYield) {
        Empresa empresa = buscarEmpresa(empresaId);
        return ativoRepository.save(new Acao(ticker, nome, preco, liquidez, empresa, percentualCapital, dividendYield));
    }

    public FundoImobiliario criarFII(String ticker, String nome, BigDecimal preco, Liquidez liquidez,
                                     Long empresaId, String tipo, BigDecimal rendimentoMensal) {
        Empresa empresa = buscarEmpresa(empresaId);
        return ativoRepository.save(new FundoImobiliario(ticker, nome, preco, liquidez, empresa, tipo, rendimentoMensal));
    }

    public TituloRendaFixa criarTitulo(String ticker, String nome, BigDecimal preco, Liquidez liquidez,
                                       Long empresaId, LocalDate vencimento, BigDecimal taxaJuros) {
        Empresa empresa = buscarEmpresa(empresaId);
        return ativoRepository.save(new TituloRendaFixa(ticker, nome, preco, liquidez, empresa, vencimento, taxaJuros));
    }

    public Ativo atualizarPreco(Long ativoId, BigDecimal novoPreco) {
        Ativo ativo = buscarPorId(ativoId);
        ativo.atualizarPreco(novoPreco);
        return ativoRepository.save(ativo);
    }

    public List<Ativo> listarTodos() { return ativoRepository.findAll(); }

    public Ativo buscarPorId(Long id) {
        return ativoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ativo não encontrado: " + id));
    }

    private Empresa buscarEmpresa(Long id) {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empresa não encontrada: " + id));
    }
}
