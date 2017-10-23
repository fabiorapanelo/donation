package fabiorapanelo.com.donation.services;

import fabiorapanelo.com.donation.model.Log;
import fabiorapanelo.com.donation.repositories.DonationRepository;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by fabio on 23/10/2017.
 */

public class DonationService extends ServiceBase {

    protected DonationRepository donationRepository;

    private static DonationService instance = new DonationService();

    private DonationService(){
        super();
        donationRepository = retrofit.create(DonationRepository.class);
    }

    public static DonationService getInstance(){
        return instance;
    }

    public void save(Log donation, final Callback<ResponseBody> callback) {

        Call<ResponseBody> call = donationRepository.create(donation);
        call.enqueue(callback);

    }
}
