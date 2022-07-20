package com.soulcode.Servicos.Controllers;

import com.soulcode.Servicos.Models.User;
import com.soulcode.Servicos.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("servicos")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/usuarios")
    public List<User> mostrarTodosUsuarios() {
        return userService.mostrarTodosUsuarios();
    }

    @PostMapping("/usuarios")
    public ResponseEntity<User> cadastrarFuncionario(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userService.cadastrarUsuario(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
