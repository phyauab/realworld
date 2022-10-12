package com.clement.realworld.domain.user.role;

import com.clement.realworld.domain.common.BaseEntity;
import com.clement.realworld.domain.user.role.ERole;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Role extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;

}
