package gaia.repository.mongodb.entities;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.CappedAt;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity(cap = @CappedAt(count = 1))
public class CappedEntity {

    @Id
    private ObjectId id;
    private String data;

    private CappedEntity() {
    }

    public CappedEntity(final String data) {
        this.data = data;
    }

    public ObjectId getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
