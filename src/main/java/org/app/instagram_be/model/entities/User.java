package org.app.instagram_be.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.*;

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
    @OneToOne
    @JsonIgnore
    private Account account;
}
