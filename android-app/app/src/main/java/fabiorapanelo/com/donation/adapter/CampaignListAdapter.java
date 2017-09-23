package fabiorapanelo.com.donation.adapter;

import android.content.Context;
import android.location.Location;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import fabiorapanelo.com.donation.R;
import fabiorapanelo.com.donation.utils.HaversineAlgorithm;
import fabiorapanelo.com.donation.model.Campaign;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by fabio on 15/07/2017.
 */

//https://www.androidtutorialpoint.com/basics/android-image-slider-tutorial/
public class CampaignListAdapter  extends RecyclerView.Adapter<CampaignListAdapter.ViewHolder> {
    private List<Campaign> campaigns;
    private Context context;
    private Location lastLocation;

    public CampaignListAdapter(Context context, List<Campaign> campaigns, Location lastLocation) {
        this.context = context;
        this.campaigns = campaigns;
        this.lastLocation = lastLocation;
    }

    @Override
    public CampaignListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_campaign, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        Campaign campaign = campaigns.get(i);

        String name = campaign.getName();
        if(lastLocation != null){
            name += " - " + this.getDistance(campaign, lastLocation);
        }

        viewHolder.textView.setText(name);
        viewHolder.viewPager.setAdapter(new CampaignImagePageAdapter(this.context, campaign.getImages()));
        viewHolder.indicator.setViewPager(viewHolder.viewPager);

    }

    @Override
    public int getItemCount() {
        return campaigns.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ViewPager viewPager;
        CircleIndicator indicator;

        public ViewHolder(View view) {
            super(view);

            textView = (TextView) view.findViewById(R.id.text_campaign_name);
            viewPager = (ViewPager) view.findViewById(R.id.view_pager_campaign_image);
            indicator = (CircleIndicator)  view.findViewById(R.id.indicator);
        }
    }

    protected String getDistance(Campaign campaign, Location lastLocation){
        List<Double> coordinates = campaign.getLocation().getCoordinates();
        double longitude = coordinates.get(0);
        double latitude = coordinates.get(1);

        double distance = HaversineAlgorithm.HaversineInKM(lastLocation.getLatitude(),
                lastLocation.getLongitude(),
                latitude,
                longitude);

        NumberFormat formatter = new DecimalFormat("#0.00");
        if(distance >= 0.1D){
            return formatter.format(distance) + " km";
        } else {
            distance = distance * 1000;
            return formatter.format(distance) + " m";
        }

    }

}
