package com.pki.pkitest.Services;


import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.UUID;

import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.springframework.stereotype.Service;

import com.pki.pkitest.Utils.CertificateUtils;

@Service
public class PkiServices {
    public CertificateUtils certificateUtils;


    public PkiServices(CertificateUtils certificateUtils){
        this.certificateUtils = certificateUtils;
    }

    public String getCertificate(String stringCSR , String type, UUID userId){

        PKCS10CertificationRequest csr =  certificateUtils.convertoCSR(stringCSR);
        String caAlias;
        UUID caId;
        if ("MTLS".equals(type.toUpperCase())) {
            caAlias = "CA_MTLS";
            caId = UUID.fromString("3a388685-c06d-4dd1-9a9a-314c182e3e6c");
        } else if ("KDH".equals(type.toLowerCase())) {
            caAlias = "CA_KDH";
            caId = UUID.fromString("981f51a0-bd56-415e-a824-1ba85973de36");
        } else if ("KRD".equals(type.toUpperCase())) {
             caAlias = "CA_KRD";
             caId = UUID.fromString("c8af09e8-fe7c-4c95-abcd-3b6daf930cf4");
        } else {
            throw new IllegalArgumentException("Tipo de certificado no soportado: " + type);
        }

        X509Certificate caCert = certificateUtils.getCaCert(caAlias);
        PrivateKey caPrivateKey = certificateUtils.getCaPrivateKey(caAlias);
        
        return certificateUtils.getCertificate(csr, caCert, caPrivateKey, type, caId, userId);
    }

}
