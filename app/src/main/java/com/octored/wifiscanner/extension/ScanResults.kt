package com.octored.wifiscanner.extension

import android.net.wifi.ScanResult

fun ScanResult.hasPassword(): Boolean {
    if (capabilities.contains("WPA")) return true
    if (capabilities.contains("WEP")) return true
    return false
}