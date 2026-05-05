package com.pki.pkitest.Services;

import com.pki.pkitest.Utils.CertificateUtils;
import com.pki.pkitest.Utils.OcspUtils;
import org.springframework.stereotype.Service;

import java.security.cert.X509Certificate;

@Service
public class OcspServices {
        private final OcspUtils ocspUtils;
        private final CertificateUtils certificateUtils;

        public OcspServices(OcspUtils ocspUtils , CertificateUtils certificateUtils){

            this.ocspUtils = ocspUtils;
            this.certificateUtils = certificateUtils;
        }

        public String getOcspRequest(String cert){

            X509Certificate certificate = certificateUtils.convertToCer(cert);
            ocspUtils.verifyChain(certificate);
            return ocspUtils.getOcspRequest(certificate);

        }

        public String ocsp(String cert){
            X509Certificate certificate = certificateUtils.convertToCer(cert);
            return ocspUtils.verifyChain(certificate);
        }

         public String OcspResponceServiceApi(byte[] requestOcspHex){

            String ocspRes = ocspUtils.ocspResponseApi(requestOcspHex);
            return ocspRes;
        }

}
