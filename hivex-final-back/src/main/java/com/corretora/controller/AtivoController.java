package com.corretora.controller;

import com.corretora.model.Ativo;
import com.corretora.model.enums.Liquidez;
import com.corretora.service.AtivoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ativos")
@CrossOrigin(origins = "*")
public class AtivoController {

    private final AtivoService ativoService;

    public AtivoController(AtivoService ativoService) {
        this.ativoService = ativoService;
    }

    @GetMapping
    public List<Ativo> listar() {
        return ativoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ativo> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(ativoService.buscarPorId(id));
    }

    @PatchMapping("/{id}/preco")
    public ResponseEntity<Ativo> atualizarPreco(@PathVariable Long id,
                                                @RequestBody Map<String, Object> body) {
        Ativo ativo = ativoService.atualizarPreco(id, new BigDecimal(body.get("preco").toString()));
        return ResponseEntity.ok(ativo);
    }

    @PostMapping("/acao")
    public ResponseEntity<Ativo> criarAcao(@RequestBody Map<String, Object> body) {
        return ResponseEntity.ok(ativoService.criarAcao(
            (String) body.get("ticker"),
            (String) body.get("nome"),
            new BigDecimal(body.get("precoAtual").toString()),
            Liquidez.valueOf(body.get("liquidez").toString()),
            Long.valueOf(body.get("empresaId").toString()),
            new BigDecimal(body.get("percentualCapital").toString()),
            new BigDecimal(body.get("dividendYield").toString())
        ));
    }

    @PostMapping("/fii")
    public ResponseEntity<Ativo> criarFII(@RequestBody Map<String, Object> body) {
        return ResponseEntity.ok(ativoService.criarFII(
            (String) body.get("ticker"),
            (String) body.get("nome"),
            new BigDecimal(body.get("precoAtual").toString()),
            Liquidez.valueOf(body.get("liquidez").toString()),
            Long.valueOf(body.get("empresaId").toString()),
            (String) body.get("tipo"),
            new BigDecimal(body.get("rendimentoMensal").toString())
        ));
    }

    @PostMapping("/titulo")
    public ResponseEntity<Ativo> criarTitulo(@RequestBody Map<String, Object> body) {
        return ResponseEntity.ok(ativoService.criarTitulo(
            (String) body.get("ticker"),
            (String) body.get("nome"),
            new BigDecimal(body.get("precoAtual").toString()),
            Liquidez.valueOf(body.get("liquidez").toString()),
            Long.valueOf(body.get("empresaId").toString()),
            LocalDate.parse(body.get("vencimento").toString()),
            new BigDecimal(body.get("taxaJuros").toString())
        ));
    }
}
