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
import kotlinx.android.synthetic.main.activity_campaign_details.*
import me.relex.circleindicator.CircleIndicator
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by fabio on 24/09/2017.
 */

class CampaignDetailsActivity : BaseActivity() {

    protected var campaign: Campaign? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        campaign = intent.extras!!.get("campaign") as Campaign

        setContentView(R.layout.activity_campaign_details)

        ButterKnife.bind(this)

        this.setupToolbar()

        text_campaign_name!!.text = campaign!!.name
        view_pager_campaign_image.adapter = ImagePageAdapter(this, campaign!!.images)
        indicator.setViewPager(view_pager_campaign_image)

        btn_donate.setOnClickListener { view -> donate(view) }

        seek_bar_donation_value.progress = DONATION_START_VALUE
        setDonationText(DONATION_START_VALUE)

        seek_bar_donation_value.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {}

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                setDonationText(seekBar.progress)
            }
        })

        this.getBalance()
    }

    protected fun donate(view: View) {

        btn_donate.isEnabled = false
        progress_bar.visibility = View.VISIBLE

        val donationValue = seek_bar_donation_value.progress

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
                    donate_message_layout.visibility = View.VISIBLE
                    donate_button_layout.visibility = View.GONE
                } else {
                    Toast.makeText(this@CampaignDetailsActivity, "Falha ao realizar doação!", Toast.LENGTH_LONG).show()
                }
                btn_donate.isEnabled = true
                progress_bar.visibility = View.INVISIBLE
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                btn_donate.isEnabled = true
                progress_bar.visibility = View.INVISIBLE
                Toast.makeText(this@CampaignDetailsActivity, "Falha ao realizar doação!", Toast.LENGTH_LONG).show()
            }
        })

    }

    protected fun setDonationText(donationValue: Int) {
        val text = resources.getQuantityString(R.plurals.button_donate_messages, donationValue, donationValue)
        btn_donate.text = text
    }

    protected fun getBalance() {

        btn_donate.isEnabled = false
        progress_bar.visibility = View.VISIBLE

        userService.getBalance(user, object : Callback<UserInfo> {
            override fun onResponse(call: Call<UserInfo>, response: Response<UserInfo>) {
                progress_bar.visibility = View.INVISIBLE
                if (response.isSuccessful) {
                    try {
                        val balance = response.body()!!.balance
                        if (balance > 0) {
                            btn_donate.isEnabled = true
                        }
                        seek_bar_donation_value.max = balance!!
                        txt_user_balance.text = resources.getQuantityString(R.plurals.text_user_balance, balance, balance)

                    } catch (e: Exception) {
                        txt_user_balance.text = resources.getString(R.string.text_user_balance_unavailable)
                    }

                } else {
                    txt_user_balance.text = resources.getString(R.string.text_user_balance_unavailable)
                }
            }

            override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                progress_bar.visibility = View.INVISIBLE
                txt_user_balance.text = resources.getString(R.string.text_user_balance_unavailable)
            }
        })
    }

    companion object {
        protected val DONATION_START_VALUE = 0
        protected val LOG_TYPE = "DONATION"
    }
}
