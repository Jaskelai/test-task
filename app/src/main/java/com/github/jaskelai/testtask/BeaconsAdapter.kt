package com.github.jaskelai.testtask

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.altbeacon.beacon.Beacon

class BeaconsAdapter :
    ListAdapter<Beacon, BeaconsAdapter.BeaconViewHolder>(BeaconsDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeaconViewHolder {
        return BeaconViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.beacons_list_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BeaconViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class BeaconViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(beacon: Beacon) {
            
        }
    }
}

class BeaconsDiffUtilCallback : DiffUtil.ItemCallback<Beacon>() {

    override fun areItemsTheSame(oldItem: Beacon, newItem: Beacon): Boolean {
        return oldItem.id1 == newItem.id1
    }

    override fun areContentsTheSame(oldItem: Beacon, newItem: Beacon): Boolean {
        return (oldItem.id1 == newItem.id1) && (oldItem.id2 == newItem.id2)
                && (oldItem.id3 == newItem.id3) && (oldItem.rssi == newItem.rssi)
    }

}
