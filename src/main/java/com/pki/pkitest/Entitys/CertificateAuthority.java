package com.pki.pkitest.Entitys;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.pki.pkitest.Entitys.AppEnums.CaType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
    @GeneratedValue
    @Column(name = "CD_ID")
    private UUID id;

    @Column(name = "NB_CA_NAME", nullable = false, length = 255)
    private String caName;

    @Column(name = "NB_ALIAS", unique = true, length = 100)
    private String alias;

    @Enumerated(EnumType.STRING)
    @Column(name = "NB_TYPE", nullable = false)
    private CaType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CD_PARENT_CA_ID")
    private CertificateAuthority parentCa;

    @OneToMany(mappedBy = "parentCa")
    private List<CertificateAuthority> subAuthorities = new ArrayList<>();

    @Column(name = "NB_CRL_DISTRIBUTION_POINT", length = 500)
    private String crlDistributionPoint;

    @Column(name = "NB_IS_ACTIVE")
    private Boolean isActive = true;

    @Column(name = "FH_CREATED_AT", updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @OneToMany(mappedBy = "issuerCa")
    private List<Certificates> issuedCertificates = new ArrayList<>();
}