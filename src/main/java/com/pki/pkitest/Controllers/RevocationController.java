package com.pki.pkitest.Controllers;


import com.pki.pkitest.Models.Cert;
import com.pki.pkitest.Services.RevocationServices;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.Map;

@Controller
public class RevocationController {

    private final RevocationServices revocacionServices;

    public RevocationController (RevocationServices revocacionServices){
        this.revocacionServices = revocacionServices;
    }



    @PostMapping("/revocaccion")
    public ResponseEntity<?> ocsp(Authentication authentication, @RequestBody Cert cert){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        revocacionServices.revocateCer(cert.getCert(), userDetails.getUsername());

        return ResponseEntity.ok(Map.of("Revocation:", "ok"));
    }
}
