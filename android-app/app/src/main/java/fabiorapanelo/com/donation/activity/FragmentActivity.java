package fabiorapanelo.com.donation.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ncapdevi.fragnav.FragNavController;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fabiorapanelo.com.donation.R;
import fabiorapanelo.com.donation.fragment.AddTicketFragment;
import fabiorapanelo.com.donation.fragment.CampaignsFragment;
import fabiorapanelo.com.donation.fragment.UserInformationFragment;
import fabiorapanelo.com.donation.fragment.SearchFragment;

//Inspiration: https://github.com/f22labs/InstaLikeFragmentTransaction/
//Using: https://github.com/ncapdevi/FragNav
//Improvement to be done: https://github.com/roughike/BottomBar

public class FragmentActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {

    @Bind(R.id.bottom_tab_layout)
    protected TabLayout bottomTabLayout;

    @Bind(R.id.my_toolbar)
    protected Toolbar mTopToolbar;

    @Bind(R.id.edit_text_campaign_name2)
    protected EditText editTextCampaignName;

    protected FragNavController mNavController;
    protected String[] tabsName;

    protected int currentDistanceInKM = 20;
    protected int tempDistanceInKM = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        ButterKnife.bind(this);

        setSupportActionBar(mTopToolbar);

        this.initTabs();

        mNavController = FragNavController.newBuilder(savedInstanceState, getSupportFragmentManager(), R.id.content_frame)
                .rootFragments(this.createFragmentList())
                .build();

        bottomTabLayout.addOnTabSelectedListener(this);

        tabsName = getResources().getStringArray(R.array.tabs);

        updateToolbar(0);

        editTextCampaignName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    searchCampaigns();
                    return true;
                }
                return false;
            }
        });

        mNavController.switchTab(0);

    }

    protected void initTabs() {
        this.addTab(R.drawable.ic_home_black_24dp);
        this.addTab(R.drawable.ic_search_white_24px);
        this.addTab(R.drawable.ic_add_box_black_24px);
        this.addTab(R.drawable.ic_favorite_white_24px);
        this.addTab(R.drawable.ic_person_black_24px);
    }

    protected void addTab(int iconId) {

        TabLayout.Tab tab = bottomTabLayout.newTab();

        View view = LayoutInflater.from(FragmentActivity.this).inflate(R.layout.tab_item_bottom, null);
        ImageView icon = view.findViewById(R.id.tab_icon);

        icon.setImageResource(iconId);

        tab.setCustomView(view);

        bottomTabLayout.addTab(tab);
    }

    public List<Fragment> createFragmentList() {

        List<Fragment> fragments = new ArrayList<>(5);

        fragments.add(CampaignsFragment.newInstance(currentDistanceInKM));
        fragments.add(SearchFragment.newInstance(null));
        fragments.add(AddTicketFragment.newInstance());
        fragments.add(AddTicketFragment.newInstance());
        fragments.add(UserInformationFragment.Factory.newInstance());

        return fragments;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        updateToolbar(tab.getPosition());
        mNavController.switchTab(tab.getPosition());

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

        mNavController.clearStack();
        updateToolbar(tab.getPosition());
        mNavController.switchTab(tab.getPosition());

    }

    public void updateToolbar(int position) {

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(tabsName[position]);
        }

        if(position == 1){
            editTextCampaignName.setVisibility(View.VISIBLE);
        } else {
            editTextCampaignName.setVisibility(View.GONE);
        }

        if(mTopToolbar.getMenu().size() > 0){
            if(position == 0){
                mTopToolbar.getMenu().getItem(0).setVisible(true);
            } else {
                mTopToolbar.getMenu().getItem(0).setVisible(false);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filter) {

            showFilterDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void showFilterDialog(){
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_distance);
        dialog.setTitle(R.string.dialog_distance_title);

        // set the custom dialog components - text, image and button
        final TextView currentDistance = dialog.findViewById(R.id.text_current_distance);
        currentDistance.setText("Distância: " + tempDistanceInKM + "km");

        final SeekBar seekBarDistance = dialog.findViewById(R.id.seek_bar_distance);
        seekBarDistance.setProgress(tempDistanceInKM);

        seekBarDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                FragmentActivity.this.tempDistanceInKM = seekBar.getProgress();
                currentDistance.setText("Distância: " + seekBar.getProgress() + "km");
            }
        });

        Button dialogButton = dialog.findViewById(R.id.btn_confirm_distance);

        dialogButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentActivity.this.currentDistanceInKM = FragmentActivity.this.tempDistanceInKM;

                Fragment fragment = CampaignsFragment.newInstance(FragmentActivity.this.currentDistanceInKM);
                FragmentActivity.this.mNavController.replaceFragment(fragment);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    protected void searchCampaigns(){
        String campaignName = editTextCampaignName.getText().toString();
        Fragment fragment = SearchFragment.newInstance(campaignName);
        FragmentActivity.this.mNavController.replaceFragment(fragment);
    }


}
