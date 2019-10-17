package com.github.jaskelai.testtask.domain

import com.github.jaskelai.testtask.data.BleDevicesRepository
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class DevicesInteractor(private val bleDevicesRepository: BleDevicesRepository) {

    fun observeBluetooth(): Observable<Boolean> {
        return bleDevicesRepository.observeBluetooth()
    }

    fun observeBleDevices(): Observable<BleDevice> {
        return bleDevicesRepository.observeBleDevices()
            .distinct()
            .subscribeOn(Schedulers.io())
    }
}
