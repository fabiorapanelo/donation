package fabiorapanelo.com.donation.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import fabiorapanelo.com.donation.R;
import fabiorapanelo.com.donation.adapter.CampaignImagePageAdapter;
import fabiorapanelo.com.donation.model.Campaign;
import fabiorapanelo.com.donation.utils.HaversineAlgorithm;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by fabio on 24/09/2017.
 */

public class CampaignDetailsActivity extends BaseActivity {

    @Bind(R.id.text_campaign_name)
    protected TextView textCampaignName;

    @Bind(R.id.view_pager_campaign_image)
    protected ViewPager viewPager;

    @Bind(R.id.indicator)
    protected CircleIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Campaign campaign = (Campaign) getIntent().getExtras().get("campaign");

        setContentView(R.layout.activity_campaign_details);

        ButterKnife.bind(this);

        this.setupToolbar();

        textCampaignName.setText(campaign.getName());
        viewPager.setAdapter(new CampaignImagePageAdapter(this, campaign.getImages()));
        indicator.setViewPager(viewPager);

    }
}
