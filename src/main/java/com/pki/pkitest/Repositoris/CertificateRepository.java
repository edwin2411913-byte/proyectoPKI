package com.pki.pkitest.Repositoris;


import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pki.pkitest.Entitys.Certificates;



@Repository
public interface CertificateRepository extends JpaRepository<Certificates, String> {

    @Procedure(procedureName = "public.sp_generate_certificate", outputParameterName = "p_res_serial")
    String generateCertificate(
        @Param("p_serial_number") String p_serial_number,
        @Param("p_owner_id") UUID p_owner_id,
        @Param("p_issuer_ca_id") UUID p_issuer_ca_id,
        @Param("p_common_name") String p_common_name,
        @Param("p_certificate_pem") String p_certificate_pem,
        @Param("p_days_valid") Integer p_days_valid
    );
}
