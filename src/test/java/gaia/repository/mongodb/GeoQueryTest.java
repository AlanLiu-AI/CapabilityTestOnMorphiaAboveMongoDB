package gaia.repository.mongodb;

import gaia.repository.mongodb.entities.GeoPosition;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.query.Shape;

public class GeoQueryTest extends BaseTest {

    @Before
    @Override
    public void setup() throws Exception {

        super.setup();

        getMorphia().map(GeoPosition.class);

        getDatastore().ensureIndexes();

        mongoServerIsAtLeastVersion(2.4);
    }

    @Test
    public void query_withNear_foundPosition() throws Exception {
        final GeoPosition geoPosition = new GeoPosition("position-1", 1, 1);
        getDatastore().save(geoPosition);

        final GeoPosition found = getDatastore().find(GeoPosition.class)
                .field("location")
                .near(0, 0)
                .get();
        Assert.assertNotNull(found);
    }

    @Test
    public void query_withNearRadius_foundPosition() throws Exception {
        final GeoPosition geoPosition = new GeoPosition("position-1", 1, 1);
        getDatastore().save(geoPosition);

        final GeoPosition found = getDatastore().find(GeoPosition.class)
                .field("location")
                .near(0, 0, 1.5)
                .get();
        Assert.assertNotNull(found);
    }

    @Test
    public void query_withNearMaxDistance_notFoundPosition() throws Exception {
        final GeoPosition geoPosition = new GeoPosition("position-1", 1, 1);
        getDatastore().save(geoPosition);

        final GeoPosition notFound = getDatastore().find(GeoPosition.class)
                .field("location")
                .near(0, 0, 1)
                .get();
        Assert.assertNull(notFound);
    }

    @Test
    public void query_withinCenterWithRadius_foundPosition() throws Exception {
        final GeoPosition geoPosition = new GeoPosition("position-1", 1, 1);
        getDatastore().save(geoPosition);

        Shape centerWithRadius = Shape.center(new Shape.Point(0, 1), 1.1);
        final GeoPosition found = getDatastore().find(GeoPosition.class)
                .field("location")
                .within(centerWithRadius)
                .get();
        Assert.assertNotNull(found);
    }

    @Test
    public void query_withinBox_foundPosition() throws Exception {
        final GeoPosition geoPosition = new GeoPosition("position-1", 1, 1);
        getDatastore().save(geoPosition);

        Shape box = Shape.box(new Shape.Point(0, 0), new Shape.Point(2, 2));
        final GeoPosition found = getDatastore().find(GeoPosition.class)
                .field("location")
                .within(box)
                .get();
        Assert.assertNotNull(found);
    }

    @Test
    public void query_withOutsideBox_notFoundPosition() throws Exception {
        final GeoPosition geoPosition = new GeoPosition("position-1", 1, 1);
        getDatastore().save(geoPosition);

        Shape box = Shape.box(new Shape.Point(0, 0), new Shape.Point(.5, .5));
        final GeoPosition found = getDatastore().find(GeoPosition.class)
                .field("location")
                .within(box)
                .get();
        Assert.assertNull(found);
    }

    @Test
    public void query_withinPolygon_foundPosition() throws Exception {

        final GeoPosition geoPosition = new GeoPosition("position-1", 1, 1);
        getDatastore().save(geoPosition);

        Shape polygon = Shape.polygon(
                new Shape.Point(0, 0),
                new Shape.Point(0, 1),
                new Shape.Point(1, 1),
                new Shape.Point(1, 0)
        );
        final GeoPosition found = getDatastore().find(GeoPosition.class)
                .field("location")
                .within(polygon)
                .get();
        Assert.assertNotNull(found);
    }

    @Test
    public void query_withinOutsidePolygon_notFoundPosition() throws Exception {

        final GeoPosition geoPosition = new GeoPosition("position-1", 1, 1);
        getDatastore().save(geoPosition);

        Shape polygon = Shape.polygon(
                new Shape.Point(0, 0),
                new Shape.Point(0, .5),
                new Shape.Point(.5, .5),
                new Shape.Point(.5, 0)
        );
        final GeoPosition found = getDatastore().find(GeoPosition.class)
                .field("location")
                .within(polygon)
                .get();
        Assert.assertNull(found);
    }
}
