package fabiorapanelo.com.donation.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import fabiorapanelo.com.donation.R;
import fabiorapanelo.com.donation.activity.CampaignDetailsActivity;
import fabiorapanelo.com.donation.activity.PartnerDetailsActivity;
import fabiorapanelo.com.donation.model.Campaign;
import fabiorapanelo.com.donation.model.Partner;
import fabiorapanelo.com.donation.services.ServiceBase;
import fabiorapanelo.com.donation.utils.HaversineAlgorithm;
import fabiorapanelo.com.donation.utils.LocationUtils;

/**
 * Created by fabio on 15/07/2017.
 */

//https://www.androidtutorialpoint.com/basics/android-image-slider-tutorial/
public class PartnerListAdapter extends RecyclerView.Adapter<PartnerListAdapter.ViewHolder> {

    private List<Partner> partners;
    private Activity activity;
    private Location lastLocation;

    private static final int REQUEST_CODE_CAMPAIGN_DETAILS = 1;

    public PartnerListAdapter(Activity activity, List<Partner> partners, Location lastLocation) {
        this.activity = activity;
        this.partners = partners;
        this.lastLocation = lastLocation;
    }

    @Override
    public PartnerListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_campaign, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        final Partner partner = partners.get(i);

        String name = partner.getName();
        viewHolder.textView.setText(name);

        double longitude = partner.getLocation().getCoordinates().get(0);
        double latitude = partner.getLocation().getCoordinates().get(1);
        String locationName = LocationUtils.getLocationName(this.activity, longitude, latitude);
        if(lastLocation != null){
            locationName += " - " + HaversineAlgorithm.getDistance(partner.getLocation(), lastLocation);
        }
        viewHolder.textLocation.setText(locationName);

        if(partner.getImages() != null && partner.getImages().size() > 0){


            String imageUrl = ServiceBase.getUrl("images/" + partner.getImages().get(0));

            RequestOptions options = new RequestOptions()
                    .centerCrop();

            Glide.with(this.activity)
                .load(imageUrl)
                .apply(options)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        viewHolder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        viewHolder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(viewHolder.imageView);
        } else {
            viewHolder.progressBar.setVisibility(View.GONE);
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PartnerListAdapter.this.onPartnerClick(partner);
            }
        };
        viewHolder.itemView.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return partners.size();
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

    protected void onPartnerClick(Partner partner){

        Intent intent = new Intent(this.activity, PartnerDetailsActivity.class);
        intent.putExtra("partner", partner);

        activity.startActivityForResult(intent, REQUEST_CODE_CAMPAIGN_DETAILS);

    }



}
