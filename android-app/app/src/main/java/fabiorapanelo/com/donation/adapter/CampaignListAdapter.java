package fabiorapanelo.com.donation.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import fabiorapanelo.com.donation.R;
import fabiorapanelo.com.donation.model.Campaign;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by fabio on 15/07/2017.
 */

//https://www.androidtutorialpoint.com/basics/android-image-slider-tutorial/
public class CampaignListAdapter  extends RecyclerView.Adapter<CampaignListAdapter.ViewHolder> {
    private List<Campaign> campaigns;
    private Context context;

    public CampaignListAdapter(Context context, List<Campaign> campaigns) {
        this.context = context;
        this.campaigns = campaigns;

    }

    @Override
    public CampaignListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_campaign, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        Campaign campaign = campaigns.get(i);

        viewHolder.textView.setText(campaign.getName());
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
}
