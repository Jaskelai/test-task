package com.github.jaskelai.testtask.data

import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import com.github.jaskelai.testtask.domain.BleDevice
import com.github.jaskelai.testtask.utils.mapScanResultToBleDevice
import io.reactivex.Observable

class BleDevicesRepository(private val bluetoothLeScanner: BluetoothLeScanner?) {

    fun observeBleDevices(): Observable<BleDevice> {
        return Observable.create { emitter ->
            val scanCallback = object : ScanCallback() {

                override fun onScanResult(callbackType: Int, result: ScanResult?) {
                    result?.let {
                        emitter.onNext(it.mapScanResultToBleDevice())
                    }
                }

                override fun onScanFailed(errorCode: Int) {
                    emitter.onError(Throwable(errorCode.toString()))
                }
            }

            bluetoothLeScanner?.startScan(scanCallback)

            emitter.setCancellable {
                bluetoothLeScanner?.stopScan(scanCallback)
            }
        }
    }
}
