package fabiorapanelo.com.donation.services


import fabiorapanelo.com.donation.model.Credentials
import fabiorapanelo.com.donation.model.User
import fabiorapanelo.com.donation.model.UserInfo
import fabiorapanelo.com.donation.repositories.UserRepository
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback

/**
 * Created by fabio on 29/07/2017.
 */

class UserService private constructor() : ServiceBase() {

    protected var userRepository: UserRepository

    init {
        userRepository = retrofit.create(UserRepository::class.java)
    }

    fun authenticate(credentials: Credentials, callback: Callback<User>) {
        val authenticate = userRepository.authenticate(credentials)
        authenticate.enqueue(callback)
    }

    fun findByUsername(username: String, callback: Callback<User>) {
        val findByUsername = userRepository.findByUsername(username)
        findByUsername.enqueue(callback)
    }

    fun save(user: User, callback: Callback<ResponseBody>) {

        val call: Call<ResponseBody>
        if (user.id != null) {
            call = userRepository.update(user.id!!.toString(), user)
        } else {
            call = userRepository.create(user)
        }

        call.enqueue(callback)

    }

    fun getBalance(user: User, callback: Callback<UserInfo>) {
        val call = userRepository.getBalance(user.id!!)
        call.enqueue(callback)
    }

    fun searchBySimilarName(name: String, limit: Long, callback: Callback<ResponseBody>) {
        val call: Call<ResponseBody>
        call = userRepository.searchBySimilarName(name, limit)
        call.enqueue(callback)
    }

    companion object Factory {

        val instance = UserService()

        fun getUrlForUser(user: User): String {

            return BASE_URL + "users/" + user.id

        }
    }

}
