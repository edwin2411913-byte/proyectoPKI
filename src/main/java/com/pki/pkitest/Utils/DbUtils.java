package com.pki.pkitest.Utils;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.pki.pkitest.Services.DB_Services.CertificateServices;

@Service
public class DbUtils {
    private CertificateServices certificateServices;

    public DbUtils(CertificateServices certificateServices){
        this.certificateServices = certificateServices;
    }

    public void saveCertificate(String serialName, String CommonName, String cerPem, UUID caId, UUID userId, String type){

        
        certificateServices.saveCertificate(serialName, userId, caId, CommonName, cerPem, type);
    }

}
