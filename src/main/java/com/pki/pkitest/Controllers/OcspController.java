package com.pki.pkitest.Controllers;

import com.pki.pkitest.Models.JWSGeneral;
import com.pki.pkitest.Models.RequestOCSP;
import com.pki.pkitest.Services.OcspServices;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/ocsp")
public class OcspController {

    private final OcspServices ocspServices;

    public OcspController(OcspServices ocspServices){
        this.ocspServices = ocspServices;
    }

    @GetMapping("/request")
    public ResponseEntity<?> getRequest(@RequestBody RequestOCSP requestOCSP){
        return ResponseEntity.ok(Map.of("Ocsp_Request",ocspServices.getOcspRequest(requestOCSP.getCert())));
    }
}
