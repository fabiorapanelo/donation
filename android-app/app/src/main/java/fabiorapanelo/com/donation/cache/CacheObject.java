package fabiorapanelo.com.donation.cache;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by fabio on 16/09/2017.
 */

public class CacheObject {

    private Object object;
    private int secondsToLive;
    private Date expiration;

    public CacheObject(Object object, int secondsToLive){
        this.object = object;
        this.secondsToLive = secondsToLive;

        if (secondsToLive != 0)
        {
            expiration = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(expiration);
            cal.add(cal.SECOND, secondsToLive);
            expiration = cal.getTime();
        }
    }

    public Object getObject() {
        return object;
    }

    public int getSecondsToLive() {
        return secondsToLive;
    }

    public boolean isExpired(){
        if (expiration == null) {
            return false;
        } else {
            if (expiration.before(new Date())) {
                return true;
            } else {
                return false;
            }
        }
    }
}
