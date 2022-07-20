package com.soulcode.Servicos.Services;

import com.soulcode.Servicos.Models.Cliente;
import com.soulcode.Servicos.Models.Endereco;
import com.soulcode.Servicos.Repositories.ClienteRepository;
import com.soulcode.Servicos.Repositories.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnderecoService {

    @Autowired
    EnderecoRepository enderecoRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Cacheable("enderecosCache")
    public List<Endereco> mostrarTodosEnderecos() {
        return enderecoRepository.findAll();
    }

    @Cacheable(value = "enderecosCache", key = "#idEndereco")
    public Endereco mostrarEnderecoPeloId(Integer idEndereco){
        Optional<Endereco> endereco = enderecoRepository.findById(idEndereco);
        return endereco.orElseThrow();
    }

    // REGRAS PARA CADASTRO DE ENDEREÇO:
    // 1 -> para cadastrar um endereço, o cliente já deve estar cadastrado no DB
    // 2 -> no momento do cadastro do endereço precisamos passar o id do cliente dono desse endereço
    // 3 -> o id do endereço vai ser o mesmo id do cliente
    // 4 -> não permitir que um endereço seja salvo sem a existência do respectivo cliente

    @CachePut(value = "enderecosCache", key = "#enreco.idEndereco")
    public Endereco cadastrarEndereco(Endereco endereco, Integer idCliente){
        // declarando um optional de cliente e atribuindo para este os dados do cliente que receberá o novo endereço
        Optional<Cliente> cliente = clienteRepository.findById(idCliente);

        if(cliente.isPresent()) {
        endereco.setIdEndereco(idCliente);
        enderecoRepository.save(endereco);
        cliente.get().setEndereco(endereco);
        clienteRepository.save(cliente.get());
        return endereco;

        } else {
            throw new RuntimeException();
        }

    }

    @CachePut(value = "enderecosCache", key = "#enreco.idEndereco")
    public Endereco editarEndereco(Endereco endereco){
        return enderecoRepository.save(endereco);
    }

}
