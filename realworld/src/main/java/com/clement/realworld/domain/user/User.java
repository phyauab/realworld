package com.clement.realworld.domain.user;

import com.clement.realworld.domain.common.BaseEntity;
import com.clement.realworld.domain.user.role.Role;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @NotBlank
    @Size(max = 20)
    @Column(unique = true)
    private String username;

    @NotBlank
    @Size(max = 100)
    @Column(unique = true)
    @Email
    private String email;

    @NotBlank
    @Size(max = 150) // it is hashed, so needs to be long
    private String password;

    @ManyToMany
    private Set<Role> roles = new HashSet<>();

    private String bio;

    private String image;

}
