package com.pki.pkitest.Entitys;

import java.time.OffsetDateTime;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "MITCMS06_AUDIT_LOGS")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CD_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CD_USER_ID")
    private Users user;

    @Column(name = "NB_ACTION", nullable = false, length = 100)
    private String action;

    // Mapeo simple de JSONB como String. 
    // Para manipulación avanzada se recomienda JsonNode de Jackson.
    @Column(name = "NB_DETAILS", columnDefinition = "jsonb")
    private String details;

    @Column(name = "NB_IP_ADDRESS", length = 45)
    private String ipAddress;

    @Column(name = "FH_TIMESTAMP", insertable = false, updatable = false)
    private OffsetDateTime timestamp;
}
