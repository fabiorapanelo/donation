package fabiorapanelo.com.donation.services;

import fabiorapanelo.com.donation.model.Credentials;
import fabiorapanelo.com.donation.model.Ticket;
import fabiorapanelo.com.donation.model.User;
import fabiorapanelo.com.donation.repositories.TicketRepository;
import fabiorapanelo.com.donation.repositories.UserRepository;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by fabio on 24/10/2017.
 */

public class TicketService extends ServiceBase {

    protected TicketRepository ticketRepository;

    private static TicketService instance = new TicketService();

    private TicketService(){
        super();
        ticketRepository = retrofit.create(TicketRepository.class);
    }

    public static TicketService getInstance(){
        return instance;
    }

    public void create(Ticket ticket, final Callback<Ticket> callback) {
        Call<Ticket> call = ticketRepository.create(ticket);
        call.enqueue(callback);
    }

    public void find(String ticketId, final Callback<Ticket> callback) {
        Call<Ticket> call = ticketRepository.find(ticketId);
        call.enqueue(callback);
    }

    public void consume(String ticketId, String userId, final Callback<ResponseBody> callback) {
        Call<ResponseBody> call = ticketRepository.consume(ticketId, userId);
        call.enqueue(callback);
    }
}
