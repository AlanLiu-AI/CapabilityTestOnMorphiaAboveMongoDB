package gaia.repository.mongodb.entities;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

@Entity
public class ReferenceUserEntity {

    @Id
    private ObjectId id;
    private String name;

    @Reference
    private ReferenceGroupEntity group;

    public ReferenceUserEntity() {
    }

    public ReferenceUserEntity(final String name) {
        this.name = name;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(final ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public ReferenceGroupEntity getGroup() {
        return group;
    }

    public void setGroup(ReferenceGroupEntity group) {
        this.group = group;
    }

}
