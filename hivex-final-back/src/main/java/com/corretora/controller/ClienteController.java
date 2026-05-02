package com.corretora.controller;

import com.corretora.model.Cliente;
import com.corretora.model.Ordem;
import com.corretora.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public List<Cliente> listar() {
        return clienteService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Cliente> cadastrar(@RequestBody Map<String, Object> body) {
        Cliente cliente = clienteService.cadastrar(
            (String) body.get("nome"),
            (String) body.get("cpf"),
            (String) body.get("email"),
            new BigDecimal(body.get("saldoInicial").toString())
        );
        return ResponseEntity.ok(cliente);
    }

    @PostMapping("/{id}/depositar")
    public ResponseEntity<Void> depositar(@PathVariable Long id,
                                          @RequestBody Map<String, Object> body) {
        clienteService.depositar(id, new BigDecimal(body.get("valor").toString()));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/comprar")
    public ResponseEntity<Ordem> comprar(@PathVariable Long id,
                                         @RequestBody Map<String, Object> body) {
        Ordem ordem = clienteService.comprar(
            id,
            Long.valueOf(body.get("ativoId").toString()),
            Long.valueOf(body.get("corretoraId").toString()),
            Integer.parseInt(body.get("quantidade").toString())
        );
        return ResponseEntity.ok(ordem);
    }

    @PostMapping("/{id}/vender")
    public ResponseEntity<Ordem> vender(@PathVariable Long id,
                                        @RequestBody Map<String, Object> body) {
        Ordem ordem = clienteService.vender(
            id,
            Long.valueOf(body.get("ativoId").toString()),
            Long.valueOf(body.get("corretoraId").toString()),
            Integer.parseInt(body.get("quantidade").toString())
        );
        return ResponseEntity.ok(ordem);
    }
}
