package com.pki.pkitest.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pki.pkitest.Repositoris.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void newUser(String username, String email, String password) {
        // Llamada al procedimiento almacenado
        // Si no envías el rol, puedes pasar "USER" o null si el SQL maneja el DEFAULT
        userRepository.createUser(username, email, password, "USER");
    }

}
