package com.octored.wifiscanner.presentation

import android.Manifest
import android.content.*
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.octored.wifiscanner.NetworkListAdapter
import com.octored.wifiscanner.R
import com.octored.wifiscanner.extension.hasPassword
import com.octored.wifiscanner.model.AvailableNetwork
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), LoginDialog.LoggingListener {

    lateinit var manager: WifiManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), 0
        )

        manager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        manager.startScan()

        main_networks_rv.layoutManager = LinearLayoutManager(this)


        val wifiScanReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val scan = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
                if (scan) {
                    scanForNetworks()
                } else {
                    Toast.makeText(this@MainActivity, "No networks", Toast.LENGTH_LONG)
                }
            }
        }

        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        applicationContext.registerReceiver(wifiScanReceiver, intentFilter)
        scanForNetworks()

        main_networks_srl.setOnRefreshListener {
            scanForNetworks()
        }

        main_disconnect_ib.setOnClickListener {
            manager.disconnect()
        }

    }

    override fun onResume() {
        super.onResume()
        scanForNetworks()
    }

    fun scanForNetworks() {
        manager.startScan()
        val list = ArrayList<AvailableNetwork>()
        val handler = Handler()
        handler.postDelayed(Runnable {
            val results = manager.scanResults
            main_networks_rv.run {
                results.forEach {
                    list.add(AvailableNetwork(it.SSID, it.level, it.hasPassword()))
                    main_networks_srl.isRefreshing = false
                }
                main_networks_rv.adapter =
                    NetworkListAdapter(
                        list,
                        this@MainActivity::showConnectDialog
                    )
            }
        }, 1000)
    }

    fun showConnectDialog(ssid: String) {
        val dialog = LoginDialog(ssid)
        dialog.show(supportFragmentManager, "Sign in")
    }

    fun connectToNetwork(ssid: String, password: String) {
        val wifiConf = WifiConfiguration()
        wifiConf.SSID = "\"" + ssid + "\""
        wifiConf.preSharedKey = "\"" + password + "\""

        val i = manager.addNetwork(wifiConf)
        manager.disconnect()
        manager.enableNetwork(i, true)
        manager.reconnect()
    }

    override fun onSignInClick(dialog: DialogFragment, ssid: String, password: String) {
        connectToNetwork(ssid, password)
    }

}
