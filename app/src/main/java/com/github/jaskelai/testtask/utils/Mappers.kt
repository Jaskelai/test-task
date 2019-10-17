package com.github.jaskelai.testtask.utils

import android.bluetooth.le.ScanResult
import com.github.jaskelai.testtask.domain.BleDevice
import org.altbeacon.beacon.Beacon

fun Beacon.mapBeaconToBleDevice(): BleDevice {
    return BleDevice(
        id1.toHexString(),
        rssi,
        id2.toInt(),
        id3.toInt(),
        distance
    )
}

fun ScanResult.mapScanResultToBleDevice(): BleDevice {
    return BleDevice(
        device.address,
        rssi
    )
}
