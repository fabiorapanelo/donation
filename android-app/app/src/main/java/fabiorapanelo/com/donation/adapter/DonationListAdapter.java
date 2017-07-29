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
import fabiorapanelo.com.donation.model.Donation;

/**
 * Created by fabio on 15/07/2017.
 */

public class DonationListAdapter  extends ArrayAdapter<Donation> {

    private LayoutInflater layoutInflater;

    public DonationListAdapter(Activity activity, List<Donation> categories) {
        super(activity, R.layout.donation, categories);
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.donation, null);
        }
        Donation donation = getItem(position);
        TextView textView = (TextView) convertView.findViewById(R.id.text_donation_name);
        textView.setText(donation.getName());

        return convertView;

    }
}
