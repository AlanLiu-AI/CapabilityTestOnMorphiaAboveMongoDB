package gaia.repository.mongodb;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import gaia.repository.mongodb.interfaces.IDatastoreMongoDB;
import java.net.UnknownHostException;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;

public class DatastoreMongoDB implements IDatastoreMongoDB {

    //TODO: currently we use alias records in /etc/hosts to map the database host
    private static final String MONGO_URI = "mongodb://gaia.datastore:27017/gaia";

    private final Datastore datastore;
    private final Morphia morphia;
    private final MongoClient mongoClient;
    private final DB mongoDb;

    public DatastoreMongoDB() throws UnknownHostException {
        this(MONGO_URI);
        morphia.mapPackage("gaia.repository.mongodb.entities");
    }

    DatastoreMongoDB(String mongoUri) throws UnknownHostException {
        this.morphia = new Morphia();
        MongoClientURI mongoClientUri = new MongoClientURI(mongoUri);
        this.mongoClient = new MongoClient(mongoClientUri);
        this.mongoDb = this.mongoClient.getDB(mongoClientUri.getDatabase());
        this.datastore = morphia.createDatastore(this.mongoClient, mongoClientUri.getDatabase());
    }

    @Override
    public <Entity extends Object, Key extends Object> Entity get(Class<Entity> entityClass, Key id) {
        return datastore.get(entityClass, id);
    }

    @Override
    public <Entity extends Object> Entity save(Entity entity) {
        datastore.save(entity);
        return entity;
    }

    @Override
    public <Entity extends Object, Key extends Object> void delete(Class<Entity> entityClass, Key id) {
        datastore.delete(entityClass, id);
    }

    @Override
    public <Entity extends Object> Query<Entity> createQuery(Class<Entity> entityClass) {
        return datastore.createQuery(entityClass);
    }

    Datastore getDatastore() {
        return datastore;
    }

    Morphia getMorphia() {
        return morphia;
    }

    MongoClient getMongoClient() {
        return mongoClient;
    }

    DB getMongoDb() {
        return mongoDb;
    }
}
