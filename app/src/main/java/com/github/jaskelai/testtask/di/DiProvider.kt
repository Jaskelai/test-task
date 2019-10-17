package com.github.jaskelai.testtask.di

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.BluetoothLeScanner
import com.github.jaskelai.testtask.data.BleDevicesRepository
import com.github.jaskelai.testtask.domain.DevicesInteractor
import com.github.jaskelai.testtask.utils.DevicesViewModelFactory

object DiProvider {

    val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

    val bluetoothLeScanner: BluetoothLeScanner? = bluetoothAdapter?.bluetoothLeScanner

    val BLE_DEVICES_REPOSITORY: BleDevicesRepository = BleDevicesRepository(bluetoothLeScanner)

    val devicesInteractor: DevicesInteractor = DevicesInteractor(BLE_DEVICES_REPOSITORY)

    val devicesViewModelFactory: DevicesViewModelFactory = DevicesViewModelFactory(devicesInteractor)
}
