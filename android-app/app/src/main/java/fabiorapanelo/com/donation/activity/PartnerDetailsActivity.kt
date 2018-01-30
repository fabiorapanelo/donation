package fabiorapanelo.com.donation.activity

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast

import java.util.Date

import butterknife.Bind
import butterknife.ButterKnife
import fabiorapanelo.com.donation.R
import fabiorapanelo.com.donation.adapter.ImagePageAdapter
import fabiorapanelo.com.donation.model.Campaign
import fabiorapanelo.com.donation.model.Log
import fabiorapanelo.com.donation.model.Partner
import kotlinx.android.synthetic.main.activity_partner_details.*
import me.relex.circleindicator.CircleIndicator
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by fabio on 24/09/2017.
 */

class PartnerDetailsActivity : BaseActivity() {

    protected lateinit var partner: Partner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        partner = intent.extras!!.get("partner") as Partner

        setContentView(R.layout.activity_partner_details)

        this.setupToolbar()

        text_partner_name.text = partner.name
        view_pager_partner_image.adapter = ImagePageAdapter(this, partner.images)
        indicator.setViewPager(view_pager_partner_image)
    }
}
