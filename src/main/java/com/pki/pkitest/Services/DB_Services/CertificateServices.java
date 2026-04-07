package com.pki.pkitest.Services.DB_Services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pki.pkitest.Repositoris.CertificateRepository;

@Service
public class CertificateServices {

    @Autowired
    private CertificateRepository certificateRepository;

    public void saveCertificate(String serialNumber,
        UUID userId,
        UUID caId,
        String commonName,
        String certificatePem,
        Integer daysValid           // Validez
) {
    try {
        certificateRepository.generateCertificate(
            serialNumber,
            userId,
            caId,
            commonName,
            certificatePem,
            daysValid );
        
        System.out.println("Certificado guardados exitosamente (Transacción Completa)");
        
    } catch (Exception e) {
        // Si falla la entidad o el certificado, el Procedure hace Rollback
        throw new RuntimeException("Error en la operación atómica: " + e.getMessage());
    }
}
}
