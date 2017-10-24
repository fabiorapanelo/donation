package fabiorapanelo.com.donation.repositories;

import fabiorapanelo.com.donation.model.Credentials;
import fabiorapanelo.com.donation.model.Log;
import fabiorapanelo.com.donation.model.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryName;

/**
 * Created by fabio on 31/07/2017.
 */

public interface UserRepository {

    @POST("authentication")
    Call<User> authenticate(@Body Credentials credentials);

    @GET("users")
    Call<ResponseBody> find();

    @POST("users")
    Call<ResponseBody> create(@Body User user);

    @GET("users/{userId}")
    Call<User> find(@Path("userId") String userId);

    @PUT("users/{userId}")
    Call<ResponseBody> update(@Path("userId") String userId, @Body User user);

    @DELETE("users/{userId}")
    Call<ResponseBody> delete(@Path("userId") String userId);

    @GET("users/search/findOneByUsernameIgnoreCase")
    Call<User> findByUsername(@Query("name") String username);

    @GET("users/{userId}/logs")
    Call<ResponseBody> getLogs(@Path("userId") Long userId);

    @GET("users/{userId}/balance")
    Call<ResponseBody> getBalance(@Path("userId") Long userId);

    @POST("users/{userId}/add-ticket")
    Call<ResponseBody> addTicket(@Path("userId") Long userId, @Body Log ticket);

}
