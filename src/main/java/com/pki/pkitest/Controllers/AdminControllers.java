package com.pki.pkitest.Controllers;


import com.pki.pkitest.Models.RequestCSRModel;
import com.pki.pkitest.Services.JwsServices;
import com.pki.pkitest.Services.PkiServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/Admin")
public class AdminControllers {

    private final JwsServices jwsServices;
    private final PkiServices pkiServices;

    public AdminControllers(JwsServices jwsServices, PkiServices pkiServices){

        this.jwsServices = jwsServices;
        this.pkiServices = pkiServices;
    }

    @PostMapping("/getCA")
    public ResponseEntity<?> getCA (@RequestAttribute("payload_jws") Object payload,
                                    @RequestAttribute("userID") UUID userId) {
        RequestCSRModel csr = jwsServices.convertJWTInCSR(payload);

        return ResponseEntity.ok(pkiServices.getCaCertifice(csr.getCsrRequest(), csr.getType(), userId));

    }
}
