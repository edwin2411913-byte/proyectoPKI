package com.pki.pkitest.Services;


import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.springframework.stereotype.Service;

import com.pki.pkitest.Utils.CertificateUtils;

@Service
public class PkiServices {
    public CertificateUtils certificateUtils;


    public PkiServices(CertificateUtils certificateUtils){
        this.certificateUtils = certificateUtils;
    }

    public String getCertificate(String stringCSR , String type){

        PKCS10CertificationRequest csr =  certificateUtils.convertoCSR(stringCSR);
        String caAlias;
        if ("MTLS".equals(type.toUpperCase())) {
            caAlias = "CA_MTLS";
        } else if ("KDH".equals(type.toLowerCase())) {
            caAlias = "CA_KDH";
        } else if ("KRD".equals(type.toUpperCase())) {
             caAlias = "CA_KRD";
        } else {
            throw new IllegalArgumentException("Tipo de certificado no soportado: " + type);
        }

        X509Certificate caCert = certificateUtils.getCaCert(caAlias);
        PrivateKey caPrivateKey = certificateUtils.getCaPrivateKey(caAlias);
        
        return certificateUtils.getCertificate(csr, caCert, caPrivateKey, type);
    }

}
