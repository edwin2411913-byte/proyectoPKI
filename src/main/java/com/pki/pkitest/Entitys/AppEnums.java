package com.pki.pkitest.Entitys;

public class AppEnums {
    
    public enum CertStatus {
        VALID, REVOKED, EXPIRED
    }

    public enum CaType {
        ROOT, INTERMEDIATE
    }

    public enum RevocationReason {
        KEY_COMPROMISE, CA_COMPROMISE, AFFILIATION_CHANGED, 
        SUPERSEDED, CESSATION_OF_OPERATION, CERTIFICATE_HOLD
    }
}
