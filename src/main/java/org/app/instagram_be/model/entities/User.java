package org.app.instagram_be.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import lombok.*;
import org.app.instagram_be.model.entities.enums.UserRoleEnum;
import org.app.instagram_be.security.oauth2.AuthProvider;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User extends BaseEntity {
    @Column(nullable = false)
    private String fullName;
    @Column
    private String gender;
    @Column
    private String bio;
    @Column
    private String phoneNumber;
    @Column
    private LocalDateTime dob;
    @Column
    private String email;
    @Column
    private Integer followers;
    @Column
    private Integer following;
    @Column
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;
    private String providerId;
    @Column
    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;
    private String imageUrl;
    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private Account account;
}
