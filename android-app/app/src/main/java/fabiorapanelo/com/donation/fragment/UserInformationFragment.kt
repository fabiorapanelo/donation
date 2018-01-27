package fabiorapanelo.com.donation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fabiorapanelo.com.donation.R
import kotlinx.android.synthetic.main.fragment_user_information.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserInformationFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_user_information, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        this.getBalance()
    }

    companion object Factory {

        fun newInstance(): UserInformationFragment {
            return UserInformationFragment()
        }
    }

    protected fun getBalance() {

        progress_bar.setVisibility(View.VISIBLE)

        userService.getBalance(user, object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                progress_bar.setVisibility(View.INVISIBLE)
                if (response.isSuccessful) {
                    try {
                        val balance = Integer.valueOf(response.body()!!.string())
                        text_view_user_balance.setText(resources.getQuantityString(R.plurals.text_user_balance, balance!!, balance))

                    } catch (e: Exception) {
                        text_view_user_balance.setText(resources.getString(R.string.text_user_balance_unavailable))
                    }

                } else {
                    text_view_user_balance.setText(resources.getString(R.string.text_user_balance_unavailable))
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                progress_bar.setVisibility(View.INVISIBLE)
                text_view_user_balance.setText(resources.getString(R.string.text_user_balance_unavailable))
            }
        })
    }
}
