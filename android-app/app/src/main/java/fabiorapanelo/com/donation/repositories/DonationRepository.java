package fabiorapanelo.com.donation.repositories;

import fabiorapanelo.com.donation.model.Log;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by fabio on 23/10/2017.
 */

public interface DonationRepository {

    @POST("donate")
    Call<ResponseBody> create(@Body Log donation);
}
