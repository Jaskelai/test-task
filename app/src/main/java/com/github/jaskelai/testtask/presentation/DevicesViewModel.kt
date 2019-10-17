package com.github.jaskelai.testtask.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.jaskelai.testtask.domain.BleDevice
import com.github.jaskelai.testtask.domain.DevicesInteractor
import com.github.jaskelai.testtask.utils.mapBeaconToBleDevice
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import org.altbeacon.beacon.Beacon

class DevicesViewModel(
    private val devicesInteractor: DevicesInteractor
) : ViewModel() {

    val devicesLiveData = MutableLiveData<MutableMap<String, BleDevice>>()

    private val disposables = CompositeDisposable()

    init {
        devicesLiveData.value = HashMap()
    }

    fun fetchBleDevices() {
        disposables.add(
            devicesInteractor.observeBleDevices()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    devicesLiveData.value?.put(it.mac, it)
                    devicesLiveData.value = devicesLiveData.value
                }, {
                    it.printStackTrace()
                })
        )
    }

    fun addBeacons(beacons: Collection<Beacon>) {
        beacons.map {
            val bleDevice = it.mapBeaconToBleDevice()
            devicesLiveData.value?.put(bleDevice.mac, bleDevice)
            devicesLiveData.value = devicesLiveData.value
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (!disposables.isDisposed) disposables.dispose()
    }
}
