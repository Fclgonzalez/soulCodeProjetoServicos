package com.soulcode.Servicos.Services;

import com.soulcode.Servicos.Models.Chamado;
import com.soulcode.Servicos.Models.Pagamento;
import com.soulcode.Servicos.Models.StatusChamado;
import com.soulcode.Servicos.Models.StatusPagamento;
import com.soulcode.Servicos.Repositories.ChamadoRepository;
import com.soulcode.Servicos.Repositories.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PagamentoService {

    @Autowired
    PagamentoRepository pagamentoRepository;

    @Autowired
    ChamadoRepository chamadoRepository;

    @Cacheable("pagamentosCache")
    public List<Pagamento> mostrarTodosPagamento() {
        return pagamentoRepository.findAll();
    }

    @Cacheable(value = "pagamentosCache", key = "#idPagamento")
    public Pagamento mostrarPagamentoPeloId(Integer idPagamento) {
        Optional<Pagamento> pagamento = pagamentoRepository.findById(idPagamento);
        return pagamento.orElseThrow();
    }

    @Cacheable(value = "pagamentosCache", key = "#status")
    public List<Pagamento> mostrarPagamentosPorStatus(String status) {
        return pagamentoRepository.findByStatus(status);
    }

    @CachePut(value = "pagamentosCache", key = "#idChamado")
    public Pagamento cadastrarPagamento(Integer idChamado, Pagamento pagamento) {
        Optional<Chamado> chamado = chamadoRepository.findById(idChamado);

        if(chamado.isPresent()) {
            pagamento.setIdPagamento(idChamado);
            pagamento.setStatus(StatusPagamento.LANCADO);
            chamado.get().setPagamento(pagamento);
            chamadoRepository.save(chamado.get());
            return pagamentoRepository.save(pagamento);
        } else {
            throw new RuntimeException();
        }
    }

    @CachePut(value = "pagamentosCache", key = "#pagamento.idPagamento")
    public Pagamento editarPagamento(Pagamento pagamento) {
        return pagamentoRepository.save(pagamento);
    }

    @CacheEvict(value = "pagamentosCache", key = "#idPagamento")
    public void excluirPagamento(Integer idPagamento) {
        pagamentoRepository.deleteById(idPagamento);
    }


    @CachePut(value = "pagamentosCache", key = "#idPagamento")
    public Pagamento modificarStatus(Integer idPagamento, String status) {
        Optional<Pagamento> pagamento = pagamentoRepository.findById(idPagamento);
        switch (status){
            case "LANCADO":
                pagamento.get().setStatus(StatusPagamento.LANCADO);
                break;

            case "QUITADO":
                pagamento.get().setStatus(StatusPagamento.QUITADO);
                break;
        }
        return pagamentoRepository.save(pagamento.get());
    }

    @Cacheable("pagamentosCache")
    public List<List> orcamentoComChamadoCliente(){
        return pagamentoRepository.orcamentoComChamadoCliente();
    }
}
