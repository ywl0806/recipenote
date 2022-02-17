package com.example.recipenote.entity;

import com.example.recipenote.entity.role.UserRole;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@Table(name = "user")
@NoArgsConstructor
public class User extends AbstractEntity implements UserDetails ,UserInf{

    @Builder
    public User(String username, String name, String password, UserRole role) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    @Id
    @SequenceGenerator(name = "user_id_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column
    private Long affiliateId;

    @Column(nullable = false)
    private  Boolean enabled = true;

    @OneToOne
    @JoinColumn(name = "affiliateId",insertable = false, updatable = false)
    private Affiliate affiliate;


    @Column
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.toString()));
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
