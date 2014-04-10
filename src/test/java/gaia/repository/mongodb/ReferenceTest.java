package gaia.repository.mongodb;

import gaia.repository.mongodb.entities.ReferenceGroupEntity;
import gaia.repository.mongodb.entities.ReferenceUserEntity;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.mapping.MappingException;
import org.mongodb.morphia.query.Query;

public class ReferenceTest extends BaseTest {

    @Before
    @Override
    public void setup() throws Exception {

        super.setup();

        getMorphia().map(ReferenceUserEntity.class, ReferenceGroupEntity.class);
    }

    @Test(expected = MappingException.class)
    public void save_withNonePersistReference_throwException() throws Exception {

        final ReferenceGroupEntity group = new ReferenceGroupEntity();
        final ReferenceUserEntity user = new ReferenceUserEntity();

        group.addUser(user);
        getDatastore().save(group);
    }

    @Test
    public void query_loadReferenceButNotLazyReference() throws Exception {

        final ReferenceUserEntity user_1 = new ReferenceUserEntity("user_1");
        getDatastore().save(user_1);

        final ReferenceUserEntity lazyUser_1 = new ReferenceUserEntity("lazyUser_1");
        getDatastore().save(lazyUser_1);

        final ReferenceGroupEntity container_1 = new ReferenceGroupEntity("group_1", user_1, lazyUser_1);
        Key<ReferenceGroupEntity> key = getDatastore().save(container_1);
        ObjectId containerId = new ObjectId((String) key.getId());

        ReferenceGroupEntity container = (ReferenceGroupEntity) getDatastore().
                get(ReferenceGroupEntity.class, containerId);

        ReferenceUserEntity user = container.getUsers().get(0);
        Assert.assertNotNull(user);
        Assert.assertEquals("user_1", user.getName());

        ReferenceUserEntity lazyUser = container.getLazyUsers().get(0);
        Assert.assertNotNull(lazyUser);
        Assert.assertEquals("lazyUser_1", lazyUser.getName());
    }

    @Test
    public void query_withReference() throws Exception {

        final ReferenceUserEntity user = new ReferenceUserEntity("user_1");
        getDatastore().save(user);

        final ReferenceGroupEntity container = new ReferenceGroupEntity("group_1");
        container.addUser(user);
        getDatastore().save(container);

        final Query<ReferenceGroupEntity> query = getDatastore().createQuery(ReferenceGroupEntity.class);
        final ReferenceGroupEntity object = query.field("users").hasThisOne(user).get();

        Assert.assertNotNull(object);
    }

    @Test
    public void query_withLazyReference() throws Exception {

        final ReferenceUserEntity user = new ReferenceUserEntity();
        getDatastore().save(user);

        final ReferenceGroupEntity group = new ReferenceGroupEntity();
        group.addLazyUser(user);
        getDatastore().save(group);

        final Query<ReferenceGroupEntity> query = getDatastore().createQuery(ReferenceGroupEntity.class);
        final ReferenceGroupEntity object = query.field("lazyUsers").hasThisOne(user).get();

        Assert.assertNotNull(object);
    }

    @Test
    public void query_withMultipleReference() throws Exception {

        final ReferenceUserEntity user = new ReferenceUserEntity("user_1");
        getDatastore().save(user);

        final ReferenceGroupEntity group_1 = new ReferenceGroupEntity("group_1");
        group_1.addUser(user);
        getDatastore().save(group_1);

        final ReferenceGroupEntity group_2 = new ReferenceGroupEntity("group_2");
        group_2.addUser(user);
        getDatastore().save(group_2);

        final int expectedRecords = 2;
        final Query<ReferenceGroupEntity> query = getDatastore().createQuery(ReferenceGroupEntity.class);

        Assert.assertEquals(expectedRecords, query.field("users").hasThisOne(user).asList().size());
    }

    @Test
    public void query_withMultipleLazyReference() throws Exception {

        final ReferenceUserEntity user = new ReferenceUserEntity("user_1");
        getDatastore().save(user);

        final ReferenceGroupEntity group_1 = new ReferenceGroupEntity("group_1");
        group_1.addLazyUser(user);
        getDatastore().save(group_1);

        final ReferenceGroupEntity group_2 = new ReferenceGroupEntity("group_2");
        group_2.addLazyUser(user);
        getDatastore().save(group_2);

        final int expectedRecords = 2;
        final Query<ReferenceGroupEntity> query = getDatastore().createQuery(ReferenceGroupEntity.class);

        Assert.assertEquals(expectedRecords, query.field("lazyUsers").hasThisOne(user).asList().size());
    }

}
