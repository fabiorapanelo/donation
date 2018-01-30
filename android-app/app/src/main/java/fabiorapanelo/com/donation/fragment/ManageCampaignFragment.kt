package fabiorapanelo.com.donation.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fabiorapanelo.com.donation.R
import fabiorapanelo.com.donation.activity.CreateCampaignActivity
import fabiorapanelo.com.donation.adapter.CampaignListAdapter
import fabiorapanelo.com.donation.model.Campaign
import fabiorapanelo.com.donation.services.CampaignService
import fabiorapanelo.com.donation.services.UserService
import kotlinx.android.synthetic.main.fragment_manage_campaign.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

class ManageCampaignFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_manage_campaign, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val layoutManager = LinearLayoutManager(this.context)
        recycler_view_campaigns.layoutManager = layoutManager

        btn_create_campaign.setOnClickListener { view -> createCampaign(view) }

        this.findMyCampaigns()
    }

    private fun findMyCampaigns() {

        val cacheObject = cacheManager.get(CampaignService.CACHE_KEY_CAMPAIGN_SERVICE_FIND_MY_CAMPAIGNS)
        if (cacheObject != null) {

            val campaigns = cacheObject as List<Campaign>
            val adapter = CampaignListAdapter(this.activity, campaigns, null)
            recycler_view_campaigns.adapter = adapter

        } else {
            campaignService.searchByUser(user.id!!.toString(), object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {

                        try {

                            val body = response.body()!!.string()
                            val listType = object : TypeToken<ArrayList<Campaign>>() {

                            }.type
                            val campaigns = Gson().fromJson<List<Campaign>>(body, listType)

                            cacheManager.put(CampaignService.CACHE_KEY_CAMPAIGN_SERVICE_FIND_MY_CAMPAIGNS, campaigns,
                                    CampaignService.CACHE_TIMEOUT_CAMPAING_SERVICE_FIND)

                            val adapter = CampaignListAdapter(
                                    this@ManageCampaignFragment.activity, campaigns, null)
                            recycler_view_campaigns.adapter = adapter

                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                    } else {
                        Toast.makeText(this@ManageCampaignFragment.activity, "Falha ao cadastrar campanha!", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@ManageCampaignFragment.activity, "Falha ao recuperar suas campanhas!", Toast.LENGTH_LONG).show()
                }
            })
        }

    }

    protected fun createCampaign(view: View) {
        val intent = Intent(this.activity, CreateCampaignActivity::class.java)
        startActivityForResult(intent, ManageCampaignFragment.REQUEST_CODE_CREATE_CAMPAIGN)
    }

    companion object Factory {

        val REQUEST_CODE_CREATE_CAMPAIGN = 9002

        fun newInstance(): ManageCampaignFragment {
            return ManageCampaignFragment()
        }
    }

}
