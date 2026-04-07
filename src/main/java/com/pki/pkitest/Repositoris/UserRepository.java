package com.pki.pkitest.Repositoris;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pki.pkitest.Entitys.Users;


@Repository
public interface UserRepository extends JpaRepository<Users, UUID> {

    Optional<Users> findByusername(String alias);
    }


