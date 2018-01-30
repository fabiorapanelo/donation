package fabiorapanelo.com.donation.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import fabiorapanelo.com.donation.cache.CacheManager;
import fabiorapanelo.com.donation.database.UserDao;
import fabiorapanelo.com.donation.model.User;
import fabiorapanelo.com.donation.services.CampaignService;
import fabiorapanelo.com.donation.services.DonationService;
import fabiorapanelo.com.donation.services.ImageService;
import fabiorapanelo.com.donation.services.PartnerService;
import fabiorapanelo.com.donation.services.TicketService;
import fabiorapanelo.com.donation.services.UserService;

/**
 * Created by fabio on 15/07/2017.
 */

public class BaseActivity extends AppCompatActivity {

    protected UserDao userDao;
    protected User user;
    protected CampaignService campaignService;
    protected ImageService imageService;
    protected UserService userService;
    protected DonationService donationService;
    protected TicketService ticketService;
    protected PartnerService partnerService;
    protected CacheManager cacheManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userDao = new UserDao(this);
        user = userDao.find();

        campaignService = CampaignService.getInstance();
        imageService = ImageService.getInstance();
        userService = UserService.getInstance();
        donationService = DonationService.getInstance();
        ticketService = TicketService.getInstance();
        partnerService = PartnerService.Factory.getInstance();
        cacheManager = CacheManager.getInstance();
    }

    protected void setupToolbar(){
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}