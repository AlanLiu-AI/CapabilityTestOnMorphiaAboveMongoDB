package gaia.repository.mongodb.entities;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;

@Entity(value = IndexedEntity.ENTITY_NAME, noClassnameStored = true)
@Indexes({
    @Index(name = IndexedEntity.UNIQUE_INDEX_NAME, value = "uniqueName", unique = true),
    @Index(name = IndexedEntity.NONE_UNIQUE_INDEX_NAME, value = "data", unique = false),
    @Index(name = IndexedEntity.COMPOSITE_INDEX_NAME, value = "indexPart1, indexPart2")
})
public class IndexedEntity {

    public static final String ENTITY_NAME = "indexedentities";
    public static final String PRIMARY_KEY_INDEX_NAME = "_id_";
    public static final String UNIQUE_INDEX_NAME = "uniqueNameIndex";
    public static final String NONE_UNIQUE_INDEX_NAME = "dataIndex";
    public static final String COMPOSITE_INDEX_NAME = "indexWithMultiFields";

    public static final String[] DEFINED_INDEX_NAMES = new String[]{PRIMARY_KEY_INDEX_NAME, UNIQUE_INDEX_NAME, NONE_UNIQUE_INDEX_NAME, COMPOSITE_INDEX_NAME};

    @Id
    private ObjectId id;
    private String uniqueName;
    private String data;
    private String indexPart1;
    private String indexPart2;

    public IndexedEntity() {
    }

    public IndexedEntity(String name, String data, String part1, String part2) {
        this.uniqueName = name;
        this.data = data;
        this.indexPart1 = part1;
        this.indexPart2 = part2;
    }

    public IndexedEntity(ObjectId id, String name, String data, String part1, String part2) {
        this(name, data, part1, part2);
        this.id = id;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getIndexPart1() {
        return indexPart1;
    }

    public void setIndexPart1(String indexPart1) {
        this.indexPart1 = indexPart1;
    }

    public String getIndexPart2() {
        return indexPart2;
    }

    public void setIndexPart2(String indexPart2) {
        this.indexPart2 = indexPart2;
    }

}
