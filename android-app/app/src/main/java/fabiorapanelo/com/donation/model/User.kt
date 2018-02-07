package fabiorapanelo.com.donation.model

import java.io.Serializable

/**
 * Created by fabio on 29/07/2017.
 */

class User: Serializable {

    var id: String? = null
    var name: String? = null
    var username: String? = null
    var password: String? = null
    var roles: Array<String>? = null


    fun hasPermission(role: String): Boolean{
        if(roles == null){
            return false
        }
        for(r in roles!!){
            if(r == role || r == "admin"){
                return true;
            }
        }
        return false;
    }
}
