package com.soulcode.Servicos.Services;

import com.soulcode.Servicos.Models.*;
import com.soulcode.Servicos.Repositories.ChamadoRepository;
import com.soulcode.Servicos.Repositories.ClienteRepository;
import com.soulcode.Servicos.Repositories.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ChamadoService {

    @Autowired
    ChamadoRepository chamadoRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    FuncionarioRepository funcionarioRepository;

    @Cacheable("chamadosCache")
    public List<Chamado> mostrarTodosChamados() {
        return chamadoRepository.findAll();
    }

    @Cacheable(value = "chamadosCache", key = "#idChamado")
    public Chamado mostrarChamadoPeloId(Integer idChamado) {
        Optional<Chamado> chamado = chamadoRepository.findById(idChamado);
        return chamado.orElseThrow();
    }

    @Cacheable(value = "chamadosCache")
    public List<Chamado> buscarChamadosPorCliente(Integer idCliente) {
        Optional<Cliente> cliente = clienteRepository.findById(idCliente);
        return chamadoRepository.findByCliente(cliente);
    }

    @Cacheable(value = "chamadosCache")
    public List<Chamado> buscarChamadosPorFuncionario(Integer idFuncionario) {
        Optional<Funcionario> funcionario = funcionarioRepository.findById(idFuncionario);
        return chamadoRepository.findByFuncionario(funcionario);
    }

    @Cacheable(value = "chamadosCache")
    public List<Chamado> buscarChamadosPorStatus(String status) {
        return chamadoRepository.findByStatus(status);
    }

    @Cacheable("chamadosCache")
    public List<Chamado> buscarPorIntervaloData(Date data1, Date data2) {
        return chamadoRepository.findByIntervaloData(data1, data2);
    }

    // cadastrar um novo chamado
    // temos 2 regras:
    //  1) no momento do cadastro do chamado já devenos informar de qual cliente é
    //  2) no momento do cadastro do chamado, a principio vamos fazer esse cadastro sem estrar atribuido um funcionario
    //  3) no momento do cadastro do chamado, o status desse chamado deve ser RECEBIDO
    @CachePut(value = "chamadosCache", key = "#chamado.idChamado")
    public Chamado cadastrarChamado(Chamado chamado, Integer idCliente) {
        chamado.setStatus((StatusChamado.RECEBIDO));                            // regra 3
        chamado.setFuncionario(null);                                           // regra 2
        Optional<Cliente> cliente = clienteRepository.findById(idCliente);      // regra 1
        chamado.setCliente(cliente.get());
        return chamadoRepository.save(chamado);
    }

    @CacheEvict(value = "chamadosCache", key = "#idChamado", allEntries = true)
    public void excluirChamado(Integer idChamado) {
        chamadoRepository.deleteById(idChamado);
    }

    //no momento de edição de um chamado devemos preservar o cliente e o fucionario desse chamado
    @CachePut(value = "chamadosCache", key = "#chamado.idChamado")
    public Chamado editarChamado(Chamado chamado, Integer idChamado) {
        Chamado chamadoSemNovasAlteracoes = mostrarChamadoPeloId(idChamado);
        Funcionario funcionario = chamadoSemNovasAlteracoes.getFuncionario();
        Cliente cliente = chamadoSemNovasAlteracoes.getCliente();
        chamado.setFuncionario(funcionario);
        chamado.setCliente(cliente);
        return chamadoRepository.save(chamado);
    }

    // método para atribuir ou trocar o funcionário para um determinado chamado
    // regra: no momento em que um determinado chamado é atribuído a um funcionário o status é alterado para ATRIBUIDO
    @CachePut(value = "chamadosCache", key = "#idFuncionaio")
    public Chamado atribuirFuncionario(Integer idChamado, Integer idFuncionario) {
        Optional<Funcionario> funcionario = funcionarioRepository.findById(idFuncionario);
        Chamado chamado = mostrarChamadoPeloId(idChamado);
        chamado.setFuncionario(funcionario.get());
        chamado.setStatus(StatusChamado.ATRIBUIDO);
        return chamadoRepository.save(chamado);
    }

    @CachePut(value = "chamadosCache", key = "#idChamado")
    public Chamado modificarStatus(Integer idChamado, String status) {
        Chamado chamado = mostrarChamadoPeloId(idChamado);
        switch (status){
            case "ATRIBUIDO":
            {
                if (chamado.getFuncionario() != null) {
                    chamado.setStatus(StatusChamado.ATRIBUIDO);
                    break;
                } else {
                    throw new RuntimeException("Não é possível mudar o status para ATRIBUIDO sem que um funcionário tenha sido atribuído ao chamado.");
                }
            }
            case "CONCLUIDO":
            {
                chamado.setStatus(StatusChamado.CONCLUIDO);
                break;
            }
            case "ARQUIVADO":
            {
                chamado.setStatus(StatusChamado.ARQUIVADO);
                break;
            }
            case "RECEBIDO":
            {
                chamado.setStatus(StatusChamado.RECEBIDO);
                break;
            }
        }
        return chamadoRepository.save(chamado);
    }

//    public List<Chamado> buscarPorCLientePagamento() {
//        return chamadoRepository.findByClientePagamento();
//    }
}
