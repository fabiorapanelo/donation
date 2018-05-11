package fabiorapanelo.com.donation.services

import fabiorapanelo.com.donation.model.Credentials
import fabiorapanelo.com.donation.model.Ticket
import fabiorapanelo.com.donation.model.User
import fabiorapanelo.com.donation.repositories.TicketRepository
import fabiorapanelo.com.donation.repositories.UserRepository
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by fabio on 24/10/2017.
 */

class TicketService private constructor() : ServiceBase(){

    val BASE_URL = "http://ec2-54-145-27-121.compute-1.amazonaws.com:8081/donation/"

    var ticketRepository: TicketRepository

    init {
        ticketRepository = retrofit.create(TicketRepository::class.java)
    }

    fun create(ticket: Ticket, callback: Callback<Ticket>) {
        val call = ticketRepository.create(ticket)
        call.enqueue(callback)
    }

    fun find(ticketId: String, callback: Callback<Ticket>) {
        val call = ticketRepository.find(ticketId)
        call.enqueue(callback)
    }

    fun consume(ticketId: String, userId: String, callback: Callback<ResponseBody>) {
        val call = ticketRepository.consume(ticketId, userId)
        call.enqueue(callback)
    }

    companion object Factory {
        val instance = TicketService()
    }
}
