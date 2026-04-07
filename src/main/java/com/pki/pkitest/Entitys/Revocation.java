package com.pki.pkitest.Entitys;

import java.time.OffsetDateTime;

import com.pki.pkitest.Entitys.AppEnums.RevocationReason;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MITCMS05_REVOCATIONS")
public class Revocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CD_ID")
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CD_SERIAL_NUMBER", nullable = false, referencedColumnName = "CD_SERIAL_NUMBER")
    private Certificates certificate;

    @Enumerated(EnumType.STRING)
    @Column(name = "NB_REASON", nullable = false)
    private RevocationReason reason;

    @Column(name = "FH_REVOKED_AT", updatable = false)
    private OffsetDateTime revokedAt = OffsetDateTime.now();

    @Column(name = "FH_INVALIDITY_DATE")
    private OffsetDateTime invalidityDate;
}
