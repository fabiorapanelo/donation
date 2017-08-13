package fabiorapanelo.com.donation.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import fabiorapanelo.com.donation.R;
import fabiorapanelo.com.donation.activity.RegisterUserActivity;
import fabiorapanelo.com.donation.database.UserDao;
import fabiorapanelo.com.donation.model.Campaign;
import fabiorapanelo.com.donation.model.User;
import fabiorapanelo.com.donation.services.CampaignService;
import fabiorapanelo.com.donation.services.UserService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateCampaignFragment extends Fragment {

    @Bind(R.id.input_campaign_name)
    protected EditText campaignName;

    @Bind(R.id.input_campaign_latitude)
    protected EditText campaignLatitude;

    @Bind(R.id.input_campaign_longitude)
    protected EditText campaignLongitude;

    @Bind(R.id.btn_create_campaign)
    protected Button createCampaign;

    protected CampaignService campaignService;

    protected UserDao userDao;

    public static CreateCampaignFragment newInstance() {
        CreateCampaignFragment fragment = new CreateCampaignFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_campaign, container, false);

        ButterKnife.bind(this, view);

        campaignService = CampaignService.getInstance();

        userDao = new UserDao(this.getActivity());

        createCampaign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateCampaignFragment.this.createCampaign(view);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    protected void createCampaign(View view){

        String name = campaignName.getText().toString();
        String latitude = campaignLatitude.getText().toString();
        String longitude = campaignLongitude.getText().toString();
        User user = userDao.find();

        Campaign campaign = new Campaign();
        campaign.setName(name);
        campaign.setLatitude(latitude);
        campaign.setLongitude(longitude);
        campaign.setCreatedBy(UserService.getUrlForUser(user));

        campaignService.save(campaign, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.isSuccessful()){
                    Toast.makeText(CreateCampaignFragment.this.getActivity(), "Campanha cadastrada com sucesso!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(CreateCampaignFragment.this.getActivity(), "Falha ao cadastrar campanha!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(CreateCampaignFragment.this.getActivity(), "Falha ao cadastrar campanha!", Toast.LENGTH_LONG).show();
            }
        });

    }
}
