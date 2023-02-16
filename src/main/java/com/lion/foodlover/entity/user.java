package com.lion.foodlover.entity;

import org.springframework.stereotype.Indexed;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Indexed
@Table(name = "users")
public class user implements Serializable {
    private static final long serialVersionUID = 2652327633296064143L;

    @Id
    private String username;
    private String password;

    private String firstname;
    private String lastname;

    @Column(unique = true)
    private String email;

    private String phoneNumber;
    private boolean enabled;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    private Set<Post> postList;
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
