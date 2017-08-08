package fabiorapanelo.com.donation.repositories;

import fabiorapanelo.com.donation.model.Campaign;
import fabiorapanelo.com.donation.model.Credentials;
import fabiorapanelo.com.donation.model.User;
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

    @GET("campaigns")
    Call<ResponseBody> find();

    @POST("campaigns")
    Call<ResponseBody> create(@Body Campaign campaign);

    @GET("campaigns/{campaignId}")
    Call<Campaign> find(@Path("campaignId") String campaignId);

    @POST("campaigns/{campaignId}")
    Call<ResponseBody> update(@Path("campaignId") String campaignId, @Body Campaign campaign);

    @DELETE("campaigns/{campaignId}")
    Call<ResponseBody> delete(@Path("campaignId") String campaignId);

}
