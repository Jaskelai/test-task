package com.github.jaskelai.testtask

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.net.Uri
import android.os.RemoteException
import android.provider.Settings
import android.widget.Toast
import org.altbeacon.beacon.BeaconConsumer
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.RangeNotifier
import org.altbeacon.beacon.Region

class MainActivity : AppCompatActivity(), BeaconConsumer {

    companion object {
        private const val CODE_PERMISSION_REQUEST_FINE_LOCATION = 1
        private const val BEACON_MANAGER_NAME = "random_name"
    }

    private lateinit var beaconManager: BeaconManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                    fetchLocations()
                } else {
                    showPermsOnSetting()
                }
            }
        }
    }

    override fun onBeaconServiceConnect() {
        val notifier = RangeNotifier { beacons, region ->
            if (beacons.isNotEmpty()) {

            } else {
                Toast.makeText(this, R.string.no_beacons, Toast.LENGTH_SHORT).show()
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

    private fun dealWithPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionWithRationale()
        } else {
            fetchLocations()
        }
    }

    private fun requestPermissionWithRationale() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            Snackbar.make(layout_main, R.string.location_perm_reason, Snackbar.LENGTH_INDEFINITE)
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

    private fun fetchLocations() {
        beaconManager = BeaconManager.getInstanceForApplication(this)
        beaconManager.bind(this)
    }

}
