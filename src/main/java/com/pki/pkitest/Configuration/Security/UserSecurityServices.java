package com.pki.pkitest.Configuration.Security;

import com.pki.pkitest.Entitys.Role;
import com.pki.pkitest.Entitys.Users;
import com.pki.pkitest.Repositoris.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserSecurityServices implements UserDetailsService {

    private final UserRepository userRepository;

    public UserSecurityServices (UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        Users userEntity = this.userRepository.findByusername(username)
                            .orElseThrow(() -> new UsernameNotFoundException("User; " + username + "No encontrado"));
        String[] roles = userEntity.getRoles().stream().map(Role::getName).toArray(String[]::new);

        return User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .roles(roles)
                .build();
    }

}
