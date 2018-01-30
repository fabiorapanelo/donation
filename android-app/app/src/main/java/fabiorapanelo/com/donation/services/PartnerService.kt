package fabiorapanelo.com.donation.services

import fabiorapanelo.com.donation.model.Partner
import fabiorapanelo.com.donation.repositories.PartnerRepository
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback

/**
 * Created by fabio on 29/01/2018.
 */

class PartnerService private constructor() : ServiceBase() {

    protected var partnerRepository: PartnerRepository

    init {
        partnerRepository = retrofit.create(PartnerRepository::class.java)
    }

    fun nearLocation(latitude: Double, longitude: Double,
                     distanceInMeters: Double, callback: Callback<ResponseBody>) {
        val call: Call<ResponseBody>
        call = partnerRepository.nearLocation(latitude, longitude, distanceInMeters)
        call.enqueue(callback)
    }

    fun searchByUser(userId: String, callback: Callback<ResponseBody>) {
        val call: Call<ResponseBody>
        call = partnerRepository.searchByUser(userId)
        call.enqueue(callback)
    }

    fun save(partner: Partner, callback: Callback<ResponseBody>) {

        val call: Call<ResponseBody>
        if (partner.id != null) {
            call = partnerRepository.update(partner.id.toString(), partner)
        } else {
            call = partnerRepository.create(partner)
        }

        call.enqueue(callback)

    }

    fun searchBySimilarName(partnerName: String, limit: Long, callback: Callback<ResponseBody>) {
        val call: Call<ResponseBody>
        call = partnerRepository.searchBySimilarName(partnerName, limit)
        call.enqueue(callback)
    }

    companion object Factory {

        val CACHE_KEY_PARTNER_SERVICE_FIND = "PartnerService.find"
        val CACHE_KEY_PARTNER_SERVICE_FIND_MY_PARTNERS = "PartnerService.findMyPartners"
        val CACHE_TIMEOUT_PARTNER_SERVICE_FIND = 30

        val instance = PartnerService()
    }

}
