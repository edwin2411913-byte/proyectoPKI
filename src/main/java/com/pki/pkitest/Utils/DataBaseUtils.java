package com.pki.pkitest.Utils;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pki.pkitest.Entitys.CertificateAuthority;
import com.pki.pkitest.Entitys.Users;
import com.pki.pkitest.Repositoris.CaRepository;
import com.pki.pkitest.Repositoris.UserRepository;
import com.pki.pkitest.Services.DB_Services.CertificateServices;

@Service
public class DataBaseUtils {

    @Autowired 
    CertificateServices certificateServices;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CaRepository caRepository;

    public void guardarCertificado (String certPem , String noSerie, String commonName, Integer daysValid){
        CertificateAuthority caCertDB = caRepository.findByAlias("CA_JWT")
        .orElseThrow(() -> new RuntimeException("Error: No se encontró la autoridad con el alias 'CA_JWT'"));;

        UUID caId = caCertDB.getId();

        Users usertest = userRepository.findByusername("USER_TEST").orElseThrow(() -> new RuntimeException("Error: No se encontro el usuario"));; 

        certificateServices.saveCertificate(
        noSerie,
        usertest.getId(),
        caId,
        commonName,
        certPem,daysValid       
    );
                                          
    }
}
