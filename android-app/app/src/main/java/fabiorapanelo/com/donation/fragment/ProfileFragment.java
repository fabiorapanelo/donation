package fabiorapanelo.com.donation.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import org.json.JSONArray;
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
import fabiorapanelo.com.donation.adapter.DonationListAdapter;
import fabiorapanelo.com.donation.database.UserDao;
import fabiorapanelo.com.donation.model.Campaign;
import fabiorapanelo.com.donation.model.User;
import fabiorapanelo.com.donation.services.CampaignService;
import fabiorapanelo.com.donation.services.UserService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    @Bind(R.id.text_profile_name)
    protected TextView profileName;

    @Bind(R.id.list_view_my_campaigns)
    protected ListView listViewMyCampaigns;

    @Bind(R.id.btn_edit_profile)
    protected Button editProfile;

    protected UserDao userDao;

    protected User user;

    protected UserService userService;

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ButterKnife.bind(this, view);

        userDao = new UserDao(this.getActivity());
        user = userDao.find();
        profileName.setText(user.getName());

        this.findMyCampaigns();

        return view;
    }

    private void findMyCampaigns(){

        userService = UserService.getInstance();
        userService.findMyCampaigns(user.getId(), new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){

                    try {

                        String body = response.body().string();
                        String campaigns = new JSONObject(body).getJSONObject("_embedded").getJSONArray("campaigns").toString();
                        Type listType = new TypeToken<ArrayList<Campaign>>(){}.getType();
                        List<Campaign> myCampaigns = new Gson().fromJson(campaigns, listType);

                        CampaignListAdapter adapter = new CampaignListAdapter(getActivity(), myCampaigns);
                        listViewMyCampaigns.setAdapter(adapter);

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
