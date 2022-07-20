package com.soulcode.Servicos.Services;

import com.soulcode.Servicos.Models.Cargo;
import com.soulcode.Servicos.Models.Funcionario;
import com.soulcode.Servicos.Repositories.CargoRepository;
import com.soulcode.Servicos.Repositories.FuncionarioRepository;
import com.soulcode.Servicos.Services.Exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//quando se fala em serviços estamos falando dos métodos do CRUD da tabela

@Service
public class FuncionarioService {

    // aqui se faz a injeçãode dependência

    @Autowired
    FuncionarioRepository funcionarioRepository;

    @Autowired
    CargoRepository cargoRepository;

    //primeiro serviço na tabela de funcionários: leitura de todos os funcionário cadastrados
    //findAll -> método do spring JPA: busca todos os registros de uma tabela
    public List<Funcionario> mostrarTodosFuncionarios()
    {
        return funcionarioRepository.findAll();
    }

    // vamos criar mais um serviço relacionado ao funcionário
    // criar um serviço de buscar apenas um funcionário pelo seu id (chave primária)

    public Funcionario mostrarUmFuncionarioPeloId(Integer idFuncionario) {
        Optional<Funcionario> funcionario = funcionarioRepository.findById(idFuncionario);
        return funcionario.orElseThrow(
                () -> new EntityNotFoundException("Funcionário não cadastrado: " + idFuncionario)
        );
    }

    //vamos criar mais um serviço para busccar um funcionário pelo seu email

    public Funcionario mostrarUnFuncionarioPeloEmail(String email) {
        Optional<Funcionario> funcionario = funcionarioRepository.findByEmail(email);
        return funcionario.orElseThrow();
    }

    // vamos criar um serviço para cadastrar um novo funcionário

    public Funcionario cadastrarFuncionario(Funcionario funcionario, Integer idCargo) {
        // só por precaução nós vamos colocar o id do funcionário como nulo
        funcionario.setIdFuncionario(null);
        try {
            Optional<Cargo> cargo = cargoRepository.findById(idCargo);
            funcionario.setCargo(cargo.get());
            return funcionarioRepository.save(funcionario);
        } catch  (Exception e){
            throw new DataIntegrityViolationException("Erro ao cadastrar funcionário");
        }
    }

    public void excluirFuncionario(Integer idFuncionario) {

        funcionarioRepository.deleteById(idFuncionario);
    }

    public Funcionario editarFuncionario(Funcionario funcionario) {

        return funcionarioRepository.save(funcionario);
    }

    public Funcionario salvarFoto (Integer idFuncionario, String caminhoFoto) {
        Funcionario funcionario = mostrarUmFuncionarioPeloId(idFuncionario);
        funcionario.setFoto(caminhoFoto);
        return funcionarioRepository.save(funcionario);
    }

    public List<Funcionario> buscarFuncionariosPorCargo(Integer idCargo) {
        Optional<Cargo> cargo = cargoRepository.findById(idCargo);
        return funcionarioRepository.findByCargo(cargo);
    }

}
