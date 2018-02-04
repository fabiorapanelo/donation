package fabiorapanelo.com.donation.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import fabiorapanelo.com.donation.R
import fabiorapanelo.com.donation.adapter.RoleSelectorAdapter
import fabiorapanelo.com.donation.model.User
import kotlinx.android.synthetic.main.fragment_user_management.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class UserManagementFragment : BaseFragment() {

    private var roles: Array<String>? = null
    var edit_user: User? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_user_management, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {

        roles = resources.getStringArray(R.array.roles)

        btn_search_user.setOnClickListener { _ ->

            val name = edit_text_username.text.toString();
            searchUser(name)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    fun searchUser(query: String){

        linear_layout_selected_user.visibility = View.INVISIBLE

        userService.findByUsername(query, object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {

                    try {
                        edit_user = response.body()!!

                        text_username.text = edit_user!!.name + " - " + edit_user!!.username
                        displayRoles(edit_user!!)

                        linear_layout_selected_user.visibility = View.VISIBLE

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                } else {
                    Toast.makeText(this@UserManagementFragment.activity, "Usuário não encontrado", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@UserManagementFragment.activity, "Falha ao buscar usuário", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun displayRoles(user: User){
        val layoutManager = LinearLayoutManager(this.activity)
        recycler_view_roles.layoutManager = layoutManager

        val adapter = RoleSelectorAdapter(this.activity, roles!!)
        recycler_view_roles.adapter = adapter

    }

    companion object {

        fun newInstance(): UserManagementFragment {
            return UserManagementFragment()
        }
    }

}
