package fabiorapanelo.com.donation.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fabiorapanelo.com.donation.R;
import fabiorapanelo.com.donation.activity.PickLocationActivity;
import fabiorapanelo.com.donation.adapter.CampaignListAdapter;
import fabiorapanelo.com.donation.model.Campaign;
import fabiorapanelo.com.donation.utils.PermissionUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static fabiorapanelo.com.donation.services.CampaignService.CACHE_KEY_CAMPAING_SERVICE_FIND;
import static fabiorapanelo.com.donation.services.CampaignService.CACHE_TIMEOUT_CAMPAING_SERVICE_FIND;

//https://www.learn2crack.com/2016/02/image-loading-recyclerview-picasso.html
public class CampaignsFragment extends BaseFragment {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private FusedLocationProviderClient mFusedLocationClient;

    @Bind(R.id.recycler_view_campaigns)
    protected RecyclerView recyclerViewCampaigns;

    protected int distanceInKM;

    public void setDistanceInKM(int distanceInKM){
        this.distanceInKM = distanceInKM;
    }

    public static CampaignsFragment newInstance(int distanceInKM) {
        CampaignsFragment fragment = new CampaignsFragment();
        fragment.setDistanceInKM(distanceInKM);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_campaigns, container, false);

        ButterKnife.bind(this, view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerViewCampaigns.setLayoutManager(layoutManager);

        this.enableMyLocation();

        return view;
    }

    private void findCampaigns(final Location location) {

        Object object = cacheManager.get(CACHE_KEY_CAMPAING_SERVICE_FIND + distanceInKM);
        if (object != null) {

            List<Campaign> campaigns = (List<Campaign>) object;

            CampaignListAdapter adapter = new CampaignListAdapter(this.getActivity(), campaigns, location);
            recyclerViewCampaigns.setAdapter(adapter);

        } else {

            campaignService.nearLocation(location.getLatitude(), location.getLongitude(), distanceInKM * 1000, new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {

                        try {

                            String body = response.body().string();
                            Type listType = new TypeToken<ArrayList<Campaign>>() {}.getType();
                            List<Campaign> campaigns = new Gson().fromJson(body, listType);

                            cacheManager.put(CACHE_KEY_CAMPAING_SERVICE_FIND + distanceInKM, campaigns, CACHE_TIMEOUT_CAMPAING_SERVICE_FIND);
                            CampaignListAdapter adapter = new CampaignListAdapter(
                                    CampaignsFragment.this.getActivity(), campaigns, location);
                            recyclerViewCampaigns.setAdapter(adapter);

                        } catch (IOException e) {
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

    private void enableMyLocation() {

        AppCompatActivity activity = (AppCompatActivity) this.getActivity();
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(activity, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else {

            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getActivity());
            mFusedLocationClient.getLastLocation().addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        CampaignsFragment.this.findCampaigns(location);
                    }
                }
            });

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            showMissingPermissionError();
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog.newInstance(true).show(getFragmentManager(), "dialog");
    }
}
