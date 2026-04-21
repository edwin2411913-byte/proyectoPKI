package com.pki.pkitest.Services;

import org.springframework.stereotype.Service;

import com.pki.pkitest.Models.JWSGeneral;

import com.pki.pkitest.Models.RequestCSRModel;
import com.pki.pkitest.Utils.JwsUtils;


@Service
public class JwsServices {

    private JwsUtils jwsUtils;

    public JwsServices (JwsUtils jwsUtils){
        this.jwsUtils = jwsUtils;
    }


    public String crearJWS(JWSGeneral contenido , String user){
        return jwsUtils.crearJWS(contenido, user);
    }

    public RequestCSRModel convertJWTInCSR (  Object signedJWT){
        return jwsUtils.convertJWTInCSR(signedJWT);
    }
}
 