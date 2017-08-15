package fabiorapanelo.com.donation.fragment;

import android.content.Context;
import android.content.Intent;
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
import fabiorapanelo.com.donation.activity.LoginActivity;
import fabiorapanelo.com.donation.activity.PickLocationActivity;
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

import static android.app.Activity.RESULT_OK;

public class CreateCampaignFragment extends Fragment {

    public static final int REQUEST_CODE_PICK_LOCATION = 1;

    @Bind(R.id.input_campaign_name)
    protected EditText campaignName;

    //@Bind(R.id.input_campaign_latitude)
    //protected EditText campaignLatitude;

    //@Bind(R.id.input_campaign_longitude)
    //protected EditText campaignLongitude;

    @Bind(R.id.btn_create_campaign)
    protected Button createCampaign;

    @Bind(R.id.btn_pick_location)
    protected Button pickLocation;

    protected String mLatitude;
    protected String mLongitude;
    protected boolean mLocationSelected = false;

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

        pickLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateCampaignFragment.this.pickLocation(view);
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

        if(!mLocationSelected){
            Toast.makeText(CreateCampaignFragment.this.getActivity(), "Localização deve ser selecionada!", Toast.LENGTH_SHORT).show();
        }
        String name = campaignName.getText().toString();
        User user = userDao.find();

        Campaign campaign = new Campaign();
        campaign.setName(name);
        campaign.setLatitude(mLatitude);
        campaign.setLongitude(mLongitude);
        campaign.setCreatedBy(UserService.getUrlForUser(user));

        campaignService.save(campaign, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.isSuccessful()){
                    Toast.makeText(CreateCampaignFragment.this.getActivity(), "Campanha cadastrada com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CreateCampaignFragment.this.getActivity(), "Falha ao cadastrar campanha!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(CreateCampaignFragment.this.getActivity(), "Falha ao cadastrar campanha!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    protected void pickLocation(View view){
        Intent intent = new Intent(this.getActivity(), PickLocationActivity.class);
        startActivityForResult(intent, REQUEST_CODE_PICK_LOCATION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_LOCATION && resultCode == RESULT_OK) {

            mLatitude = String.valueOf(data.getDoubleExtra("latitude", 0));
            mLongitude = String.valueOf(data.getDoubleExtra("longitude", 0));
            mLocationSelected = true;
        }
    }

}
