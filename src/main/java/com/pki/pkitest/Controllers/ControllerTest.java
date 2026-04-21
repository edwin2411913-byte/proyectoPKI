package com.pki.pkitest.Controllers;

import org.springframework.web.bind.annotation.RestController;

import com.pki.pkitest.Utils.CiferUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;



@RestController
public class ControllerTest {



    @Autowired
    private CiferUtils ciferUtils;

    @GetMapping("/TestApikey")
    public ResponseEntity<?> testApikey(@RequestHeader("X-API-KEY") String apikey) {

        System.out.println(ciferUtils.calcularSha256(apikey));
        return ResponseEntity.ok("ok");
    }
    
}
