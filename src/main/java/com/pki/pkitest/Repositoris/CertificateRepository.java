package com.pki.pkitest.Repositoris;


import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pki.pkitest.Entitys.Certificates;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface CertificateRepository extends JpaRepository<Certificates, String> {
    @Procedure(procedureName = "public.sp_add_end_entity_certificate")
    void addEndEntityCertificate(
        @Param("p_serial_number") String serialNumber,
        @Param("p_owner_id") UUID ownerId,
        @Param("p_issuer_ca_id") UUID issuerCaId,
        @Param("p_common_name") String commonName,
        @Param("p_certificate_pem") String certificatePem,
        @Param("p_not_before") OffsetDateTime notBefore,
        @Param("p_not_after") OffsetDateTime notAfter
    );

    @Transactional
    @Modifying
    @Query(value = "CALL public.sp_create_ca_certificate(:p_owner_id, :p_ca_name, :p_alias, CAST(:p_type AS ca_type), :p_parent_ca_id, :p_certificate_pem, :p_crl_url)", nativeQuery = true)
    void addCaCertificate(
            @Param("p_owner_id") UUID ownerId,
            @Param("p_ca_name") String caName,
            @Param("p_alias") String alias,
            @Param("p_type") String caType,
            @Param("p_parent_ca_id") UUID parentCaId,
            @Param("p_certificate_pem") String certificatePem,
            @Param("p_crl_url") String crlUrl
    );

    Optional<Certificates> findBySerialNumber(String serialNumber);

}
