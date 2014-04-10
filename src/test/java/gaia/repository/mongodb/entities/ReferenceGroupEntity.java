package gaia.repository.mongodb.entities;

import java.util.ArrayList;
import java.util.List;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

@Entity
public class ReferenceGroupEntity {

    @Id
    private String id;
    private String name;
    @Reference
    private List<ReferenceUserEntity> users = new ArrayList<>();
    @Reference(lazy = true)
    private List<ReferenceUserEntity> lazyUsers = new ArrayList<>();

    public ReferenceGroupEntity() {

    }

    public ReferenceGroupEntity(String name) {
        this.name = name;
    }

    public ReferenceGroupEntity(String name, ReferenceUserEntity user, ReferenceUserEntity lazyUser) {
        this.name = name;
        this.users.add(user);
        this.lazyUsers.add(lazyUser);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ReferenceUserEntity> getUsers() {
        return users;
    }

    public void setUsers(List<ReferenceUserEntity> users) {
        this.users = users;
    }

    public void addUser(ReferenceUserEntity user) {
        this.users.add(user);
    }

    public List<ReferenceUserEntity> getLazyUsers() {
        return lazyUsers;
    }

    public void setLazyUsers(List<ReferenceUserEntity> lazyUsers) {
        this.lazyUsers = lazyUsers;
    }

    public void addLazyUser(ReferenceUserEntity user) {
        this.lazyUsers.add(user);
    }
}
