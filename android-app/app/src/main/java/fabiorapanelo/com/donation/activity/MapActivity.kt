package fabiorapanelo.com.donation.activity

import android.location.Location
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import fabiorapanelo.com.donation.R
import fabiorapanelo.com.donation.model.Campaign
import fabiorapanelo.com.donation.model.GeoPointLocation
import fabiorapanelo.com.donation.model.Partner
import java.io.Serializable
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.model.CameraPosition


/**
 * Created by fabio on 31/01/2018.
 */

class MapActivity: BaseActivity(), OnMapReadyCallback {

    var items: Serializable? = null
    var lastLocation: Location? = null
    var itemType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        setupToolbar()

        items = intent.getSerializableExtra("items");
        lastLocation = intent.getParcelableExtra("lastLocation") as Location
        itemType = intent.getStringExtra("itemType")

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this);
    }

    override fun onMapReady(googleMap: GoogleMap) {

        if(itemType == HomeActivity.TAG_CAMPAIGN){
            val campaigns = items as List<Campaign>

            for(campaign in campaigns){
                if(campaign?.location?.coordinates != null){
                    val latlng = LatLng(campaign!!.location!!.coordinates[1], campaign!!.location!!.coordinates[0])
                    googleMap.addMarker(MarkerOptions().position(latlng).title(campaign.name));
                }
            }

        } else if(itemType == HomeActivity.TAG_PARTNER){
            val partners = items as List<Partner>
            for(partner in partners){
                if(partner?.location?.coordinates != null){
                    val latlng = LatLng(partner!!.location!!.coordinates[1], partner!!.location!!.coordinates[0])
                    googleMap.addMarker(MarkerOptions().position(latlng).title(partner.name));
                }
            }
        }

        if(lastLocation != null){
            val latlng = LatLng(lastLocation!!.latitude, lastLocation!!.longitude)
            val cameraPosition = CameraPosition.Builder()
                    .target(latlng)
                    .zoom(12f)
                    .build()
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }
}
