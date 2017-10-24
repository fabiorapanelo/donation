package fabiorapanelo.com.donation.repositories;

import fabiorapanelo.com.donation.model.Campaign;
import fabiorapanelo.com.donation.model.Ticket;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by fabio on 24/10/2017.
 */

public interface TicketRepository {

    @GET("ticket/{ticketId}")
    Call<Ticket> find(@Path("ticketId") String ticketId);

    @POST("ticket")
    Call<Ticket> create(@Body Ticket ticket);

    @POST("ticket/{ticketId}/user/{userId}/consume")
    Call<ResponseBody> consume(@Path("ticketId") String ticketId, @Path("userId") String userId);

}
