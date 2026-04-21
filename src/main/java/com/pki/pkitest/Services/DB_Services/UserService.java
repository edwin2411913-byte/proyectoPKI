package com.pki.pkitest.Services.DB_Services;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import com.pki.pkitest.Entitys.Users;
import com.pki.pkitest.Repositoris.UserRepository;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService (UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public Users getUser(String username){

        return userRepository.findByusername(username)
                        .orElseThrow(() ->  new BadCredentialsException("Usuario no valida"));               
     
    }

}
