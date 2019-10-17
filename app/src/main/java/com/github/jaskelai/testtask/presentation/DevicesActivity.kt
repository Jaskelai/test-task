package com.github.jaskelai.testtask.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.RemoteException
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import com.github.jaskelai.testtask.R
import com.github.jaskelai.testtask.di.DiProvider
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import org.altbeacon.beacon.BeaconConsumer
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.RangeNotifier
import org.altbeacon.beacon.Region

class DevicesActivity : AppCompatActivity(), BeaconConsumer {

    companion object {
        private const val CODE_PERMISSION_REQUEST_FINE_LOCATION = 1
        private const val BEACON_MANAGER_NAME = "random_name"
    }

    private lateinit var devicesAdapter: BleDevicesAdapter
    private lateinit var beaconManager: BeaconManager
    private lateinit var devicesViewModel: DevicesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        devicesViewModel = ViewModelProviders.of(
            this,
            DiProvider.devicesViewModelFactory
        ).get(DevicesViewModel::class.java)

        beaconManager = BeaconManager.getInstanceForApplication(this)
        beaconManager.bind(this)

        setupRecyclerView()
        subscribeToLiveData()
        dealWithPermissions()
    }

    override fun onDestroy() {
        super.onDestroy()
        beaconManager.unbind(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CODE_PERMISSION_REQUEST_FINE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchNearDevices()
                } else {
                    showPermsOnSetting()
                }
            }
        }
    }

    override fun onBeaconServiceConnect() {
        val notifier = RangeNotifier { beacons, region ->
            if (beacons.isNotEmpty()) {
                devicesViewModel.addBeacons(beacons)
            }
        }

        try {
            beaconManager.startRangingBeaconsInRegion(
                Region(BEACON_MANAGER_NAME, null, null, null)
            )
            beaconManager.addRangeNotifier(notifier)
        } catch (e: RemoteException) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView() {
        devicesAdapter = BleDevicesAdapter()

        rv_main.apply {
            adapter = devicesAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun dealWithPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionWithRationale()
        } else {
            fetchNearDevices()
        }
    }

    private fun requestPermissionWithRationale() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            Snackbar.make(
                layout_main,
                R.string.location_perm_reason, Snackbar.LENGTH_INDEFINITE
            )
                .setAction(getString(R.string.grant)) { requestPerms() }
                .show()
        } else {
            requestPerms()
        }
    }

    private fun requestPerms() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                CODE_PERMISSION_REQUEST_FINE_LOCATION
            )
        }
    }

    private fun showPermsOnSetting() {
        Snackbar.make(
            layout_main,
            R.string.perm_not_granted,
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(getString(R.string.settings)) { openApplicationSettings() }
            .show()
    }

    private fun openApplicationSettings() {
        val appSettingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:" + this.packageName)
        )
        startActivity(appSettingsIntent)
    }

    private fun fetchNearDevices() {
        devicesViewModel.fetchBleDevices()
    }

    private fun subscribeToLiveData() {
        devicesViewModel.devicesLiveData.observe(this, Observer {
            devicesAdapter.submitList(it.values.toList())
        })
    }
}
