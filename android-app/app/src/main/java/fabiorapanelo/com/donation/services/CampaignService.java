package fabiorapanelo.com.donation.services;

import fabiorapanelo.com.donation.model.Campaign;
import fabiorapanelo.com.donation.repositories.CampaignRepository;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Query;

/**
 * Created by fabio on 08/08/2017.
 */

public class CampaignService extends ServiceBase {

    public static final String CACHE_KEY_CAMPAIGN_SERVICE_FIND_MY_CAMPAIGNS = "CampaingService.findMyCampaigns";
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

    public void nearLocation(double latitude, double longitude,
                             double distanceInMeters, final Callback<ResponseBody> callback){
        Call<ResponseBody> call;
        call = campaignRepository.nearLocation(latitude, longitude, distanceInMeters);
        call.enqueue(callback);
    }

    public void searchByUser(String userId, final Callback<ResponseBody> callback){
        Call<ResponseBody> call;
        call = campaignRepository.searchByUser(userId);
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

    public void searchBySimilarName(String campaignName, long limit, final Callback<ResponseBody> callback){
        Call<ResponseBody> call;
        call = campaignRepository.searchBySimilarName(campaignName, limit);
        call.enqueue(callback);
    }

}
