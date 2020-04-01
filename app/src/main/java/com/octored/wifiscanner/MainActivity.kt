package com.octored.wifiscanner

import android.content.Context
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiInfo
import android.net.wifi.WifiNetworkSpecifier
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.octored.wifiscanner.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}