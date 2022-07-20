package com.soulcode.Servicos.Repositories;

import com.soulcode.Servicos.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
    public Optional<User> findByLogin(String username);
}
