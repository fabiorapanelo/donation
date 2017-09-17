package fabiorapanelo.com.donation.services;

import fabiorapanelo.com.donation.model.Campaign;
import fabiorapanelo.com.donation.repositories.CampaignRepository;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by fabio on 08/08/2017.
 */

public class CampaignService extends ServiceBase {

    public static final String CACHE_KEY_CAMPAING_SERVICE_FIND = "CampaingService.find";
    public static final int CACHE_TIMEOUT_CAMPAING_SERVICE_FIND = 30;

    protected CampaignRepository campaignRepository;

    private static CampaignService instance = new CampaignService();

    private CampaignService(){
        super();
        campaignRepository = retrofit.create(CampaignRepository.class);
    }

    public static CampaignService getInstance(){
        return instance;
    }

    public void find(final Callback<ResponseBody> callback){
        Call<ResponseBody> call;
        call = campaignRepository.find();
        call.enqueue(callback);
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
