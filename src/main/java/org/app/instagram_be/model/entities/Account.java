package org.app.instagram_be.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.app.instagram_be.model.entities.enums.UserRoleEnum;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account extends BaseEntity {

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String password;



    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    @ToString.Exclude
    private User user;
}
