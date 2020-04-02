package com.octored.wifiscanner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.octored.wifiscanner.model.AvailableNetwork
import kotlinx.android.synthetic.main.item_network.view.*
import java.util.zip.Inflater

class NetworkListAdapter(val networks: ArrayList<AvailableNetwork>, val connect : (ssid : String) -> Unit) : RecyclerView.Adapter<NetworkListAdapter.NetworkViewHolder>(){

    class NetworkViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val ssid = view.item_ssid_tv
        val level = view.item_level_tv
        val password = view.item_pass_iv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NetworkViewHolder {
       return NetworkViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_network, parent, false))
    }

    override fun getItemCount() = networks.size

    override fun onBindViewHolder(holder: NetworkViewHolder, position: Int) {
        holder.ssid.text = networks[position].SSID
        var level : String
        if(networks[position].level > -50) level = "Высокий"
        else if(networks[position].level < -70) level = "Низкий"
        else level = "Средний"

        holder.level.text = level + " уровень сигнала"

        if (networks[position].password){
            holder.password.visibility = View.VISIBLE
        }

        holder.itemView.setOnClickListener {
            connect(networks[position].SSID)
        }
    }
}