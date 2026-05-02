package com.corretora.controller;

import com.corretora.model.CotacaoMercado;
import com.corretora.service.CotacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cotacoes")
@CrossOrigin(origins = "*")
public class CotacaoController {

    private final CotacaoService cotacaoService;

    public CotacaoController(CotacaoService cotacaoService) {
        this.cotacaoService = cotacaoService;
    }

    /** GET /api/cotacoes/PETR4 — busca preço real e atualiza ativo */
    @GetMapping("/{ticker}")
    public ResponseEntity<?> buscarCotacao(@PathVariable String ticker) {
        CotacaoMercado cotacao = cotacaoService.atualizarPrecoPorTicker(ticker.toUpperCase());
        if (cotacao == null) {
            return ResponseEntity.ok(Map.of(
                "ticker", ticker.toUpperCase(),
                "mensagem", "Sem dados para este ticker. Verifique o nome (ex: PETR4, VALE3, IBM)."
            ));
        }
        return ResponseEntity.ok(cotacao);
    }

    /** POST /api/cotacoes/atualizar-todos — atualiza todos os ativos */
    @PostMapping("/atualizar-todos")
    public ResponseEntity<Map<String, Object>> atualizarTodos() {
        List<CotacaoMercado> cotacoes = cotacaoService.atualizarTodosAtivos();
        return ResponseEntity.ok(Map.of(
            "atualizados", cotacoes.size(),
            "cotacoes", cotacoes
        ));
    }

    /** GET /api/cotacoes/PETR4/historico */
    @GetMapping("/{ticker}/historico")
    public List<CotacaoMercado> historico(@PathVariable String ticker) {
        return cotacaoService.historicoCotacoes(ticker.toUpperCase());
    }
}
