package com.group2.restaurantorderingwebapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@Builder
public class User extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(columnDefinition = "TEXT" )
    private String imageUrl;
    @Column(columnDefinition = "varchar(120) collate 'utf8_bin'" )
    private String firstName;
    @Column(columnDefinition = "varchar(120) collate 'utf8_bin'" )
    private String lastName;
    @Column(nullable = false,unique = true)
    private String phoneNumber;
    @Column(nullable = false)
    private String email;
    @Column(columnDefinition = "varchar(120) collate 'utf8_bin'" ,nullable = false)
    private String password;
    @Column(columnDefinition = "varchar(120) collate 'utf8_bin'" )
    private String address;
    private String gender;
    private LocalDate Dob;
    private Integer otp;
    @Builder.Default
    @Column(columnDefinition = "TINYINT")
    private boolean status = false;

    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @JsonIgnore
    @OneToMany (mappedBy = "user", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<Order> orders;

    @JsonIgnore
    @OneToMany (mappedBy = "user", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<Reservation> reservations;


    @JsonIgnore
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<Ranking> rankings;

    @JsonIgnore
    @OneToMany (mappedBy = "user", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<Payment> payments;

    @JsonIgnore
    @OneToMany (mappedBy = "user", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<Favorite> favorites;



    @Override
    public String getUsername() {
        return this.phoneNumber != null ? this.phoneNumber : this.email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> role= new ArrayList<>();
        for (Role x: roles){
            role.add(new SimpleGrantedAuthority(x.getRoleName()));
        }
        return role;
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
