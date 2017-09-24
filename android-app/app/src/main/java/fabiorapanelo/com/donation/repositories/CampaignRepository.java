package fabiorapanelo.com.donation.repositories;

import fabiorapanelo.com.donation.model.Campaign;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by fabio on 07/08/2017.
 */

public interface CampaignRepository {

    @POST("campaigns")
    Call<ResponseBody> create(@Body Campaign campaign);

    @GET("campaigns/near-location")
    Call<ResponseBody> nearLocation(@Query("lat") double latitude,
                                            @Query("long") double longitude,
                                            @Query("distance") double distanceInMeters);

    @GET("campaigns/search-by-user/{userId}")
    Call<ResponseBody> searchByUser(@Path("userId") String userId);


    //Below are not implemented yet

    @Deprecated
    @GET("campaigns/{campaignId}")
    Call<Campaign> find(@Path("campaignId") String campaignId);

    @Deprecated
    @POST("campaigns/{campaignId}")
    Call<ResponseBody> update(@Path("campaignId") String campaignId, @Body Campaign campaign);

    @Deprecated
    @DELETE("campaigns/{campaignId}")
    Call<ResponseBody> delete(@Path("campaignId") String campaignId);
}
