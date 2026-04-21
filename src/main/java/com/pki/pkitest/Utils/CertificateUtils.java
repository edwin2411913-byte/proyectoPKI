package com.pki.pkitest.Utils;

import java.io.StringWriter;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Date;

import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509ExtensionUtils;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.openssl.jcajce.JcaMiscPEMGenerator;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DigestCalculator;
import org.bouncycastle.operator.bc.BcDigestCalculatorProvider;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequest;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.io.pem.PemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class CertificateUtils {

    private DataBaseUtils dataBaseUtils;

    public KeyStore ks;

    public CertificateUtils(@Qualifier("hsmKeyStore") KeyStore keyStore, DataBaseUtils dataBaseUtils) {
        this.ks = keyStore;
        this.dataBaseUtils = dataBaseUtils;
    }

    public X509Certificate getCaCert(String alias){
        try{
            X509Certificate caCert = (X509Certificate)ks.getCertificate(alias);

            if(caCert == null){
                throw new RuntimeException("No se pudo cargar el certificado del HSM, no existe el alias ");
            }
            return caCert;
        }catch(Exception e){
            throw new RuntimeException("No se pudo cargar el certificado del HSM"+ e.getMessage());
        }
    }


    public PrivateKey getCaPrivateKey(String alias){
        try{
        PrivateKey caPrivateKey = (PrivateKey)ks.getKey(alias,"EECF7DC7F3ADE3F73DA9A2CEEAF3".toCharArray());
        
        if(caPrivateKey == null){
            throw new RuntimeException("No se pudo cargar la llave privada del HSM, no existe el alias ");
        }
        return caPrivateKey;

        }catch(Exception e){
            throw new RuntimeException("No se pudo cargar la llave privada");
        }    
    }

    public String getCertificate(PKCS10CertificationRequest csr, X509Certificate caCert , PrivateKey caPrivateKey, String type){
        
        try{

        BigInteger serial = BigInteger.valueOf(System.currentTimeMillis());
        Date notBefore = new Date();
        Date notAfter = new Date(System.currentTimeMillis() + ((365L * 24 * 60 * 60 * 1000))*2); 

        X509v3CertificateBuilder cBuilder = new JcaX509v3CertificateBuilder(
            X500Name.getInstance(caCert.getSubjectX500Principal().getEncoded()), 
            serial,
            notAfter,
            notBefore, 
            csr.getSubject(),  
            new JcaPKCS10CertificationRequest(csr).getPublicKey()
        );
        
        cBuilder = CreateExtencion(cBuilder,type ,csr, caCert);


        ContentSigner signer = new JcaContentSignerBuilder("SHA256WITHRSA")
                                .setProvider(ks.getProvider())
                                .build(caPrivateKey);


        X509CertificateHolder holder = cBuilder.build(signer);

        String cert = convertoToPem( new JcaX509CertificateConverter().getCertificate(holder));
        //dataBaseUtils.guardarCertificado(cert, serial.toString(), csr.getSubject().toString(), 730);
        return cert;
        
        
        }catch(Exception e){
            throw new RuntimeException("error"+ e.getMessage());
        }
    }

    private X509v3CertificateBuilder CreateExtencion (X509v3CertificateBuilder cBuilder, String type, PKCS10CertificationRequest csr, X509Certificate caCert){
        try{    
        ////Agregamos Extencion de Entidad fianl
            BasicConstraints basicConstraints = new BasicConstraints(false);
            cBuilder.addExtension(Extension.basicConstraints, false, basicConstraints);

            //Cargamos las extecion del Nombre del emisor
            final SubjectPublicKeyInfo publicKeyInfo1 = SubjectPublicKeyInfo.getInstance( new JcaPKCS10CertificationRequest(csr).getPublicKey().getEncoded());
            final DigestCalculator digCalc1 = new BcDigestCalculatorProvider().get(new AlgorithmIdentifier(OIWObjectIdentifiers.idSHA1));
            SubjectKeyIdentifier subjectKeyIdentifier = new X509ExtensionUtils(digCalc1).createSubjectKeyIdentifier(publicKeyInfo1);
            cBuilder.addExtension(Extension.subjectKeyIdentifier, false, subjectKeyIdentifier);

            //Cargamos la extecion del nombre del titular
            final SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(caCert.getPublicKey().getEncoded());
            final DigestCalculator digCalc = new BcDigestCalculatorProvider().get(new AlgorithmIdentifier(OIWObjectIdentifiers.idSHA1));
            AuthorityKeyIdentifier authorityKeyIdentifier = new X509ExtensionUtils((digCalc)).createAuthorityKeyIdentifier(publicKeyInfo);
            cBuilder.addExtension(Extension.authorityKeyIdentifier, false, authorityKeyIdentifier);

            //Cargamos los usos del certificado
            KeyUsage keyUsage = new KeyUsage(KeyUsage.nonRepudiation | KeyUsage.digitalSignature);
            cBuilder.addExtension(Extension.keyUsage, false, keyUsage);

            //Cargamos la extencion de proposito del certfificado para - mtls
            if(type.equals("MTLS")){

                KeyPurposeId[] keyPurposeIds = new KeyPurposeId[]{KeyPurposeId.id_kp_clientAuth};
                ExtendedKeyUsage eku = new ExtendedKeyUsage(keyPurposeIds);
                
                cBuilder.addExtension(Extension.extendedKeyUsage ,false ,eku);
            return cBuilder;
            }

            return cBuilder;
            
        } catch (Exception e) {
            throw new RuntimeException("No se puedieron crear las extencio del certificado");
        }
    }



    private static String convertoToPem(Object object){

       if(object == null){
        throw new IllegalArgumentException("El objeto a convertir no puede ser nulo");
       }
        try(
            StringWriter sw= new StringWriter();
            PemWriter pw = new PemWriter(sw)){
                pw.writeObject(new JcaMiscPEMGenerator(object));
                pw.flush();
                return sw.toString();
        }
        

        catch(Exception e){
            throw new RuntimeException("error"+ e.getMessage());
        }
    }


    public PKCS10CertificationRequest convertoCSR (String stringcsr){
        try{
            byte[] derEncoded = Base64.decode(stringcsr.replace("\n", "").replace("\r", "").trim());

            return new PKCS10CertificationRequest(derEncoded);

        }catch(Exception e){
            throw new RuntimeException("Erro al convertir cadena string csr a PKCS10");
        }
    }


}
