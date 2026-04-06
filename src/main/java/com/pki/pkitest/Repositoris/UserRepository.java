package com.pki.pkitest.Repositoris;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pki.pkitest.Entitys.Users;

import jakarta.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<Users, UUID> {

    @Transactional
    @Procedure(procedureName =  "public.create_user")
    void createUser(
        @Param("p_username") String username,
        @Param("p_email") String email,
        @Param("p_password")String password,
        @Param("p_role") String role
    );
    }


