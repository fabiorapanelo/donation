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
import fabiorapanelo.com.donation.activity.CreatePartnerActivity
import fabiorapanelo.com.donation.adapter.PartnerListAdapter
import fabiorapanelo.com.donation.model.Partner
import fabiorapanelo.com.donation.services.PartnerService
import kotlinx.android.synthetic.main.fragment_manage_partner.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

class ManagePartnerFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_manage_partner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val layoutManager = LinearLayoutManager(this.context)
        recycler_view_partners.layoutManager = layoutManager

        btn_create_campaign.setOnClickListener { view -> createPartner(view) }

        this.findMyPartners()
    }

    private fun findMyPartners() {

        val cacheObject = cacheManager.get(PartnerService.CACHE_KEY_PARTNER_SERVICE_FIND_MY_PARTNERS)
        if (cacheObject != null) {

            val partners = cacheObject as List<Partner>
            val adapter = PartnerListAdapter(this.activity, partners, null)
            recycler_view_partners.adapter = adapter

        } else {
            partnerService.searchByUser(user.id!!.toString(), object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {

                        try {

                            val body = response.body()!!.string()
                            val listType = object : TypeToken<ArrayList<Partner>>() {

                            }.type
                            val partners = Gson().fromJson<List<Partner>>(body, listType)

                            cacheManager.put(PartnerService.CACHE_KEY_PARTNER_SERVICE_FIND_MY_PARTNERS, partners,
                                    PartnerService.CACHE_TIMEOUT_PARTNER_SERVICE_FIND)

                            val adapter = PartnerListAdapter(
                                    this@ManagePartnerFragment.activity, partners, null)
                            recycler_view_partners.adapter = adapter

                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                    } else {
                        Toast.makeText(this@ManagePartnerFragment.activity, "Falha ao cadastrar parceiro!", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@ManagePartnerFragment.activity, "Falha ao recuperar seus parceiros!", Toast.LENGTH_LONG).show()
                }
            })
        }

    }

    protected fun createPartner(view: View) {
        val intent = Intent(this.activity, CreatePartnerActivity::class.java)
        startActivityForResult(intent, ManagePartnerFragment.REQUEST_CODE_CREATE_PARTNER)
    }

    companion object Factory {

        val REQUEST_CODE_CREATE_PARTNER = 9003

        fun newInstance(): ManagePartnerFragment {
            return ManagePartnerFragment()
        }
    }

}
