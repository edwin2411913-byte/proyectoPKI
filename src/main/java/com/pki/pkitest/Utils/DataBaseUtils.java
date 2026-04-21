package com.pki.pkitest.Utils;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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

                                    
    
}
