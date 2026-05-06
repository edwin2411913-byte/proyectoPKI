package com.pki.pkitest.Utils;

import com.pki.pkitest.Entitys.AppEnums;
import com.pki.pkitest.Repositoris.RevocationRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.UUID;

@Service
public class RevocationUtils {

    private final RevocationRepository revocationRepository;

    public RevocationUtils(RevocationRepository revocationRepository){
        this.revocationRepository = revocationRepository;
    }

    public String revokeCert(BigInteger serialNumber, UUID idUser) {
       revocationRepository.revokeCertificate(
                serialNumber.toString(), AppEnums.RevocationReason.KEY_COMPROMISE.name(), idUser, "127.0.0.1"
        );
        return "ok";
    }
}
