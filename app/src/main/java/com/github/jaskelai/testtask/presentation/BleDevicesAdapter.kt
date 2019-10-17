package com.github.jaskelai.testtask.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.github.jaskelai.testtask.domain.BleDevice
import com.github.jaskelai.testtask.R
import kotlinx.android.synthetic.main.beacons_list_item.view.*
import kotlinx.android.synthetic.main.simple_device_list_item.view.*

class BleDevicesAdapter :
    ListAdapter<BleDevice, BaseViewHolder<BleDevice>>(
        BleDevicesDiffUtilCallback()
    ) {

    companion object {
        private const val TYPE_BEACON = 1
        private const val TYPE_SIMPLE = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<BleDevice> {
        return if (viewType == TYPE_BEACON) {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.beacons_list_item, parent, false)
            BeaconViewHolder(itemView)
        } else {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.simple_device_list_item, parent, false)
            SimpleDeviceViewHolder(itemView)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).distance != null || getItem(position).major != null ||
            getItem(position).minor != null
        ) {
            TYPE_BEACON
        } else {
            TYPE_SIMPLE
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<BleDevice>, position: Int) {
        holder.bind(getItem(position))
    }

    inner class BeaconViewHolder(itemView: View) : BaseViewHolder<BleDevice>(itemView) {

        override fun bind(item: BleDevice) {
            itemView.apply {
                tv_beacon_mac_value.text = item.mac
                tv_beacon_rssi_value.text = item.rssi.toString()
                tv_beacon_major_value.text = item.major.toString()
                tv_beacon_minor_value.text = item.minor.toString()
                tv_beacon_distance.text = item.distance.toString()
            }
        }
    }

    inner class SimpleDeviceViewHolder(itemView: View) : BaseViewHolder<BleDevice>(itemView) {

        override fun bind(item: BleDevice) {
            itemView.apply {
                tv_simple_mac_value.text = item.mac
                tv_simple_rssi_value.text = item.rssi.toString()
            }
        }
    }
}

class BleDevicesDiffUtilCallback : DiffUtil.ItemCallback<BleDevice>() {

    override fun areItemsTheSame(oldItem: BleDevice, newItem: BleDevice): Boolean {
        return oldItem.mac == newItem.mac
    }

    override fun areContentsTheSame(oldItem: BleDevice, newItem: BleDevice): Boolean {
        return oldItem == newItem
    }

}
