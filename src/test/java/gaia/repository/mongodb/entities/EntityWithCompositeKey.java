package gaia.repository.mongodb.entities;

import com.google.common.base.Objects;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;

@Entity(value = EntityWithCompositeKey.ENTITY_NAME, noClassnameStored = true)
@Indexes({
    @Index(name = EntityWithCompositeKey.COMPOSITE_FIELD_INDEX_NAME, value = "_id.idPart1")
})
public class EntityWithCompositeKey {

    public static final String ENTITY_NAME = "entitywithembeddedkey";
    public static final String PRIMARY_KEY_INDEX_NAME = "_id_";
    public static final String COMPOSITE_FIELD_INDEX_NAME = "embeddedid_idPart1_idx";

    @Id
    private CompositeKey id;
    private String data;

    public EntityWithCompositeKey() {
    }

    public EntityWithCompositeKey(final CompositeKey myId, final String data) {
        id = myId;
        this.data = data;
    }

    public CompositeKey getId() {
        return id;
    }

    public void setId(CompositeKey id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, data);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof EntityWithCompositeKey)) {
            return false;
        }

        EntityWithCompositeKey other = (EntityWithCompositeKey) obj;
        return Objects.equal(id, other.id)
                && Objects.equal(data, other.data);
    }

}
