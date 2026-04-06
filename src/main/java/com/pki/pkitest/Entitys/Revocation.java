package com.pki.pkitest.Entitys;

import java.time.OffsetDateTime;

import com.pki.pkitest.Entitys.AppEnums.RevocationReason;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "revocations")
public class Revocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "serial_number", referencedColumnName = "serial_number", nullable = false)
    private Certificate certificate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RevocationReason reason;

    private OffsetDateTime revokedAt = OffsetDateTime.now();
    private OffsetDateTime invalidityDate;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Certificate getCertificate() {
        return certificate;
    }
    public void setCertificate(Certificate certificate) {
        this.certificate = certificate;
    }
    public RevocationReason getReason() {
        return reason;
    }
    public void setReason(RevocationReason reason) {
        this.reason = reason;
    }
    public OffsetDateTime getRevokedAt() {
        return revokedAt;
    }
    public void setRevokedAt(OffsetDateTime revokedAt) {
        this.revokedAt = revokedAt;
    }
    public OffsetDateTime getInvalidityDate() {
        return invalidityDate;
    }
    public void setInvalidityDate(OffsetDateTime invalidityDate) {
        this.invalidityDate = invalidityDate;
    }
}
