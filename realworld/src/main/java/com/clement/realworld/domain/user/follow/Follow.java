package com.clement.realworld.domain.user.follow;

import com.clement.realworld.domain.common.BaseEntity;
import com.clement.realworld.domain.user.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "unique_follow_pair", columnNames = {})
})
public class Follow extends BaseEntity {
    
    @ManyToOne
    @JoinColumn(name = "followee_id", nullable = false)
    private User followee;

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;
    
}
