package fabiorapanelo.com.donation.services;

/**
 * Created by fabio on 29/07/2017.
 */

public interface ServiceListener {
    public void onSuccess(Object object);

    public void onError(Throwable t);
}

