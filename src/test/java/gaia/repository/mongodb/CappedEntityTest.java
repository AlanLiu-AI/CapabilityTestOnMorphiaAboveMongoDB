package gaia.repository.mongodb;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import gaia.repository.mongodb.entities.CappedEntity;

public class CappedEntityTest extends BaseTest {

    @Before
    @Override
    public void setup() throws Exception {

        super.setup();

        getMorphia().map(CappedEntity.class);

        getDatastore().ensureCaps();
    }

    @Test
    public void cappedEntity_multipleSave_onlyPersistLast() throws Exception {
        final CappedEntity cs = new CappedEntity("First save");
        getDatastore().save(cs);

        Assert.assertEquals(1, getDatastore().getCount(CappedEntity.class));

        getDatastore().save(new CappedEntity("Second save"));

        Assert.assertEquals(1, getDatastore().getCount(CappedEntity.class));
        Assert.assertTrue(getDatastore().find(CappedEntity.class).limit(1).get().getData().equals("Second save"));

        getDatastore().save(new CappedEntity("Third save"));

        Assert.assertEquals(1, getDatastore().getCount(CappedEntity.class));

        getDatastore().save(new CappedEntity("Fourth save"));

        Assert.assertEquals(1, getDatastore().getCount(CappedEntity.class));

        getDatastore().save(new CappedEntity("Fifth save"));

        Assert.assertEquals(1, getDatastore().getCount(CappedEntity.class));
        Assert.assertTrue(getDatastore().find(CappedEntity.class).limit(1).get().getData().equals("Fifth save"));
    }

}
