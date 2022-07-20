package com.soulcode.Servicos.Controllers;

import com.soulcode.Servicos.Models.Chamado;
import com.soulcode.Servicos.Models.Pagamento;
import com.soulcode.Servicos.Services.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("servicos")
public class PagamentoController {

    @Autowired
    PagamentoService pagamentoService;


    @GetMapping("/pagamentos")
    public List<Pagamento> mostrarTodosPagamentos() {
        return pagamentoService.mostrarTodosPagamento();
    }

    @GetMapping("/pagamentos/{idPagamento}")
    public ResponseEntity<Pagamento> mostrarPagamentoPeloId(@PathVariable Integer idPagamento) {
        Pagamento pagamento =  pagamentoService.mostrarPagamentoPeloId(idPagamento);
        return ResponseEntity.ok().body(pagamento);
    }

    @GetMapping("/pagamentosPorStatus")
    public List<Pagamento> mostrarPagamentosPorStatus(@RequestParam("status") String status) {
        List<Pagamento> pagamentos = pagamentoService.mostrarPagamentosPorStatus(status);
        return pagamentos;
    }

    @GetMapping("/pagamentosChamadoCliente")
    public List<List> orcamentoComChamadoCliente() {
        List<List> orcamentos = pagamentoService.orcamentoComChamadoCliente();
        return orcamentos;
    }

    @PostMapping("/pagamentos/{idChamado}")
    public ResponseEntity<Pagamento> cadastrarPagamento(@PathVariable Integer idChamado, @RequestBody Pagamento pagamento) {
        try{
            pagamento = pagamentoService.cadastrarPagamento(idChamado, pagamento);
            URI novaUri = ServletUriComponentsBuilder.fromCurrentRequest().path("id").buildAndExpand(pagamento.getIdPagamento()).toUri();
            return ResponseEntity.created(novaUri).body(pagamento);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @PutMapping("/pagamentos/{idPagamento}")
    public ResponseEntity editarPagamento(@PathVariable Integer idPagamento, @RequestBody Pagamento pagamento) {
        pagamento.setIdPagamento(idPagamento);
        pagamento = pagamentoService.editarPagamento(pagamento);
        return ResponseEntity.ok().body(pagamento);
    }


    @DeleteMapping("/pagamentos/{idPagamento}")
    public ResponseEntity<Void> excluirPagamento(@PathVariable Integer idPagamento) {
        pagamentoService.excluirPagamento(idPagamento);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/pagamentosModificarStatus/{idPagamento}")
    public ResponseEntity<Pagamento> modificarStatus(@PathVariable Integer idPagamento, @RequestParam("status") String status) {
        pagamentoService.modificarStatus(idPagamento, status);
        return ResponseEntity.noContent().build();
    }


}
