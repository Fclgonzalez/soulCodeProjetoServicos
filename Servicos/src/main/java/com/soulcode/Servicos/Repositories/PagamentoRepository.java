package com.soulcode.Servicos.Repositories;

import com.soulcode.Servicos.Models.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {

    @Query(value = "SELECT * FROM pagamento where status = :status", nativeQuery = true)
    List<Pagamento> findByStatus(String status);

    @Query(value = "SELECT pagamento.*, chamado.id_chamado, chamado.titulo, cliente.id_cliente, cliente.nome " +  "FROM chamado RIGHT JOIN pagamento ON chamado.id_pagamento = pagamento.id_pagamento " + "LEFT JOIN cliente ON cliente.id_cliente = chamado.id_cliente", nativeQuery = true)
    List<List> orcamentoComChamadoCliente();
}
