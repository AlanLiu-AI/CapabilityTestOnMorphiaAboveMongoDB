package gaia.repository.mongodb;

import com.mongodb.DBObject;
import gaia.repository.mongodb.entities.CompositeKey;
import gaia.repository.mongodb.entities.EntityWithCompositeKey;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.query.Query;

public class EntityWithCompositeKeyTest extends BaseTest {

    @Before
    @Override
    public void setup() throws Exception {

        super.setup();

        getMorphia().map(CompositeKey.class).map(EntityWithCompositeKey.class);

        getDatastore().ensureIndexes();
    }

    @Test
    public void entityWithEmbeddedId_save_succeeded() {

        final CompositeKey embeddedId = new CompositeKey("idPart1", "idPart2");
        final EntityWithCompositeKey entity = new EntityWithCompositeKey(embeddedId, "data...");

        getDatastore().save(entity);
    }

    @Test
    public void entityWithEmbeddedId_queryByKey() {

        final CompositeKey embeddedId = new CompositeKey("idPart1", "idPart2");
        final EntityWithCompositeKey entity = new EntityWithCompositeKey(embeddedId, "data...");

        getDatastore().save(entity);

        EntityWithCompositeKey retrievedEntity = getDatastore().get(EntityWithCompositeKey.class, embeddedId);

        Assert.assertEquals(entity, retrievedEntity);
    }

    @Test
    public void entityWithEmbeddedId_queryByIdField_indexOnEmbeddedFieldScanHit() {

        EntityWithCompositeKey[] entities = new EntityWithCompositeKey[]{
            new EntityWithCompositeKey(new CompositeKey("google", "idPart2-1"), "data..."),
            new EntityWithCompositeKey(new CompositeKey("google", "idPart2-2"), "data..."),
            new EntityWithCompositeKey(new CompositeKey("twitter", "idPart2-1"), "data..."),
            new EntityWithCompositeKey(new CompositeKey("twitter", "idPart2-2"), "data...")
        };

        getDatastore().save(entities);

        Query<EntityWithCompositeKey> query = getDatastore().createQuery(EntityWithCompositeKey.class);
        query.getQueryObject();
        query.field("_id.idPart1").equal("google");

        List<EntityWithCompositeKey> records = query.asList();
        int expectedRecords = 2;
        Assert.assertEquals(expectedRecords, records.size());

        DBObject explain = getQueryExplain(query);

        Assert.assertTrue(explain.containsField("cursor"));

        String cursor = (String) explain.get("cursor");
        Assert.assertTrue(cursor.contains(EntityWithCompositeKey.COMPOSITE_FIELD_INDEX_NAME));
    }

}
