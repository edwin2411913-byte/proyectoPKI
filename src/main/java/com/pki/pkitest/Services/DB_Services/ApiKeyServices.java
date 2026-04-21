package com.pki.pkitest.Services.DB_Services;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.pki.pkitest.Entitys.ApiKey;
import com.pki.pkitest.Entitys.Users;
import com.pki.pkitest.Repositoris.ApiKeyRepository;
import com.pki.pkitest.Utils.CiferUtils;

@Service
public class ApiKeyServices {

    private ApiKeyRepository apiKeyRepository;
    private CiferUtils ciferUtils;

    public ApiKeyServices (ApiKeyRepository apiKeyRepository, CiferUtils ciferUtils){
        
        this.apiKeyRepository = apiKeyRepository;
        this.ciferUtils =ciferUtils;
    }


    public Users getUserByApiKey(String StringApikey){

        String hashApikey = ciferUtils.calcularSha256(StringApikey);
        ApiKey apiKey = apiKeyRepository.findByKeyHash(hashApikey)
                        .orElseThrow(() ->  new BadCredentialsException("Apikey no valida"));               
        return apiKey.getUser(); 
    }
}
