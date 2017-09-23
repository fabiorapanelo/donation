package fabiorapanelo.com.donation.adapter;

/**
 * Created by fabio on 17/09/2017.
 */

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.TimeUnit;

import fabiorapanelo.com.donation.R;
import fabiorapanelo.com.donation.services.ServiceBase;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class CampaignImagePageAdapter extends PagerAdapter {

    private List<String> images;
    private LayoutInflater inflater;
    private Context context;
    private Picasso picasso;

    public CampaignImagePageAdapter(Context context, List<String> images) {
        this.context = context;
        this.images=images;
        inflater = LayoutInflater.from(context);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        okhttp3.OkHttpClient.Builder okHttp3Client = new okhttp3.OkHttpClient.Builder();
        okHttp3Client.addInterceptor(logging);
        okHttp3Client.readTimeout(60, TimeUnit.SECONDS);
        okHttp3Client.connectTimeout(60, TimeUnit.SECONDS);

        OkHttp3Downloader okHttp3Downloader = new OkHttp3Downloader(okHttp3Client.build());

        picasso = new Picasso.Builder(context)
                .downloader(okHttp3Downloader)
                .build();

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup viewGroup, int position) {
        View view = inflater.inflate(R.layout.item_campaign_image, viewGroup, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);

        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        String imageUrl = ServiceBase.getUrl("images/" + images.get(position));
        picasso.load(imageUrl).fit().centerCrop().into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
            }
        });

        viewGroup.addView(view, 0);

        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}