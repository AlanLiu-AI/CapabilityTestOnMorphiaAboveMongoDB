package gaia.repository.mongodb.entities;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.utils.IndexDirection;

@Entity
public class GeoPosition {

    @Id
    private ObjectId id;
    private String name;
    @Indexed(IndexDirection.GEO2D)
    private double[] location;

    public GeoPosition(final String name, final double[] location) {
        this.name = name;
        this.location = location;
    }

    public GeoPosition(final String name, final double longitude, final double latitude) {
        this.name = name;
        this.location = new double[]{longitude, latitude};
    }

    private GeoPosition() {
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double[] getLocation() {
        return location;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }

}
