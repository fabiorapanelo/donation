package fabiorapanelo.com.donation.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import fabiorapanelo.com.donation.cache.CacheManager;
import fabiorapanelo.com.donation.database.UserDao;
import fabiorapanelo.com.donation.model.User;
import fabiorapanelo.com.donation.services.CampaignService;
import fabiorapanelo.com.donation.services.ImageService;
import fabiorapanelo.com.donation.services.TicketService;
import fabiorapanelo.com.donation.services.UserService;

/**
 * Created by fabio on 16/09/2017.
 */

public class BaseFragment extends Fragment {

    protected AppCompatActivity mActivity;
    protected UserDao userDao;
    protected User user;
    protected CampaignService campaignService;
    protected ImageService imageService;
    protected UserService userService;
    protected TicketService ticketService;
    protected CacheManager cacheManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (AppCompatActivity) this.getActivity();
        userDao = new UserDao(mActivity);
        user = userDao.find();

        campaignService = CampaignService.getInstance();
        imageService = ImageService.getInstance();
        userService = UserService.getInstance();
        ticketService = TicketService.getInstance();
        cacheManager = CacheManager.getInstance();


    }

}
