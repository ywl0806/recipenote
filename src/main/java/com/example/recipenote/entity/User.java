package com.example.recipenote.entity;

import com.example.recipenote.entity.role.UserRole;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
@Table(name = "user")
@NoArgsConstructor
public class User extends AbstractEntity implements UserDetails ,UserInf{

    @Builder
    public User(String username, String name, String password) {
        this.username = username;
        this.name = name;
        this.password = password;
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

    @Column
    private String avatarUrl;

    @Column(nullable = false)
    private  Boolean enabled = true;

    @OneToOne
    @JoinColumn(name = "affiliateId",insertable = false, updatable = false)
    private Affiliate affiliate;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role>roles = new HashSet<>();

    public void addRole(Role role){
       this.roles.add(role);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

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
