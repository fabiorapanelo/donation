package fabiorapanelo.com.donation.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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

import static fabiorapanelo.com.donation.services.UserService.CACHE_KEY_USER_SERVICE_FIND;
import static fabiorapanelo.com.donation.services.UserService.CACHE_TIMEOUT_USER_SERVICE_FIND;

public class ProfileFragment extends BaseFragment {

    @Bind(R.id.text_profile_name)
    protected TextView profileName;

    @Bind(R.id.recycler_view_campaigns)
    protected RecyclerView recyclerViewCampaigns;

    @Bind(R.id.btn_edit_profile)
    protected Button editProfile;

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ButterKnife.bind(this, view);

        profileName.setText(user.getName());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerViewCampaigns.setLayoutManager(layoutManager);

        this.findMyCampaigns();

        return view;
    }

    private void findMyCampaigns(){

        Object object = cacheManager.get(CACHE_KEY_USER_SERVICE_FIND);
        if(object != null){

            List<Campaign> campaigns = (List<Campaign>) object;
            CampaignListAdapter adapter = new CampaignListAdapter(this.getContext(), campaigns, null);
            recyclerViewCampaigns.setAdapter(adapter);

        } else {
            campaignService.searchByUser(user.getId().toString(), new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()){

                        try {

                            String body = response.body().string();
                            Type listType = new TypeToken<ArrayList<Campaign>>(){}.getType();
                            List<Campaign> campaigns = new Gson().fromJson(body, listType);

                            cacheManager.put(CACHE_KEY_USER_SERVICE_FIND, campaigns, CACHE_TIMEOUT_USER_SERVICE_FIND);

                            CampaignListAdapter adapter = new CampaignListAdapter(
                                    ProfileFragment.this.getContext(), campaigns, null);
                            recyclerViewCampaigns.setAdapter(adapter);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(ProfileFragment.this.getActivity(), "Falha ao cadastrar campanha!", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(ProfileFragment.this.getActivity(), "Falha ao recuperar suas campanhas!", Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
