package gaia.repository.mongodb.interfaces;

import org.mongodb.morphia.query.Query;

public interface IDatastoreMongoDB {

    public <Entity extends Object, Key extends Object> Entity get(Class<Entity> entityClass, Key id);

    public <Entity extends Object> Entity save(Entity entity);

    public <Entity extends Object, Key extends Object> void delete(Class<Entity> entityClass, Key id);

    public <Entity extends Object> Query<Entity> createQuery(Class<Entity> entityClass);

}
