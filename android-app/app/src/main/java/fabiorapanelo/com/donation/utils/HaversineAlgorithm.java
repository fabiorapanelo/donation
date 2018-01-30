package fabiorapanelo.com.donation.utils;

import android.location.Location;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import fabiorapanelo.com.donation.model.Campaign;
import fabiorapanelo.com.donation.model.GeoPointLocation;

/**
 * Created by fabio on 23/09/2017.
 */

public class HaversineAlgorithm {

    static final double _eQuatorialEarthRadius = 6378.1370D;
    static final double _d2r = (Math.PI / 180D);

    public static int HaversineInM(double lat1, double long1, double lat2, double long2) {
        return (int) (1000D * HaversineInKM(lat1, long1, lat2, long2));
    }

    public static double HaversineInKM(double lat1, double long1, double lat2, double long2) {
        double dlong = (long2 - long1) * _d2r;
        double dlat = (lat2 - lat1) * _d2r;
        double a = Math.pow(Math.sin(dlat / 2D), 2D) + Math.cos(lat1 * _d2r) * Math.cos(lat2 * _d2r)
                * Math.pow(Math.sin(dlong / 2D), 2D);
        double c = 2D * Math.atan2(Math.sqrt(a), Math.sqrt(1D - a));
        double d = _eQuatorialEarthRadius * c;

        return d;
    }

    public static String getDistance(GeoPointLocation geoPointLocation, Location lastLocation){
        List<Double> coordinates = geoPointLocation.getCoordinates();
        double longitude = coordinates.get(0);
        double latitude = coordinates.get(1);

        double distance = HaversineAlgorithm.HaversineInKM(lastLocation.getLatitude(),
                lastLocation.getLongitude(),
                latitude,
                longitude);

        NumberFormat formatter = new DecimalFormat("#0.00");
        if(distance >= 0.1D){
            return formatter.format(distance) + " km";
        } else {
            distance = distance * 1000;
            return formatter.format(distance) + " m";
        }

    }
}