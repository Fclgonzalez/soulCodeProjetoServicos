package com.soulcode.Servicos.Repositories;

import com.soulcode.Servicos.Models.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {


}
