package com.corretora.controller;

import com.corretora.model.Empresa;
import com.corretora.service.EmpresaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/empresas")
@CrossOrigin(origins = "*")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @GetMapping
    public List<Empresa> listar() {
        return empresaService.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empresa> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(empresaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Empresa> cadastrar(@RequestBody Map<String, Object> body) {
        Empresa empresa = empresaService.cadastrar(
            (String) body.get("nome"),
            (String) body.get("cnpj"),
            (String) body.get("setor"),
            new BigDecimal(body.get("capitalMercado").toString())
        );
        return ResponseEntity.ok(empresa);
    }
}
