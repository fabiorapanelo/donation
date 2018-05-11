package fabiorapanelo.com.donation.repositories

import fabiorapanelo.com.donation.model.Campaign
import fabiorapanelo.com.donation.model.Ticket
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by fabio on 24/10/2017.
 */

interface TicketRepository {

    @GET("ticket/{ticketId}")
    fun find(@Path("ticketId") ticketId: String): Call<Ticket>

    @POST("ticket")
    fun create(@Body ticket: Ticket): Call<Ticket>

    @POST("ticket/{ticketId}/user/{userId}/consume")
    fun consume(@Path("ticketId") ticketId: String, @Path("userId") userId: String): Call<ResponseBody>

}
