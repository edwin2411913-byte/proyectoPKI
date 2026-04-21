package com.pki.pkitest.Services.Seguridad;

import com.pki.pkitest.Entitys.Role;
import com.pki.pkitest.Entitys.Users;
import com.pki.pkitest.Repositoris.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserSecurityServices implements UserDetailsService {

    private final UserRepository userRepository;

    public UserSecurityServices (UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName){
        Users userEntity = this.userRepository.findByusername(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User " +  userName + " not found"));


        String[] roles = userEntity.getRoles().stream()
                .map(Role::getName)
                .toArray(String[]::new);


        return User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .roles(roles)
                .build();

    }

}
