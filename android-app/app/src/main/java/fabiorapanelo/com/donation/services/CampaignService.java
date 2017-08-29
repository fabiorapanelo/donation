package fabiorapanelo.com.donation.services;

import android.app.Service;

import fabiorapanelo.com.donation.model.Campaign;
import fabiorapanelo.com.donation.model.User;
import fabiorapanelo.com.donation.repositories.CampaignRepository;
import fabiorapanelo.com.donation.repositories.UserRepository;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by fabio on 08/08/2017.
 */

public class CampaignService extends ServiceBase {

    protected CampaignRepository campaignRepository;

    private static CampaignService instance = new CampaignService();

    private CampaignService(){
        super();
        campaignRepository = retrofit.create(CampaignRepository.class);
    }

    public static CampaignService getInstance(){
        return instance;
    }

    public void save(Campaign campaign, final Callback<ResponseBody> callback) {

        Call<ResponseBody> call;
        if(campaign.getId() != null){
            call = campaignRepository.update(campaign.getId().toString(), campaign);
        } else {
            call = campaignRepository.create(campaign);
        }

        call.enqueue(callback);

    }
}
