package fabiorapanelo.com.donation.activity

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fabiorapanelo.com.donation.R
import fabiorapanelo.com.donation.adapter.CampaignListAdapter
import fabiorapanelo.com.donation.adapter.PartnerListAdapter
import fabiorapanelo.com.donation.model.Campaign
import fabiorapanelo.com.donation.model.Partner
import kotlinx.android.synthetic.main.activity_search_campaign.*
import okhttp3.ResponseBody
import org.apache.commons.lang3.StringUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*


/**
 * Created by fabio on 30/01/2018.
 */

class SearchActivity : BaseActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_campaign)

        setupToolbar()

        val layoutManager = LinearLayoutManager(this)
        recycler_view_search.layoutManager = layoutManager

        // Get the intent, verify the action and get the query
        val intent = intent
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            val bundle = intent.getBundleExtra(SearchManager.APP_DATA)
            var searchType = bundle.getString(SearchActivity.SEARCH_TYPE);

            if(searchType == SearchActivity.SEARCH_CAMPAIGN){
                this.searchCampaigns(query)
            } else if(searchType == SearchActivity.SEARCH_PARTNER){
                this.searchPartners(query)
            }
        }
    }

    protected fun searchCampaigns(query: String) {

        if (StringUtils.isEmpty(query)) {
            return
        }

        campaignService.searchBySimilarName(query, 10, object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {

                    try {

                        val body = response.body()!!.string()
                        val listType = object : TypeToken<ArrayList<Campaign>>() {

                        }.type
                        val campaigns = Gson().fromJson<List<Campaign>>(body, listType)

                        val adapter = CampaignListAdapter(
                                this@SearchActivity, campaigns, null)
                        recycler_view_search.adapter = adapter

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                } else {
                    Toast.makeText(this@SearchActivity, "Falha ao recuperar as campanhas!", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@SearchActivity, "Falha ao recuperar as campanhas!", Toast.LENGTH_LONG).show()
            }
        })
    }

    protected fun searchPartners(query: String) {

        if (StringUtils.isEmpty(query)) {
            return
        }

        partnerService.searchBySimilarName(query, 10, object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {

                    try {

                        val body = response.body()!!.string()
                        val listType = object : TypeToken<ArrayList<Partner>>() {

                        }.type
                        val partners = Gson().fromJson<List<Partner>>(body, listType)

                        val adapter = PartnerListAdapter(
                                this@SearchActivity, partners, null)
                        recycler_view_search.adapter = adapter

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                } else {
                    Toast.makeText(this@SearchActivity, "Falha ao recuperar os parceiros!", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@SearchActivity, "Falha ao recuperar os parceiros!", Toast.LENGTH_LONG).show()
            }
        })
    }

    companion object Factory {
        val SEARCH_TYPE = "SEARCH_TYPE"
        val SEARCH_CAMPAIGN = "SEARCH_CAMPAIGN"
        val SEARCH_PARTNER = "SEARCH_PARTNER"
    }
}
