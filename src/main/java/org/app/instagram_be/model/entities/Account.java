package org.app.instagram_be.model.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.app.instagram_be.model.entities.enums.UserRoleEnum;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Data
public class Account extends BaseEntity {

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column
    private UserRoleEnum role;

    @OneToOne(mappedBy = "account")
    private User user;
}
