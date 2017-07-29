package fabiorapanelo.com.donation.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fabiorapanelo.com.donation.R;
import fabiorapanelo.com.donation.adapter.DonationListAdapter;
import fabiorapanelo.com.donation.model.Donation;

public class DonationListActivity extends BaseActivity {

    @Bind(R.id.list_view_donations)
    protected ListView listViewDonations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_list);

        ButterKnife.bind(this);

        listViewDonations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DonationListActivity.this.listViewDonationsOnItemClick(adapterView, view, i, l);
            }
        });

        List<Donation> donations = DonationListActivity.this.createDummyDonations();
        DonationListAdapter adapter = new DonationListAdapter(DonationListActivity.this, donations);
        listViewDonations.setAdapter(adapter);

    }

    protected void listViewDonationsOnItemClick(AdapterView<?> adapterView, View view, int i, long l){

    }

    protected List<Donation> createDummyDonations(){

        List<Donation> donations = new ArrayList<>();

        for(long i=0; i< 50; i++){
            Donation d1 = new Donation();
            d1.setId(i);
            d1.setName(i + "º APAE");
            donations.add(d1);
        }

        return donations;

    }
}
