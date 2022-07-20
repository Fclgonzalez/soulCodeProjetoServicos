package com.soulcode.Servicos.Services;

import com.soulcode.Servicos.Models.Cliente;
import com.soulcode.Servicos.Repositories.ClienteRepository;
import com.soulcode.Servicos.Services.Exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {


    @Autowired // indica onde será feita a injeção de dependencia
    ClienteRepository clienteRepository;

    @Cacheable("clientesCache") // só chama o return se o cache explirar clientesCache
    public List<Cliente> mostrarTodosClientes() {
        return clienteRepository.findAll();
    }

    @Cacheable(value = "clientesCache", key = "#idCliente")
    public Cliente mostrarClientePeloId(Integer idCliente) {
        Optional<Cliente> cliente = clienteRepository.findById(idCliente);
        return cliente.orElseThrow(
                () -> new EntityNotFoundException("Cliente não cadastrado: " + idCliente)
        );
    }

    public Cliente mostrarClientePeloEmail(String email) {
        Optional<Cliente> cliente = clienteRepository.findByEmail(email);
        return cliente.orElseThrow();
    }

    @CachePut(value = "clientesCache", key = "#cliente.idCliente")
    public Cliente cadastrarCliente(Cliente cliente) {
        cliente.setIdCliente(null);
        return clienteRepository.save(cliente);
    }

    @CacheEvict(value = "clientesCache", key="#idCliente", allEntries = true)
    public void excluirCliente(Integer idCliente) {
        clienteRepository.deleteById(idCliente);
    }

    @CachePut(value = "clientesCache", key = "#cliente.idCliente")
    public Cliente editarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }
}
