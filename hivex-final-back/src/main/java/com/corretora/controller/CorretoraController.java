package com.corretora.controller;

import com.corretora.model.*;
import com.corretora.service.CorretoraService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/corretoras")
@CrossOrigin(origins = "*")
public class CorretoraController {

    private final CorretoraService corretoraService;

    public CorretoraController(CorretoraService corretoraService) {
        this.corretoraService = corretoraService;
    }

    @PostMapping
    public ResponseEntity<Corretora> cadastrar(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok(corretoraService.cadastrar(body.get("nome"), body.get("cnpj")));
    }

    @GetMapping("/{id}/portfolio")
    public List<AtivoDisponivel> portfolio(@PathVariable Long id) {
        return corretoraService.listarPortfolio(id);
    }

    @PostMapping("/{id}/portfolio")
    public ResponseEntity<Void> adicionarAtivo(@PathVariable Long id,
                                               @RequestBody Map<String, Object> body) {
        corretoraService.adicionarAtivoAoPortfolio(
            id,
            Long.valueOf(body.get("ativoId").toString()),
            Integer.parseInt(body.get("quantidade").toString()),
            new BigDecimal(body.get("precoOferta").toString())
        );
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/ordens")
    public List<Ordem> ordens(@PathVariable Long id) {
        return corretoraService.listarOrdens(id);
    }
}
