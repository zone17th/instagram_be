package org.app.instagram_be.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.app.instagram_be.security.oauth2Config.user.enums.AuthProvider;

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
    private String imageUrl;
    @Column
    @NotNull
    private boolean emailVerified = false;
    @OneToOne
    @JsonIgnore
    private Account account;
}
