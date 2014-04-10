package gaia.repository.mongodb;

import java.util.List;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;
import org.mongodb.morphia.query.Query;
import org.junit.internal.matchers.IsCollectionContaining;
import gaia.repository.mongodb.entities.IndexedEntity;
import org.junit.Before;

public class IndexedEntityTest extends BaseTest {

    @Before
    @Override
    public void setup() throws Exception {

        super.setup();

        getMorphia().map(IndexedEntity.class);

        getDatastore().ensureIndexes();
    }

    @Test
    public void annotationIndexDefinitions_shouldBeCreated() {

        List<String> indexNames = this.getEntityIndexNames(IndexedEntity.ENTITY_NAME);

        Assert.assertEquals(4, indexNames.size());

        String[] expectedIndexNames = IndexedEntity.DEFINED_INDEX_NAMES;
        Assert.assertThat(indexNames, IsCollectionContaining.hasItems(expectedIndexNames));
    }

    @Test(expected = MongoException.class)
    public void uniqueIndex_saveWithDuplicateKey_throwMongoException() throws MongoException {

        final ObjectId id = new ObjectId();

        IndexedEntity entity = new IndexedEntity("uniqueName", "data", null, null);
        getDatastore().save(entity);

        IndexedEntity entityWithDuplicateUniqueName = new IndexedEntity("uniqueName", "data modified", null, null);
        getDatastore().save(entityWithDuplicateUniqueName);
    }

    @Test
    public void uniqueIndex_queryWithIndexField_indexScanHited() {

        IndexedEntity[] entities = {
            new IndexedEntity("uniqueName1", "data...", null, null),
            new IndexedEntity("uniqueName2", "data...", null, null),
            new IndexedEntity("uniqueName3", "data...", null, null),
            new IndexedEntity("otherName1", "data...", null, null),
            new IndexedEntity("otherName2", "data...", null, null),
            new IndexedEntity("otherName3", "data...", null, null)
        };

        getDatastore().save(entities);

        Query<IndexedEntity> query = getDatastore().createQuery(IndexedEntity.class);
        query.getQueryObject();
        query.field("uniqueName").contains("uniqueName");

        DBObject explain = getQueryExplain(query);

        Assert.assertTrue(explain.containsField("cursor"));

        String cursor = (String) explain.get("cursor");

        Assert.assertTrue(cursor.contains(IndexedEntity.UNIQUE_INDEX_NAME));
    }

    @Test
    public void compositeIndex_queryWithIndexField_indexScanHited() {

        IndexedEntity[] entities = {
            new IndexedEntity("uniqueName1", "data...", "google", "..."),
            new IndexedEntity("uniqueName2", "data...", "google", "..."),
            new IndexedEntity("uniqueName3", "data...", "google", "..."),
            new IndexedEntity("otherName1", "data...", "twitter", "..."),
            new IndexedEntity("otherName2", "data...", "twitter", "..."),
            new IndexedEntity("otherName3", "data...", "twitter", "...")
        };

        getDatastore().save(entities);

        Query<IndexedEntity> query = getDatastore().createQuery(IndexedEntity.class);
        query.getQueryObject();
        query.field("indexPart1").contains("google");

        DBObject explain = getQueryExplain(query);

        Assert.assertTrue(explain.containsField("cursor"));

        String cursor = (String) explain.get("cursor");

        Assert.assertTrue(cursor.contains(IndexedEntity.COMPOSITE_INDEX_NAME));
    }

}
