package fabiorapanelo.com.donation.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import fabiorapanelo.com.donation.R;

public class ChooseUserTypeActivity extends BaseActivity {

    @Bind(R.id.image_donate)
    ImageView donate;
    @Bind(R.id.image_receive_donation)
    ImageView receiveDonation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_user_type);

        ButterKnife.bind(this);

        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                donate(view);
            }
        });
    }

    protected void donate(View view){
        Intent intent = new Intent(this, DonationListActivity.class);
        startActivity(intent);
    }
}
