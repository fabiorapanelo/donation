package fabiorapanelo.com.donation.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fabio on 23/09/2017.
 */

public class GeoPointLocation {

    public String type = "Point";
    public List<Double> coordinates = new ArrayList<>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }
}
