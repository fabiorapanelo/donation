package fabiorapanelo.com.donation.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import fabiorapanelo.com.donation.R;
import fabiorapanelo.com.donation.fragment.DonationListFragment;
import fabiorapanelo.com.donation.fragment.FragNavController;
import fabiorapanelo.com.donation.utils.Utils;

//Inspired from https://github.com/f22labs/InstaLikeFragmentTransaction/

public class HomeActivity extends BaseActivity implements
        FragNavController.RootFragmentListener{

    @Bind(R.id.bottom_tab_layout)
    protected TabLayout bottomTabLayout;

    protected FragNavController mNavController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        this.initTab();

        mNavController = FragNavController.newBuilder(savedInstanceState, getSupportFragmentManager(), R.id.content_frame)
                .rootFragmentListener(this, 5)
                .build();

    }

    protected void initTab() {

        this.addTab(R.drawable.tab_home);
        this.addTab(R.drawable.tab_search);
        this.addTab(R.drawable.tab_share);
        this.addTab(R.drawable.tab_news);
        this.addTab(R.drawable.tab_profile);

    }

    protected void addTab(int iconId) {

        TabLayout.Tab tab = bottomTabLayout.newTab();

        View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.tab_item_bottom, null);
        ImageView icon = view.findViewById(R.id.tab_icon);

        icon.setImageDrawable(Utils.setDrawableSelector(this, iconId, iconId));

        tab.setCustomView(view);

        bottomTabLayout.addTab(tab);
    }

    @Override
    public Fragment getRootFragment(int index) {
        switch (index) {

            case FragNavController.TAB1:
                return new DonationListFragment();
            case FragNavController.TAB2:
                return new DonationListFragment();
            case FragNavController.TAB3:
                return new DonationListFragment();
            case FragNavController.TAB4:
                return new DonationListFragment();
            case FragNavController.TAB5:
                return new DonationListFragment();

        }
        throw new IllegalStateException("Need to send an index that we know");
    }

}
