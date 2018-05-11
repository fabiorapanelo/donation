package fabiorapanelo.com.donation.model;

import java.io.Serializable;
import java.util.List;

public class Campaign implements Serializable {

    private String id;
    private String name;
    private String userId;
    private GeoPointLocation location;
    private List<String> images;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public GeoPointLocation getLocation() {
        return location;
    }
    public void setLocation(GeoPointLocation location) {
        this.location = location;
    }

    public List<String> getImages() {
        return images;
    }
    public void setImages(List<String> images) {
        this.images = images;
    }
}
