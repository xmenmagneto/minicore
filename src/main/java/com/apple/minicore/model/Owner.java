package com.apple.minicore.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "owners")
public class Owner {

    @Id
    private int id;

    @Size(max = 20)
    @NaturalId
    private String name;

    private String email;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "owners")
    @JsonIgnore
    private Set<Component> posts = new HashSet<>();

    public Owner(int id, @Size(max = 20) String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public Owner() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Component> getPosts() {
        return posts;
    }

    public void setPosts(Set<Component> posts) {
        this.posts = posts;
    }
}
