package fabiorapanelo.com.donation.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.ncapdevi.fragnav.FragNavController;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fabiorapanelo.com.donation.R;
import fabiorapanelo.com.donation.fragment.CampaignsFragment;
import fabiorapanelo.com.donation.fragment.CreateCampaignFragment;
import fabiorapanelo.com.donation.fragment.FavoritesFragment;
import fabiorapanelo.com.donation.fragment.ProfileFragment;
import fabiorapanelo.com.donation.fragment.SearchFragment;
import fabiorapanelo.com.donation.utils.Utils;

//Inspiration: https://github.com/f22labs/InstaLikeFragmentTransaction/
//Using: https://github.com/ncapdevi/FragNav
//Improvement to be done: https://github.com/roughike/BottomBar

public class HomeActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {

    @Bind(R.id.bottom_tab_layout)
    protected TabLayout bottomTabLayout;

    protected FragNavController mNavController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        this.initTabs();

        mNavController = FragNavController.newBuilder(savedInstanceState, getSupportFragmentManager(), R.id.content_frame)
                .rootFragments(this.createFragmentList())
                .build();

        bottomTabLayout.addOnTabSelectedListener(this);

        mNavController.switchTab(0);

    }

    protected void initTabs() {
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

    public List<Fragment> createFragmentList() {

        List<Fragment> fragments = new ArrayList<>(5);

        fragments.add(CampaignsFragment.newInstance());
        fragments.add(SearchFragment.newInstance());
        fragments.add(CreateCampaignFragment.newInstance());
        fragments.add(FavoritesFragment.newInstance());
        fragments.add(ProfileFragment.newInstance());

        return fragments;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mNavController.switchTab(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

        mNavController.clearStack();
        mNavController.switchTab(tab.getPosition());

    }
}
