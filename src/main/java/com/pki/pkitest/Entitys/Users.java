package com.pki.pkitest.Entitys;

import java.time.OffsetDateTime;

import java.util.HashSet;

import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "MITCMS01_USERS")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "CD_ID", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "NB_USERNAME", unique = true, nullable = false, length = 100)
    private String username;

    @Column(name = "NB_EMAIL", unique = true, nullable = false, length = 255)
    private String email;

    @Column(name = "NB_HASHED_PASSWORD", nullable = false)
    private String password;

    @Column(name = "NB_IS_ACTIVE")
    private Boolean isActive = true;

    @Column(name = "FH_CREATED_AT", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "MITCMS01_USER_ROLES",
        joinColumns = @JoinColumn(name = "CD_USER_ID"),
        inverseJoinColumns = @JoinColumn(name = "CD_ROLE_ID")
    )
    private Set<Role> roles = new HashSet<>();
}