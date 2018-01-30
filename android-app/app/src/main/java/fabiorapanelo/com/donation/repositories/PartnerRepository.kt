package fabiorapanelo.com.donation.repositories

import fabiorapanelo.com.donation.model.Partner
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by fabio on 29/01/2018
 */

interface PartnerRepository {

    @POST("partners")
    fun create(@Body partner: Partner): Call<ResponseBody>

    @GET("partners/near-location")
    fun nearLocation(@Query("lat") latitude: Double,
                     @Query("long") longitude: Double,
                     @Query("distance") distanceInMeters: Double): Call<ResponseBody>

    @GET("partners/search-by-user/{userId}")
    fun searchByUser(@Path("userId") userId: String): Call<ResponseBody>

    @POST("partners/{partnerId}")
    fun update(@Path("partnerId") partnerId: String, @Body partner: Partner): Call<ResponseBody>

    @GET("partners/search-by-similar")
    fun searchBySimilarName(@Query("name") name: String,
                            @Query("limit") limit: Long): Call<ResponseBody>
}
