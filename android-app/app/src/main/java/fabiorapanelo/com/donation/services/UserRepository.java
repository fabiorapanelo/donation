package fabiorapanelo.com.donation.services;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by fabio on 31/07/2017.
 */

public interface UserRepository {

    @GET("users")
    Call<ResponseBody> find();

    @GET("users/{userId}")
    Call<ResponseBody> find(@Path("userId") String userId);

    @POST("users/{userId}")
    Call<ResponseBody> create(@Path("userId") String userId);

    @POST("users/{userId}")
    Call<ResponseBody> update(@Path("userId") String userId);

    @DELETE("users/{userId}")
    Call<ResponseBody> delete(@Path("userId") String userId);

}
