package com.pki.pkitest.Entitys;

import java.time.OffsetDateTime;

import com.pki.pkitest.Entitys.AppEnums.CertStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MITCMS04_CERTIFICATES")
public class Certificates {
  @Id
    @Column(name = "CD_SERIAL_NUMBER", length = 128)
    private String serialNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CD_OWNER_ID", nullable = false)
    private Users owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CD_ISSUER_CA_ID", nullable = false)
    private CertificateAuthority issuer;

    @Column(name = "NB_COMMON_NAME", nullable = false)
    private String commonName;

    @Column(name = "CB_CERTIFICATE_PEM", nullable = false, columnDefinition = "TEXT")
    private String certificatePem;

    @Column(name = "FH_NOT_BEFORE", nullable = false)
    private OffsetDateTime notBefore;

    @Column(name = "FH_NOT_AFTER", nullable = false)
    private OffsetDateTime notAfter;

    @Enumerated(EnumType.STRING)
    @Column(name = "NB_STATUS")
    private CertStatus status;
}
