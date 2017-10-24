package fabiorapanelo.com.donation.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import fabiorapanelo.com.donation.R;
import fabiorapanelo.com.donation.adapter.CampaignImagePageAdapter;
import fabiorapanelo.com.donation.model.Campaign;
import fabiorapanelo.com.donation.model.Log;
import me.relex.circleindicator.CircleIndicator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    @Bind(R.id.btn_donate)
    protected Button btnDonate;

    @Bind(R.id.seek_bar_donation_value)
    protected SeekBar seekBarDonationValue;

    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    @Bind(R.id.donate_message_layout)
    protected LinearLayout mDonateMessageLayout;

    @Bind(R.id.donate_button_layout)
    protected LinearLayout mDonateButtonLayout;

    @Bind(R.id.txt_user_balance)
    protected  TextView txtUserBalance;

    protected Campaign campaign;
    protected static final int DONATION_START_VALUE = 0;
    protected static final String LOG_TYPE = "DONATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        campaign = (Campaign) getIntent().getExtras().get("campaign");

        setContentView(R.layout.activity_campaign_details);

        ButterKnife.bind(this);

        this.setupToolbar();

        textCampaignName.setText(campaign.getName());
        viewPager.setAdapter(new CampaignImagePageAdapter(this, campaign.getImages()));
        indicator.setViewPager(viewPager);

        btnDonate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    donate(view);
                }
            }
        );

        seekBarDonationValue.setProgress(DONATION_START_VALUE);
        setDonationText(DONATION_START_VALUE);

        seekBarDonationValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setDonationText(seekBar.getProgress());
            }
        });

        this.getBalance();
    }

    protected void donate(View view){

        btnDonate.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        int donationValue = seekBarDonationValue.getProgress();

        Log donation = new Log();
        donation.setDate(new Date().getTime());
        donation.setType(LOG_TYPE);
        donation.setQuantity(donationValue);
        donation.setUserFrom(user.getId().toString());
        donation.setUserTo(campaign.getUserId());
        donation.setCampaign(campaign.getId());

        donationService.save(donation, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.isSuccessful()){
                    mDonateMessageLayout.setVisibility(View.VISIBLE);
                    mDonateButtonLayout.setVisibility(View.GONE);
                } else {
                    Toast.makeText(CampaignDetailsActivity.this, "Falha ao realizar doação!", Toast.LENGTH_LONG).show();
                }
                btnDonate.setEnabled(true);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                btnDonate.setEnabled(true);
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(CampaignDetailsActivity.this, "Falha ao realizar doação!", Toast.LENGTH_LONG).show();
            }
        });

    }

    protected void setDonationText(int donationValue){
        String text = getResources().getQuantityString(R.plurals.button_donate_messages, donationValue, donationValue);
        btnDonate.setText(text);
    }

    protected void getBalance(){

        btnDonate.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        userService.getBalance(user, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if(response.isSuccessful()){
                    try {
                        Integer balance = Integer.valueOf(response.body().string());
                        if(balance > 0){
                            btnDonate.setEnabled(true);
                        }
                        seekBarDonationValue.setMax(balance);
                        txtUserBalance.setText(getResources().getQuantityString(R.plurals.text_user_balance, balance, balance));

                    } catch (Exception e) {
                        txtUserBalance.setText(getResources().getString(R.string.text_user_balance_unavailable));
                    }
                } else {
                    txtUserBalance.setText(getResources().getString(R.string.text_user_balance_unavailable));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                txtUserBalance.setText(getResources().getString(R.string.text_user_balance_unavailable));
            }
        });
    }
}
