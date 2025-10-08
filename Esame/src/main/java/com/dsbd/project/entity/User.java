package com.dsbd.project.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull(message = "Il Nome non puo essere vuoto!")
    private String name;

    @NotNull(message = "L'Email non puo essere vuota!")
    @Column(unique = true)
    private String email;

    @NotNull(message = "La Password non puo essere vuota!")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull(message = "Il Credito non puo essere vuoto!")
    private Double credit = 0.0;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    public Integer getId() {
        return id;
    }
    public User setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }
    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }
    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }
    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public List<String> getRoles() {
        return roles;
    }
    public User setRoles(List<String> roles) {
        this.roles = roles;
        return this;
    }

    public Double getCredit() {
        return credit;
    }
    public User setCredit(Double credit) {
        this.credit = credit;
        return this;
    }

}
