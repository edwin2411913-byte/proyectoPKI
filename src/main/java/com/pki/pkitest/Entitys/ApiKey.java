package com.pki.pkitest.Entitys;
import java.time.OffsetDateTime;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "MITCMS02_APIKEY")
public class ApiKey {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "CD_ID")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CD_USER_ID", nullable = false)
    private Users user;

    @Column(name = "NB_NAME", length = 100)
    private String name;

    @Column(name = "NB_KEY_HASH", nullable = false)
    private String keyHash;

    @Column(name = "FH_EXPIRES_AT")
    private OffsetDateTime expiresAt;

    @Column(name = "FH_LAST_USED_AT")
    private OffsetDateTime lastUsedAt;
}

    
