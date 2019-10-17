package com.github.jaskelai.testtask.domain

import com.github.jaskelai.testtask.data.BleDevicesRepository
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class DevicesInteractor(private val bleDevicesRepository: BleDevicesRepository) {

    fun observeBleDevices(): Observable<BleDevice> {
        return bleDevicesRepository.observeBleDevices()
            .distinct()
            .subscribeOn(Schedulers.io())
    }
}
