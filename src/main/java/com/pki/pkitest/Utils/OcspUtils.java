package com.pki.pkitest.Utils;

import com.pki.pkitest.Entitys.Certificates;
import com.pki.pkitest.Repositoris.CertificateRepository;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.ocsp.OCSPObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cert.ocsp.*;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DigestCalculator;
import org.bouncycastle.operator.DigestCalculatorProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.HexFormat;

@Service
public class OcspUtils {

    private  final CertificateUtils certificateUtils;
    private CertificateRepository certificateRepository;
    public KeyStore ks;

    public OcspUtils(CertificateUtils certificateUtils, CertificateRepository certificateRepository, @Qualifier("hsmKeyStore") KeyStore keyStore){
        this.certificateUtils = certificateUtils;
        this.certificateRepository = certificateRepository;
        this.ks = keyStore;
    }

    public String getOcspRequest(X509Certificate certificate) {


        String alias = certificateUtils.getCertificateAlias(certificate);

        X509Certificate certificateOcsp = certificateUtils.getCaCert(alias);
        try {
            DigestCalculatorProvider digestCalculatorProvider = new JcaDigestCalculatorProviderBuilder().build();
            DigestCalculator sha256Calculator =  digestCalculatorProvider.get(new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha256, DERNull.INSTANCE));
            CertificateID certId = new CertificateID(sha256Calculator, new JcaX509CertificateHolder(certificateOcsp),
                    //Cambiar por el numero de serie del certificado final
                    certificate.getSerialNumber());

            OCSPReqBuilder gen = new OCSPReqBuilder();
            gen.addRequest(certId);


            byte[] nonce = new byte[16];
            new SecureRandom().nextBytes(nonce);
            Extensions extensions = new Extensions(
                    new Extension(OCSPObjectIdentifiers.id_pkix_ocsp_nonce, false, new DEROctetString(nonce))
            );
            gen.setRequestExtensions(extensions);

            // 6. Generar el objeto OCSPReq y obtener los bytes codificados en DER
            OCSPReq request = gen.build();
            byte[] bytesSolicitud = request.getEncoded();
            String solicitudOcspHex = HexFormat.of().formatHex(bytesSolicitud);
            return solicitudOcspHex;

        }catch (RuntimeException | OperatorCreationException | CertificateEncodingException | OCSPException |
                IOException e) {
            throw new RuntimeException("No se pudo crear la solicitud de OCSP"+ e.getMessage());
        }
    }

    public String verifyChain(X509Certificate cer){

        return certificateUtils.verifyChain(cer);
    }

    public String ocspResponseApi(byte[] ocspRequestBytes){
        byte[] bytes = null;
        OCSPReq ocspRequest = null;
        BigInteger serialNumber=null;
        int estatus = 0;
        try {
            ocspRequest = new OCSPReq(ocspRequestBytes);
            Req[] requestList = ocspRequest.getRequestList();
            CertificateID certificateID = requestList[0].getCertID();
            serialNumber = certificateID.getSerialNumber();


            System.out.println("Número de serie (BigInteger): " + serialNumber);
            System.out.println("Número de serie (Hex): " + serialNumber.toString(16));
        }catch (Exception e) {
            System.err.println(e.getMessage());
        }


        Certificates certificate = certificateRepository.findBySerialNumber(serialNumber.toString())
                .orElseThrow(() -> new RuntimeException("Certificado no encontrado con serial: "));


        if (certificate.getNotAfter().isBefore(OffsetDateTime.now())) {
            estatus = 1; // Marcamos como revocado o expirado según tu lógica interna
        }
        try {
            bytes = genegenerateOcspResponse(ocspRequest, estatus);
            return HexFormat.of().formatHex(bytes);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear la respuesta de OCSP");
        }


    }




public byte[] genegenerateOcspResponse(
        OCSPReq request,
        int status // 0: good, 1: revoked, 2: unknown
) {
    X509Certificate certificate = certificateUtils.getCaCert("CA_OCSP");
    PrivateKey privateKey = certificateUtils.getCaPrivateKey("CA_OCSP");
    if (certificate == null || privateKey == null){
        throw new RuntimeException();
    }

    Req[] requests = request.getRequestList();
    CertificateID certId = requests[0].getCertID();

    //BigInteger bi = requests[0].getCertID().getSerialNumber();
    try {


        BasicOCSPRespBuilder respBuilder = new BasicOCSPRespBuilder(
                new RespID(new JcaX509CertificateHolder(certificate).getSubject()));

        CertificateStatus certificateStatus = null;
        if (status == 0) {
            certificateStatus = CertificateStatus.GOOD;
        } else if (status == 1) {
            // Si está revocado, se puede añadir la fecha y razón
            certificateStatus = new RevokedStatus(new Date(), 0);
        } else {
            certificateStatus = new org.bouncycastle.cert.ocsp.UnknownStatus();
        }

        respBuilder.addResponse(certId, certificateStatus);

        // 4. Firmar la respuesta
        ContentSigner signer = new JcaContentSignerBuilder("SHA256withRSA")
                .setProvider(ks.getProvider())
                .build(privateKey);

        BasicOCSPResp basicResp = respBuilder.build(signer,
                new X509CertificateHolder[]{new JcaX509CertificateHolder(certificate)},
                new Date());

        OCSPResp finalResponse = new OCSPRespBuilder().build(OCSPRespBuilder.SUCCESSFUL, basicResp);
        byte[] derEncodedResoinse = finalResponse.getEncoded();
        return derEncodedResoinse;
    }catch (Exception e) {
        e.printStackTrace();
    throw new RuntimeException(e);
}
}

}
