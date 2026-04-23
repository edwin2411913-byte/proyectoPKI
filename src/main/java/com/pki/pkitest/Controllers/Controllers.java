package com.pki.pkitest.Controllers;

import org.springframework.web.bind.annotation.RestController;


import com.pki.pkitest.Models.DecryptData;
import com.pki.pkitest.Models.EncriptData;
import com.pki.pkitest.Models.JWSGeneral;
import com.pki.pkitest.Models.RequestCSRModel;
import com.pki.pkitest.Services.JwsServices;
import com.pki.pkitest.Services.PkiServices;
import com.pki.pkitest.Services.ServisCifer;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
public class Controllers {

    private final ServisCifer ciferServis;
    private final PkiServices pkiServices;
    private final JwsServices jwsServices;


    public Controllers(ServisCifer servisCifer, PkiServices pkiServices, JwsServices jwsServices){
        this.ciferServis = servisCifer;
        this.pkiServices = pkiServices;
        this.jwsServices = jwsServices;
        
    }

    @PostMapping("/ciferAES")
    public ResponseEntity<String> ciferAES(@RequestBody EncriptData plainText) {
        return ResponseEntity.ok(ciferServis.ciferAes(plainText.getPlanText()));
    }

    @PostMapping("/deciferAES")
    public ResponseEntity<String> desciferAES(@RequestBody DecryptData decryptData) {
        return ResponseEntity.ok(ciferServis.deciferAES(decryptData.getDecryptData()));
    }

    @PostMapping("/issueCertificate")
    public ResponseEntity<String> issueCertificate(@RequestAttribute("payload_jws") Object payload,
                                                    @RequestAttribute("userID") UUID userId) {
        RequestCSRModel csr = jwsServices.convertJWTInCSR(payload);
        return ResponseEntity.ok(pkiServices.getCertificate(csr.getCsrRequest(), csr.getType(), userId));
    }

    @PostMapping("getJWS")
    public ResponseEntity<String> postMethodName(Authentication authentication,@RequestBody JWSGeneral paylod) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    
        return ResponseEntity.ok(jwsServices.crearJWS(paylod, userDetails.getUsername()));
    }

    

}