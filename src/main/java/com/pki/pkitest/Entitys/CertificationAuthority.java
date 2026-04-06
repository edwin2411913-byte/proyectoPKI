package com.pki.pkitest.Entitys;

import java.time.OffsetDateTime;
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

@Entity
@Table(name = "certification_authorities")
public class CertificationAuthority {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "ca_name", nullable = false)
    private String caName;

    @Enumerated(EnumType.STRING)
    private CaType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_ca_id")
    private CertificationAuthority parentCa;

    @OneToMany(mappedBy = "parentCa")
    private List<CertificationAuthority> subAuthorities;

    @Column(name = "crl_distribution_point")
    private String crlDistributionPoint;

    private Boolean isActive = true;
    private OffsetDateTime createdAt = OffsetDateTime.now();
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public String getCaName() {
        return caName;
    }
    public void setCaName(String caName) {
        this.caName = caName;
    }
    public CaType getType() {
        return type;
    }
    public void setType(CaType type) {
        this.type = type;
    }
    public CertificationAuthority getParentCa() {
        return parentCa;
    }
    public void setParentCa(CertificationAuthority parentCa) {
        this.parentCa = parentCa;
    }
    public List<CertificationAuthority> getSubAuthorities() {
        return subAuthorities;
    }
    public void setSubAuthorities(List<CertificationAuthority> subAuthorities) {
        this.subAuthorities = subAuthorities;
    }
    public String getCrlDistributionPoint() {
        return crlDistributionPoint;
    }
    public void setCrlDistributionPoint(String crlDistributionPoint) {
        this.crlDistributionPoint = crlDistributionPoint;
    }
    public Boolean getIsActive() {
        return isActive;
    }
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    
}