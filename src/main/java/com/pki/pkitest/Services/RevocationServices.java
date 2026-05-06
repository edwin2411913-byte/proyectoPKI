package com.pki.pkitest.Services;

import com.pki.pkitest.Entitys.Users;
import com.pki.pkitest.Repositoris.UserRepository;
import com.pki.pkitest.Utils.CertificateUtils;
import com.pki.pkitest.Utils.RevocationUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class RevocationServices {

    private final CertificateUtils certificateUtils;
    private  final UserRepository userRepository;
    private final RevocationUtils revocationUtils;

    public RevocationServices(CertificateUtils certificateUtils, UserRepository userRepository, RevocationUtils revocationUtils)
    {
        this.certificateUtils = certificateUtils;
        this.userRepository = userRepository;
        this.revocationUtils = revocationUtils;
    }
    public String revocateCer(String cert, String userName){
        Users usuario = userRepository.findByusername(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User; " + userName + "No encontrado"));
        revocationUtils.revokeCert(certificateUtils.convertToCer(cert).getSerialNumber(), usuario.getId());
        return "ok";
    }
}
