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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "certificates")
public class Certificate {
    @Id
    @Column(name = "serial_number", length = 128)
    private String serialNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entity_id", nullable = false)
    private EntityProfile entity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issuer_ca_id", nullable = false)
    private CertificationAuthority issuerCa;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String publicKey;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String certificatePem;

    private OffsetDateTime notBefore;
    private OffsetDateTime notAfter;

    @Enumerated(EnumType.STRING)
    private CertStatus status = CertStatus.VALID;

    @Column(columnDefinition = "TEXT")
    private String csr;

    private OffsetDateTime createdAt = OffsetDateTime.now();

    // Relación 1 a 1 con Revocación (opcional)
    @OneToOne(mappedBy = "certificate")
    private Revocation revocation;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public EntityProfile getEntity() {
        return entity;
    }

    public void setEntity(EntityProfile entity) {
        this.entity = entity;
    }

    public CertificationAuthority getIssuerCa() {
        return issuerCa;
    }

    public void setIssuerCa(CertificationAuthority issuerCa) {
        this.issuerCa = issuerCa;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getCertificatePem() {
        return certificatePem;
    }

    public void setCertificatePem(String certificatePem) {
        this.certificatePem = certificatePem;
    }

    public OffsetDateTime getNotBefore() {
        return notBefore;
    }

    public void setNotBefore(OffsetDateTime notBefore) {
        this.notBefore = notBefore;
    }

    public OffsetDateTime getNotAfter() {
        return notAfter;
    }

    public void setNotAfter(OffsetDateTime notAfter) {
        this.notAfter = notAfter;
    }

    public CertStatus getStatus() {
        return status;
    }

    public void setStatus(CertStatus status) {
        this.status = status;
    }

    public String getCsr() {
        return csr;
    }

    public void setCsr(String csr) {
        this.csr = csr;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Revocation getRevocation() {
        return revocation;
    }

    public void setRevocation(Revocation revocation) {
        this.revocation = revocation;
    }

}