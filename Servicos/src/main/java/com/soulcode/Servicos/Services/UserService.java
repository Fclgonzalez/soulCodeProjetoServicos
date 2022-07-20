package com.soulcode.Servicos.Services;

import com.soulcode.Servicos.Models.User;
import com.soulcode.Servicos.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Cacheable("usersCache")
    public List<User> mostrarTodosUsuarios() {
        return userRepository.findAll();
    }

    @CachePut(value = "usersCache", key = "#user.id")
    public User cadastrarUsuario(User user) {
        return userRepository.save(user);
    }

}
