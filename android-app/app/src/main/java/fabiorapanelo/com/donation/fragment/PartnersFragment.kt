package fabiorapanelo.com.donation.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fabiorapanelo.com.donation.R
import fabiorapanelo.com.donation.adapter.PartnerListAdapter
import fabiorapanelo.com.donation.model.Campaign
import fabiorapanelo.com.donation.model.Partner
import fabiorapanelo.com.donation.services.PartnerService
import fabiorapanelo.com.donation.utils.PermissionUtils
import kotlinx.android.synthetic.main.fragment_partners.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

//https://www.learn2crack.com/2016/02/image-loading-recyclerview-picasso.html
class PartnersFragment : BaseFragment() {

    private var mFusedLocationClient: FusedLocationProviderClient? = null

    protected var distanceInKM: Int = 0

    var lastLocation: Location? = null
    var partners: List<Partner>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_partners, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val layoutManager = LinearLayoutManager(this.context)
        recycler_view_partners.layoutManager = layoutManager
        this.enableMyLocation()
    }

    private fun findPartners(location: Location?) {

        lastLocation = location

        val partnersFromCache = cacheManager.get(PartnerService.Factory.CACHE_KEY_PARTNER_SERVICE_FIND + distanceInKM)
        if (partnersFromCache != null) {

            partners = partnersFromCache as List<Partner>

            val adapter = PartnerListAdapter(this.activity, partners, location)
            recycler_view_partners.adapter = adapter

        } else {

            partnerService.nearLocation(location!!.latitude, location.longitude, (distanceInKM * 1000).toDouble(), object : Callback<ResponseBody> {

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {

                        try {

                            val body = response.body()!!.string()
                            val listType = object : TypeToken<ArrayList<Partner>>() {

                            }.type
                            partners = Gson().fromJson<List<Partner>>(body, listType)

                            cacheManager.put(PartnerService.Factory.CACHE_KEY_PARTNER_SERVICE_FIND + distanceInKM,
                                    partners, PartnerService.CACHE_TIMEOUT_PARTNER_SERVICE_FIND)
                            val adapter = PartnerListAdapter(
                                    this@PartnersFragment.activity, partners, location)
                            recycler_view_partners.adapter = adapter

                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                    } else {
                        Toast.makeText(this@PartnersFragment.activity, "Falha ao recuperar os parceiros!", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@PartnersFragment.activity, "Falha ao recuperar os parceiros!", Toast.LENGTH_LONG).show()
                }
            })

        }

    }

    private fun enableMyLocation() {

        val activity = this.activity as AppCompatActivity
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(activity, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true)
        } else {


            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
            mFusedLocationClient!!.lastLocation.addOnSuccessListener(activity) { location ->
                if (location != null) {
                    findPartners(location)
                }
            }

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation()
        } else {
            // Display the missing permission error dialog when the fragments resume.
            showMissingPermissionError()
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private fun showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog.newInstance(true).show(fragmentManager, "dialog")
    }

    companion object Factory {

        private val LOCATION_PERMISSION_REQUEST_CODE = 1

        fun newInstance(distanceInKM: Int): PartnersFragment {
            val fragment = PartnersFragment()
            fragment.distanceInKM = distanceInKM
            return fragment
        }
    }
}
