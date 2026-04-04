package com.pki.pkitest.Services;

import org.springframework.stereotype.Service;

import com.pki.pkitest.Utils.CiferUtils;
import com.pki.pkitest.Utils.CertificateUtils;

@Service
public class ServisCifer {

    public CiferUtils ciferUtils;
    public CertificateUtils certificateUtils;

    public  ServisCifer(CiferUtils ciferUtils, CertificateUtils certificateUtils){
        this.ciferUtils = ciferUtils;
        this.certificateUtils = certificateUtils;
    }
  

    public String ciferAes(String plainText){
        return ciferUtils.ciferAES(plainText,"AESTEST");
        
    }

    public String deciferAES(String encriptData){
        return ciferUtils.decrypAES(encriptData ,"AESTEST");
    }

}
