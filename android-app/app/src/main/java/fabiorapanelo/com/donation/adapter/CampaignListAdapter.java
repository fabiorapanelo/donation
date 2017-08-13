package fabiorapanelo.com.donation.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import fabiorapanelo.com.donation.R;
import fabiorapanelo.com.donation.model.Campaign;
import fabiorapanelo.com.donation.model.Donation;

/**
 * Created by fabio on 15/07/2017.
 */

public class CampaignListAdapter extends ArrayAdapter<Campaign> {

    private LayoutInflater layoutInflater;

    public CampaignListAdapter(Activity activity, List<Campaign> campaigns) {
        super(activity, R.layout.item_campaign, campaigns);
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_campaign, null);
        }
        Campaign campaign = getItem(position);
        TextView textView = (TextView) convertView.findViewById(R.id.text_campaign_name);
        textView.setText(campaign.getName());

        return convertView;
    }
}
