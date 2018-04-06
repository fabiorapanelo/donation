package fabiorapanelo.com.donation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fabiorapanelo.com.donation.R
import fabiorapanelo.com.donation.model.UserInfo
import kotlinx.android.synthetic.main.fragment_user_information.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserInformationFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_user_information, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.getBalance()
    }

    companion object Factory {

        fun newInstance(): UserInformationFragment {
            return UserInformationFragment()
        }
    }

    protected fun getBalance() {

        progress_bar.visibility = View.VISIBLE
        progress_bar_number_donations.visibility = View.VISIBLE

        userService.getBalance(user, object : Callback<UserInfo> {
            override fun onResponse(call: Call<UserInfo>, response: Response<UserInfo>) {
                progress_bar.visibility = View.INVISIBLE
                progress_bar_number_donations.visibility = View.INVISIBLE

                if (response.isSuccessful) {
                    try {
                        val balance = response.body()!!.balance
                        text_view_user_balance.text = resources.getQuantityString(R.plurals.text_user_balance, balance!!, balance)
                        val numberDonations = response.body()!!.numberDonations
                        text_view_number_donations.text = numberDonations.toString()

                    } catch (e: Exception) {
                        text_view_user_balance.text = resources.getString(R.string.text_user_balance_unavailable)
                        text_view_number_donations.text = resources.getString(R.string.text_user_balance_unavailable)
                    }
                } else {
                    text_view_user_balance.text = resources.getString(R.string.text_user_balance_unavailable)
                    text_view_number_donations.text = resources.getString(R.string.text_user_balance_unavailable)
                }
            }

            override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                progress_bar.visibility = View.INVISIBLE
                progress_bar_number_donations.visibility = View.INVISIBLE
                text_view_user_balance.text = resources.getString(R.string.text_user_balance_unavailable)
                text_view_number_donations.text = resources.getString(R.string.text_user_balance_unavailable)
            }
        })
    }
}
