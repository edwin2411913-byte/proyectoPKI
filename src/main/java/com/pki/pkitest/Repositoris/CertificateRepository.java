package com.pki.pkitest.Repositoris;


import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pki.pkitest.Entitys.Certificates;



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
    
}
