package gaia.repository.mongodb;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.mapping.MappedClass;
import org.mongodb.morphia.query.QueryImpl;
import org.mongodb.morphia.query.QueryResults;

public class BaseTest {

    private static final String MONGO_URI = "mongodb://localhost:27017/gaia_morphia_test";

    private DatastoreMongoDB datastoreMongoDb;

    @Before
    public void setup() throws Exception {

        datastoreMongoDb = new DatastoreMongoDB(MONGO_URI);
    }

    @After
    public void cleanup() throws Exception {

        for (final MappedClass mc : datastoreMongoDb.getMorphia().getMapper().getMappedClasses()) {
            datastoreMongoDb.getMongoDb().getCollection(mc.getCollectionName()).drop();
        }

        datastoreMongoDb.getMongoClient().close();
    }

    public Morphia getMorphia() {

        return datastoreMongoDb.getMorphia();
    }

    public DB getMongoDb() {

        return datastoreMongoDb.getMongoDb();
    }

    public Datastore getDatastore() {

        return datastoreMongoDb.getDatastore();
    }

    public MongoClient getMongoClient() {

        return datastoreMongoDb.getMongoClient();
    }

    public double getMongoVersion() {
        String serverVersion = (String) getMongoClient().getDB("admin").command("serverStatus").get("version");
        return Double.parseDouble(serverVersion.substring(0, 3));
    }

    protected void mongoServerIsAtLeastVersion(final double version) {
        Assume.assumeTrue(getMongoVersion() >= version);
    }

    public List<String> getEntityIndexNames(String entityName) {

        final DBCollection dbCollection = getMongoDb().getCollection(entityName);
        final List<DBObject> indexes = dbCollection.getIndexInfo();

        List<String> indexNames = new ArrayList<>();
        for (DBObject index : indexes) {
            indexNames.add((String) index.get("name"));
        }

        return indexNames;
    }

    public <QueryEntityClass> DBObject getQueryExplain(QueryResults<QueryEntityClass> query) {

        QueryImpl<QueryEntityClass> queryImpl = (QueryImpl<QueryEntityClass>) query;

        DBCursor dbCursor = queryImpl.prepareCursor();
        DBObject explain = dbCursor.explain();

        return explain;
    }
}
