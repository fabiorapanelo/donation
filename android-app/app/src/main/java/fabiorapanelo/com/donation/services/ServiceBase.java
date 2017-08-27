package fabiorapanelo.com.donation.services;

/**
 * Created by fabio on 29/07/2017.
 */

public abstract class ServiceBase {

    //public static final String BASE_URL = "http://192.168.0.105:8081/donation/";
    public static final String BASE_URL = "http://ec2-54-145-27-121.compute-1.amazonaws.com:8081/donation/";

    public String getUrl(String path){
        return BASE_URL + path;
    }
}

