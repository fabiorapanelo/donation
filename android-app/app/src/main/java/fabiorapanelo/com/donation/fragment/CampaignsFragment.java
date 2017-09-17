package fabiorapanelo.com.donation.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fabiorapanelo.com.donation.R;
import fabiorapanelo.com.donation.adapter.CampaignListAdapter;
import fabiorapanelo.com.donation.model.Campaign;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static fabiorapanelo.com.donation.services.CampaignService.CACHE_KEY_CAMPAING_SERVICE_FIND;
import static fabiorapanelo.com.donation.services.CampaignService.CACHE_TIMEOUT_CAMPAING_SERVICE_FIND;

//https://www.learn2crack.com/2016/02/image-loading-recyclerview-picasso.html
public class CampaignsFragment extends BaseFragment  {

    @Bind(R.id.recycler_view_campaigns)
    protected RecyclerView recyclerViewCampaigns;

    public static CampaignsFragment newInstance() {
        CampaignsFragment fragment = new CampaignsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_campaigns, container, false);

        ButterKnife.bind(this, view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerViewCampaigns.setLayoutManager(layoutManager);

        this.findCampaigns();

        return view;
    }

    private void findCampaigns(){

        Object object = cacheManager.get(CACHE_KEY_CAMPAING_SERVICE_FIND);
        if(object != null){

            List<Campaign> campaigns = (List<Campaign>) object;

            CampaignListAdapter adapter = new CampaignListAdapter(this.getContext(), campaigns);
            recyclerViewCampaigns.setAdapter(adapter);

        } else {

            campaignService.find(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()){

                        try {

                            String body = response.body().string();
                            String campaignsArray = new JSONObject(body).getJSONObject("_embedded").getJSONArray("campaigns").toString();
                            Type listType = new TypeToken<ArrayList<Campaign>>(){}.getType();
                            List<Campaign> campaigns = new Gson().fromJson(campaignsArray, listType);

                            cacheManager.put(CACHE_KEY_CAMPAING_SERVICE_FIND, campaigns, CACHE_TIMEOUT_CAMPAING_SERVICE_FIND);
                            CampaignListAdapter adapter = new CampaignListAdapter(
                                    CampaignsFragment.this.getContext(), campaigns);
                            recyclerViewCampaigns.setAdapter(adapter);

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(CampaignsFragment.this.getActivity(), "Falha ao recuperar as campanhas!", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(CampaignsFragment.this.getActivity(), "Falha ao recuperar as campanhas!", Toast.LENGTH_LONG).show();
                }
            });

        }


    }
}
