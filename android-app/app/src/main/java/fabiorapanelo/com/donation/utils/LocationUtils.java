package fabiorapanelo.com.donation.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by fabio on 30/09/2017.
 */

public class LocationUtils {

    public static String getLocationName(Context context, double longitude, double latitude){
        try {
            Geocoder gcd = new Geocoder(context, Locale.getDefault());

            List<Address> addresses = gcd.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                return addresses.get(0).getLocality();
            }

        } catch (IOException e) {}

        return "";
    }
}
