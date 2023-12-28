package com.abhi.networkinfofortasker.wifi.actionnewwifi

import android.content.Context
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSuggestion
import android.os.Build
import androidx.annotation.RequiresApi


class WifiHelper {
    private val TAG: String? = javaClass.simpleName

    private fun getWiFiManager(context: Context): WifiManager {
        return context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    fun connectNewWiFi(context: Context, ssid: String, password: String?): ConnectionStatus {
        val wifiManager = getWiFiManager(context)
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            connectByOldMethod(wifiManager, ssid, password)
        } else {
            if (!wifiManager.isWifiEnabled) {
                ConnectionStatus.WIFI_DISABLED
            } else {
                connectQAndAbove(wifiManager, ssid, password)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun connectQAndAbove(
        wifiManager: WifiManager,
        ssid: String,
        password: String?
    ): ConnectionStatus {
        val suggestion = WifiNetworkSuggestion.Builder()
            .setSsid(ssid)
            .setWpa2Passphrase(password!!)
            .build()
        val suggestionList = listOf(suggestion)
        val status = wifiManager.addNetworkSuggestions(suggestionList)
        return if (status != WifiManager.STATUS_NETWORK_SUGGESTIONS_SUCCESS) {
            ConnectionStatus.ERROR
        } else ConnectionStatus.ADDED
    }

    private fun connectByOldMethod(
        wifiManager: WifiManager, ssid: String, password: String?
    ): ConnectionStatus {
        var netId: Int = -1
        try {
            val wifiConfig = WifiConfiguration()
            wifiConfig.SSID = "\"" + ssid + "\""
            wifiConfig.preSharedKey = "\"" + password + "\""
            netId = wifiManager.addNetwork(wifiConfig)
            wifiManager.disconnect()
            wifiManager.enableNetwork(netId, true)
            wifiManager.reconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return if (netId > 0) ConnectionStatus.ADDED else ConnectionStatus.ERROR
    }

    enum class ConnectionStatus(val status: String) {
        ADDED("network added"),
        UNAVAILABLE("network is out of reach to add"),
        USER_CANCELED("user canceled the connection attempt"),
        WIFI_DISABLED("wifi is disabled"),
        ERROR("error while adding new network"),
        UNKNOWN("unknown")
    }
}