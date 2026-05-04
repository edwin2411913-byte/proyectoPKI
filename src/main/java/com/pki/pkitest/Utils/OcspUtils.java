package com.pki.pkitest.Utils;

import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.ocsp.OCSPObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cert.ocsp.CertificateID;
import org.bouncycastle.cert.ocsp.OCSPException;
import org.bouncycastle.cert.ocsp.OCSPReq;
import org.bouncycastle.cert.ocsp.OCSPReqBuilder;
import org.bouncycastle.operator.DigestCalculator;
import org.bouncycastle.operator.DigestCalculatorProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.HexFormat;

@Service
public class OcspUtils {

    private  final CertificateUtils certificateUtils;

    public OcspUtils(CertificateUtils certificateUtils){
        this.certificateUtils = certificateUtils;
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

}
