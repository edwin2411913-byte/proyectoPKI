package com.pki.pkitest.Repositoris;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.pki.pkitest.Entitys.CertificateAuthority;


@Repository
public interface CaRepository extends JpaRepository<CertificateAuthority, UUID> {

    // Nuevo método para buscar por alias exacto
    Optional<CertificateAuthority> findByAlias(String alias);



}
