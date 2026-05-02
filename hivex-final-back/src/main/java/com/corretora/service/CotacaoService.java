package com.corretora.service;

import com.corretora.model.Ativo;
import com.corretora.model.CotacaoMercado;
import com.corretora.repository.AtivoRepository;
import com.corretora.repository.CotacaoMercadoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CotacaoService {

    private final AlphaVantageService alphaVantageService;
    private final AtivoRepository ativoRepository;
    private final CotacaoMercadoRepository cotacaoRepository;

    public CotacaoService(AlphaVantageService alphaVantageService,
                          AtivoRepository ativoRepository,
                          CotacaoMercadoRepository cotacaoRepository) {
        this.alphaVantageService = alphaVantageService;
        this.ativoRepository = ativoRepository;
        this.cotacaoRepository = cotacaoRepository;
    }

    public CotacaoMercado atualizarPrecoPorTicker(String ticker) {
        Ativo ativo = ativoRepository.findByTicker(ticker)
                .orElseThrow(() -> new IllegalArgumentException("Ativo não encontrado: " + ticker));

        CotacaoMercado cotacao = alphaVantageService.buscarCotacao(ticker);
        if (cotacao == null) return null;

        cotacaoRepository.save(cotacao);
        ativo.atualizarPreco(cotacao.getPreco());
        ativoRepository.save(ativo);

        System.out.println("✅ " + ticker + " → R$ " + cotacao.getPreco()
                + " (" + cotacao.getVariacaoPercent() + "%)");
        return cotacao;
    }

    /** Atualiza todos os ativos e retorna a lista de cotações obtidas */
    public List<CotacaoMercado> atualizarTodosAtivos() {
        List<Ativo> ativos = ativoRepository.findAll();
        List<CotacaoMercado> cotacoes = new ArrayList<>();
        for (Ativo ativo : ativos) {
            try {
                CotacaoMercado c = atualizarPrecoPorTicker(ativo.getTicker());
                if (c != null) cotacoes.add(c);
                Thread.sleep(500); // respeita rate limit gratuito
            } catch (Exception e) {
                System.out.println("⚠️ Erro: " + ativo.getTicker() + " — " + e.getMessage());
            }
        }
        return cotacoes;
    }

    public List<CotacaoMercado> historicoCotacoes(String ticker) {
        return cotacaoRepository.findByTickerOrderByConsultadoEmDesc(ticker);
    }
}
