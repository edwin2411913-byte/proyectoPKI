package com.pki.pkitest.Controllers;

import com.pki.pkitest.Models.OcspRequestApi;
import com.pki.pkitest.Models.RequestOCSP;
import com.pki.pkitest.Services.OcspServices;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HexFormat;
import java.util.Map;

@Controller
@RequestMapping("/ocsp")
public class OcspController {

    private final OcspServices ocspServices;

    public OcspController(OcspServices ocspServices){
        this.ocspServices = ocspServices;
    }

    @PostMapping("/request")
    public ResponseEntity<?> getRequest(@RequestBody RequestOCSP requestOCSP){
        return ResponseEntity.ok(Map.of("Ocsp_Request",ocspServices.getOcspRequest(requestOCSP.getCert())));
    }

    @PostMapping()
    public ResponseEntity<?> ocsp(@RequestBody OcspRequestApi ocspRequestApi){
        byte[] bytes = HexFormat.of().parseHex(ocspRequestApi.getRequest());
        String ocspResp = ocspServices.OcspResponceServiceApi(bytes);
        return ResponseEntity.ok(Map.of("OcspRespones",ocspResp));
    }

    @PostMapping(value = "/ocsp",
            consumes =  "application/ocsp-request",
            produces =  "application/ocsp-response")
    public ResponseEntity<?> ocsp(@RequestBody byte[] ocspRequest){

        String ocspRespHex = ocspServices.OcspResponceServiceApi(ocspRequest);
        byte[] responseBytes = HexFormat.of().parseHex(ocspRespHex);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/ocsp-response")).body(responseBytes);
    }
}
