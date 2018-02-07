package fabiorapanelo.com.donation.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.Toast
import fabiorapanelo.com.donation.R
import android.widget.CompoundButton
import android.widget.TextView
import fabiorapanelo.com.donation.model.RoleHolder
import org.apache.commons.lang3.mutable.Mutable


/**
 * Created by fabio on 15/07/2017.
 */

class RoleSelectorAdapter(private val activity: Activity, private val roles: MutableList<RoleHolder>) : RecyclerView.Adapter<RoleSelectorAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RoleSelectorAdapter.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_role, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val role = roles[i]
        viewHolder.role_name.text = role.name
        viewHolder.switch.isChecked = role.granted
        viewHolder.switch.setOnCheckedChangeListener { buttonView, isChecked ->
            role.granted = isChecked
        }
    }

    override fun getItemCount(): Int {
        return roles.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var switch: Switch
        internal var role_name: TextView

        init {
            switch = view.findViewById(R.id.switch_role)
            role_name = view.findViewById(R.id.text_view_role_name)
        }
    }


}
