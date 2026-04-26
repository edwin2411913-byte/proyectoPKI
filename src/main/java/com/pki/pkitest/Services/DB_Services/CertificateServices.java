package com.pki.pkitest.Services.DB_Services;

import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.stereotype.Service;

import com.pki.pkitest.Repositoris.CertificateRepository;

@Service
public class CertificateServices {

    
    private CertificateRepository certificateRepository;

    public CertificateServices(CertificateRepository certificateRepository){
        this.certificateRepository = certificateRepository;
    }

    public void saveCertificate(String serialNumber,
        UUID userId,
        UUID caId,
        String commonName,
        String certificatePem, String type
    ) {
        System.out.println(type);
        try {
            if("CA".equalsIgnoreCase(type)){
                String alias = "CA_"+type;
                certificateRepository.addCaCertificate(
                        userId, commonName,alias,"INTERMEDIATE",caId,certificatePem,""
                );
            }
            else {
                certificateRepository.addEndEntityCertificate(
                        serialNumber,userId,caId, commonName, certificatePem,
                        OffsetDateTime.now(),
                        OffsetDateTime.now().plusYears(1));}
        }catch (Exception e) {
                // Si falla la entidad o el certificado, el Procedure hace Rollback
                throw new RuntimeException("Error en la operación atómica: " + e.getMessage());
            }
        }
}

