package com.soulcode.Servicos.Repositories;

import com.soulcode.Servicos.Models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ChamadoRepository extends JpaRepository<Chamado, Integer> {

    List<Chamado> findByCliente(Optional<Cliente> cliente);

    List<Chamado> findByFuncionario(Optional<Funcionario> funcionario);

    @Query(value = "SELECT * FROM chamado WHERE status =:status", nativeQuery = true)
    List<Chamado> findByStatus(String status);

    @Query(value = "SELECT * FROM chamado WHERE data_entrada BETWEEN :data1 AND :data2", nativeQuery = true)
    List<Chamado> findByIntervaloData(Date data1, Date data2);

//    @Query(value = "SELECT chamado.id_chamado, chamado.titulo, pagamento.id_pagamento, pagamento.forma_de_pagamento, pagamento.status, pagamento.valor, cliente.id_cliente, cliente.nome FROM chamado RIGHT JOIN pagamento ON chamado.id_pagamento = pagamento.id_pagamento LEFT JOIN cliente ON cliente.id_cliente = chamado.id_cliente", nativeQuery = true)
//    List<Chamado> findByClientePagamento();


}
