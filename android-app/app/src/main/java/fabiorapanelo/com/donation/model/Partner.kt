package fabiorapanelo.com.donation.model

import java.io.Serializable

class Partner : Serializable {

    var id: String? = null
    var name: String? = null
    var userId: String? = null
    var location: GeoPointLocation? = null
    var images: List<String>? = null
}

