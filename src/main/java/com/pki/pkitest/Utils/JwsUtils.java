package com.pki.pkitest.Utils;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.pki.pkitest.Models.JWSGeneral;
import com.pki.pkitest.Models.RequestCSRModel;

import tools.jackson.databind.ObjectMapper;

@Service
public class JwsUtils {

    public final KeyStore ks;


    private CertificateUtils certificateUtils;

    @Autowired
    private ObjectMapper objectMapper;

    public JwsUtils(@Qualifier("hsmKeyStore") KeyStore keyStore, CertificateUtils certificateUtils) {
        this.certificateUtils = certificateUtils; 
        this.ks = keyStore;   
    }

     
    public String crearJWS(JWSGeneral contenido){
         
        try{
        
            PrivateKey jwsPrivate= certificateUtils.getCaPrivateKey("CA_JWS");


           JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                                    .subject("USER")
                                    .issueTime(new Date())
                                    .expirationTime(new Date(new Date().getTime() + 3600 * 10000))
                                    .claim("Payloud", contenido)
                                    .build();
            
            SignedJWT signedJWT = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.RS256).build()
                , claimsSet);
            
            JWSSigner signer = new RSASSASigner(jwsPrivate);
            signer.getJCAContext().setProvider(ks.getProvider());
            signedJWT.sign(signer);            
            return signedJWT.serialize(); 
        }catch(JOSEException e){
            System.err.println("Error de firma JOSE: " + e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            System.err.println("Error general: " + e.getMessage());
         e.printStackTrace();}
            return null;
    }

    public RequestCSRModel convertJWTInCSR(Object tokenJws){
        try{
            
            
            Object payloadObject = tokenJws;
            
            if(payloadObject != null){
                return objectMapper.convertValue(payloadObject, RequestCSRModel.class);
            }else{
                throw new RuntimeException("El JWT no contiene el payloud");
            }
           
        }catch(Exception e){
            System.err.print("Error en la validacion: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
       
    }

}
