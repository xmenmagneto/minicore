package com.apple.minicore.model;


import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "components")
public class Component {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Size(max = 30)
    private String name;

    /**
     * Default value of level is "0"
     */
    private String level = "0";


    /**
     * Default value for parentId is -1
     */
    private int parentId = -1;


    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE,
                    CascadeType.REMOVE
            })
    @JoinTable(name = "component_owners",
            joinColumns = { @JoinColumn(name = "component_id") },
            inverseJoinColumns = { @JoinColumn(name = "owner_id") })
    private Set<Owner> owners = new HashSet<>();


    Component() {}



    public Component(@Size(max = 30) String name, String level, int parentId) {
        this.name = name;
        this.level = level;
        this.parentId = parentId;
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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public Set<Owner> getOwners() {
        return owners;
    }

    public void setOwners(Set<Owner> owners) {
        this.owners = owners;
    }
}
