package com.pki.pkitest.Entitys;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MITCMS01_USERS")
public class Users {
    @Id
    @GeneratedValue
    @Column(name = "CD_ID", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "NB_USERNAME", unique = true, nullable = false, length = 100)
    private String username;

    @Column(name = "NB_EMAIL", unique = true, nullable = false, length = 255)
    private String email;

    @Column(name = "NB_HASHED_PASSWORD", nullable = false)
    private String password;

    @Column(name = "NB_ROLE", length = 20)
    private String role = "USER";

    @Column(name = "NB_IS_ACTIVE")
    private Boolean isActive = true;

    @Column(name = "FH_CREATED_AT", updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    // Relaciones
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApiKey> apiKeys = new ArrayList<>();

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Certificates> certificates = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<AuditLog> auditLogs = new ArrayList<>();
}


