package fabiorapanelo.com.donation.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fabiorapanelo.com.donation.R;
import fabiorapanelo.com.donation.adapter.DonationListAdapter;
import fabiorapanelo.com.donation.model.Donation;
import android.support.v4.app.Fragment;


public class DonationListFragment extends Fragment  {

    @Bind(R.id.list_view_donations)
    protected ListView listViewDonations;

    public static DonationListFragment newInstance() {
        DonationListFragment fragment = new DonationListFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_donation_list, container, false);

        ButterKnife.bind(this, view);

        listViewDonations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DonationListFragment.this.listViewDonationsOnItemClick(adapterView, view, i, l);
            }
        });

        List<Donation> donations = DonationListFragment.this.createDummyDonations();
        DonationListAdapter adapter = new DonationListAdapter(getActivity(), donations);
        listViewDonations.setAdapter(adapter);

        return view;

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
