package com.pki.pkitest.Services;


import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.UUID;

import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pki.pkitest.Utils.CertificateUtils;

@Service
public class PkiServices {
    public CertificateUtils certificateUtils;

    @Value("${app.ca.mtls.alias}")
    private String mtlsAlias;

    @Value("${app.ca.mtls.id}")
    private UUID mtlsId;

    @Value("${app.ca.KRD.alias}")
    private String krdAlias;

    @Value("${app.ca.KRD.id}")
    private UUID krdId;

    @Value("${app.ca.KDH.alias}")
    private String kdhAlias;

    @Value("${app.ca.KDH.id}")
    private UUID kdhId;

    @Value("${app.ca.ROOT.alias}")
    private String rootAlias;

    @Value("${app.ca.ROOT.id}")
    private UUID rootId;


    public PkiServices(CertificateUtils certificateUtils){
        this.certificateUtils = certificateUtils;
    }

    public String getCertificate(String stringCSR , String type, UUID userId){

        PKCS10CertificationRequest csr =  certificateUtils.convertoCSR(stringCSR);
        String caAlias;
        UUID caId;
        if ("MTLS".equals(type.toUpperCase())) {
            caAlias = mtlsAlias;
            caId = mtlsId;
        } else if ("KDH".equals(type.toLowerCase())) {
            caAlias = kdhAlias;
            caId = kdhId;
        } else if ("KRD".equals(type.toUpperCase())) {
             caAlias = krdAlias;
             caId = krdId;
        } else {
            throw new IllegalArgumentException("Tipo de certificado no soportado: " + type);
        }

        X509Certificate caCert = certificateUtils.getCaCert(caAlias);
        PrivateKey caPrivateKey = certificateUtils.getCaPrivateKey(caAlias);
        
        return certificateUtils.getCertificate(csr, caCert, caPrivateKey, type, caId, userId);
    }

    public String getCaCertifice(String stringCSR , String type, UUID userId){

        PKCS10CertificationRequest csr =  certificateUtils.convertoCSR(stringCSR);
        String caAlias;
        UUID caId;
        if ("CA".equalsIgnoreCase(type) || "CA_OCSP".equalsIgnoreCase(type)) {
            caAlias = rootAlias;
            caId = rootId;
        } else {
            throw new IllegalArgumentException("Tipo de certificado no esta soportado: " + type);
        }

        X509Certificate caCert = certificateUtils.getCaCert(caAlias);
        PrivateKey caPrivateKey = certificateUtils.getCaPrivateKey(caAlias);

        return certificateUtils.getCertificate(csr, caCert, caPrivateKey, type, caId, userId);
    }

}
