package fabiorapanelo.com.donation.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import fabiorapanelo.com.donation.R;
import fabiorapanelo.com.donation.activity.CampaignDetailsActivity;
import fabiorapanelo.com.donation.activity.LoginActivity;
import fabiorapanelo.com.donation.activity.RegisterUserActivity;
import fabiorapanelo.com.donation.model.Campaign;
import fabiorapanelo.com.donation.services.ServiceBase;
import fabiorapanelo.com.donation.utils.HaversineAlgorithm;
import fabiorapanelo.com.donation.utils.LocationUtils;
import me.relex.circleindicator.CircleIndicator;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by fabio on 15/07/2017.
 */

//https://www.androidtutorialpoint.com/basics/android-image-slider-tutorial/
public class CampaignListAdapter  extends RecyclerView.Adapter<CampaignListAdapter.ViewHolder> {

    private List<Campaign> campaigns;
    private Activity activity;
    private Location lastLocation;
    private Picasso picasso;

    private static final int REQUEST_CODE_CAMPAIGN_DETAILS = 1;

    public CampaignListAdapter(Activity activity, List<Campaign> campaigns, Location lastLocation) {
        this.activity = activity;
        this.campaigns = campaigns;
        this.lastLocation = lastLocation;


        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        okhttp3.OkHttpClient.Builder okHttp3Client = new okhttp3.OkHttpClient.Builder();
        okHttp3Client.addInterceptor(logging);
        okHttp3Client.readTimeout(60, TimeUnit.SECONDS);
        okHttp3Client.connectTimeout(60, TimeUnit.SECONDS);

        OkHttp3Downloader okHttp3Downloader = new OkHttp3Downloader(okHttp3Client.build());

        picasso = new Picasso.Builder(activity)
                .downloader(okHttp3Downloader)
                .build();
    }

    @Override
    public CampaignListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_campaign, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        final Campaign campaign = campaigns.get(i);

        String name = campaign.getName() + " - Por: User#" + campaign.getUserId();
        viewHolder.textView.setText(name);

        double longitude = campaign.getLocation().getCoordinates().get(0);
        double latitude = campaign.getLocation().getCoordinates().get(1);
        String locationName = LocationUtils.getLocationName(this.activity, longitude, latitude);
        if(lastLocation != null){
            locationName += " - " + HaversineAlgorithm.getDistance(campaign, lastLocation);
        }
        viewHolder.textLocation.setText(locationName);

        if(campaign.getImages() != null && campaign.getImages().size() > 0){
            String imageUrl = ServiceBase.getUrl("images/" + campaign.getImages().get(0));
            picasso.load(imageUrl).fit().centerCrop().into(viewHolder.imageView, new Callback() {
                @Override
                public void onSuccess() {
                    viewHolder.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    viewHolder.progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            viewHolder.progressBar.setVisibility(View.GONE);
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CampaignListAdapter.this.onCampaignClick(campaign);
            }
        };
        viewHolder.itemView.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return campaigns.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;
        ProgressBar progressBar;
        TextView textLocation;

        public ViewHolder(View view) {
            super(view);

            textView = view.findViewById(R.id.text_campaign_name);
            imageView = view.findViewById(R.id.image_view_campaign);
            progressBar = view.findViewById(R.id.progress_bar);
            textLocation = view.findViewById(R.id.text_location);
        }
    }

    protected void onCampaignClick(Campaign campaign){

        Intent intent = new Intent(this.activity, CampaignDetailsActivity.class);
        intent.putExtra("campaign", campaign);

        activity.startActivityForResult(intent, REQUEST_CODE_CAMPAIGN_DETAILS);

    }



}
