package fabiorapanelo.com.donation.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.Toast
import fabiorapanelo.com.donation.R

/**
 * Created by fabio on 15/07/2017.
 */

class RoleSelectorAdapter(private val activity: Activity, private val roles: Array<String>) : RecyclerView.Adapter<RoleSelectorAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RoleSelectorAdapter.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_role, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val role = roles[i]
        viewHolder.switch.text = role
    }

    override fun getItemCount(): Int {
        return roles.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var switch: Switch

        init {
            switch = view.findViewById(R.id.switch_role)
        }
    }


}
