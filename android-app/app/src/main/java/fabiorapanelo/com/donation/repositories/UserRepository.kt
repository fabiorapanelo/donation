package fabiorapanelo.com.donation.repositories

import fabiorapanelo.com.donation.model.Credentials
import fabiorapanelo.com.donation.model.Log
import fabiorapanelo.com.donation.model.User
import fabiorapanelo.com.donation.model.UserInfo
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryName

/**
 * Created by fabio on 31/07/2017.
 */

interface UserRepository {

    @POST("authentication")
    fun authenticate(@Body credentials: Credentials): Call<User>

    @GET("users")
    fun find(): Call<ResponseBody>

    @POST("users")
    fun create(@Body user: User): Call<ResponseBody>

    @GET("users/{userId}")
    fun find(@Path("userId") userId: String): Call<User>

    @POST("users/{userId}")
    fun update(@Path("userId") userId: String, @Body user: User): Call<ResponseBody>

    @DELETE("users/{userId}")
    fun delete(@Path("userId") userId: String): Call<ResponseBody>

    @GET("users/search/findOneByUsernameIgnoreCase")
    fun findByUsername(@Query("name") username: String): Call<User>

    @GET("users/{userId}/logs")
    fun getLogs(@Path("userId") userId: String): Call<ResponseBody>

    @GET("users/{userId}/balance")
    fun getBalance(@Path("userId") userId: String): Call<UserInfo>

    @POST("users/{userId}/add-ticket")
    fun addTicket(@Path("userId") userId: String, @Body ticket: Log): Call<ResponseBody>

    @GET("users/search-by-similar")
    fun searchBySimilarName(@Query("name") name: String,
                            @Query("limit") limit: Long): Call<ResponseBody>
}
