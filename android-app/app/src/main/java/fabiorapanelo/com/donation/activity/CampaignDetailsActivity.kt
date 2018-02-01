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
import fabiorapanelo.com.donation.model.User
import fabiorapanelo.com.donation.model.UserInfo
import me.relex.circleindicator.CircleIndicator
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by fabio on 24/09/2017.
 */

class CampaignDetailsActivity : BaseActivity() {

    @Bind(R.id.text_campaign_name)
    var textCampaignName: TextView? = null

    @Bind(R.id.view_pager_campaign_image)
    var viewPager: ViewPager? = null

    @Bind(R.id.indicator)
    var indicator: CircleIndicator? = null

    @Bind(R.id.btn_donate)
    var btnDonate: Button? = null

    @Bind(R.id.seek_bar_donation_value)
    var seekBarDonationValue: SeekBar? = null

    @Bind(R.id.progress_bar)
    internal var progressBar: ProgressBar? = null

    @Bind(R.id.donate_message_layout)
    var mDonateMessageLayout: LinearLayout? = null

    @Bind(R.id.donate_button_layout)
    var mDonateButtonLayout: LinearLayout? = null

    @Bind(R.id.txt_user_balance)
    var txtUserBalance: TextView? = null

    protected var campaign: Campaign? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        campaign = intent.extras!!.get("campaign") as Campaign

        setContentView(R.layout.activity_campaign_details)

        ButterKnife.bind(this)

        this.setupToolbar()

        textCampaignName!!.text = campaign!!.name
        viewPager!!.adapter = ImagePageAdapter(this, campaign!!.images)
        indicator!!.setViewPager(viewPager)

        btnDonate!!.setOnClickListener { view -> donate(view) }

        seekBarDonationValue!!.progress = DONATION_START_VALUE
        setDonationText(DONATION_START_VALUE)

        seekBarDonationValue!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {}

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                setDonationText(seekBar.progress)
            }
        })

        this.getBalance()
    }

    protected fun donate(view: View) {

        btnDonate!!.isEnabled = false
        progressBar!!.visibility = View.VISIBLE

        val donationValue = seekBarDonationValue!!.progress

        val donation = Log()
        donation.date = Date().time
        donation.type = LOG_TYPE
        donation.quantity = donationValue
        donation.userFrom = user.id!!.toString()
        donation.userTo = campaign!!.userId
        donation.campaign = campaign!!.id

        donationService.save(donation, object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if (response.isSuccessful) {
                    mDonateMessageLayout!!.visibility = View.VISIBLE
                    mDonateButtonLayout!!.visibility = View.GONE
                } else {
                    Toast.makeText(this@CampaignDetailsActivity, "Falha ao realizar doação!", Toast.LENGTH_LONG).show()
                }
                btnDonate!!.isEnabled = true
                progressBar!!.visibility = View.INVISIBLE
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                btnDonate!!.isEnabled = true
                progressBar!!.visibility = View.INVISIBLE
                Toast.makeText(this@CampaignDetailsActivity, "Falha ao realizar doação!", Toast.LENGTH_LONG).show()
            }
        })

    }

    protected fun setDonationText(donationValue: Int) {
        val text = resources.getQuantityString(R.plurals.button_donate_messages, donationValue, donationValue)
        btnDonate!!.text = text
    }

    protected fun getBalance() {

        btnDonate!!.isEnabled = false
        progressBar!!.visibility = View.VISIBLE

        userService.getBalance(user, object : Callback<UserInfo> {
            override fun onResponse(call: Call<UserInfo>, response: Response<UserInfo>) {
                progressBar!!.visibility = View.INVISIBLE
                if (response.isSuccessful) {
                    try {
                        val balance = response.body()!!.balance
                        if (balance > 0) {
                            btnDonate!!.isEnabled = true
                        }
                        seekBarDonationValue!!.max = balance!!
                        txtUserBalance!!.text = resources.getQuantityString(R.plurals.text_user_balance, balance, balance)

                    } catch (e: Exception) {
                        txtUserBalance!!.text = resources.getString(R.string.text_user_balance_unavailable)
                    }

                } else {
                    txtUserBalance!!.text = resources.getString(R.string.text_user_balance_unavailable)
                }
            }

            override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                progressBar!!.visibility = View.INVISIBLE
                txtUserBalance!!.text = resources.getString(R.string.text_user_balance_unavailable)
            }
        })
    }

    companion object {
        protected val DONATION_START_VALUE = 0
        protected val LOG_TYPE = "DONATION"
    }
}
