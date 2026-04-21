package com.pki.pkitest.Entitys;

import java.util.UUID;

import com.pki.pkitest.Entitys.AppEnums.CaType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "MITCMS03_CERTIFICATES_AUTORITIES")
public class CertificateAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "CD_ID")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CD_OWNER_ID", nullable = false)
    private Users owner;

    @Column(name = "NB_CA_NAME", nullable = false)
    private String caName;

    @Column(name = "NB_ALIAS", unique = true)
    private String alias;

    @Enumerated(EnumType.STRING)
    @Column(name = "NB_TYPE", nullable = false)
    private CaType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CD_PARENT_CA_ID")
    private CertificateAuthority parentCa;

    @Column(name = "CB_CERTIFICATE_PEM", nullable = false, columnDefinition = "TEXT")
    private String certificatePem;

    @Column(name = "NB_CRL_DISTRIBUTION_POINT")
    private String crlUrl;
}