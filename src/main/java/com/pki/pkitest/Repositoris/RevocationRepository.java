package com.pki.pkitest.Repositoris;

import com.pki.pkitest.Entitys.AppEnums;
import com.pki.pkitest.Entitys.Revocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface RevocationRepository extends JpaRepository<Revocation, Integer> {

    @Transactional
    @Modifying
    @Query(value = "CALL public.sp_revoke_certificate(" +
            ":p_serial_number, " +
            "CAST(:p_reason AS revocation_reason), " +
            ":p_user_id, " +
            ":p_ip_address)",
            nativeQuery = true)
    void revokeCertificate(
            @Param("p_serial_number") String p_serial_number,
            @Param("p_reason") String p_reason, // Recibimos el String para el CAST
            @Param("p_user_id") UUID p_user_id,
            @Param("p_ip_address") String p_ip_address
    );


}
