package com.abhi.networkinfofortasker.wifi.actionconnectnewwifi

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSpecifier
import android.os.Build
import android.util.Log


class WifiHelper {
    private val TAG: String? = javaClass.simpleName

    private fun getWiFiManager(context: Context): WifiManager {
        return context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    fun connectNewWiFi(context: Context, ssid: String, password: String?) {
        val wifiManager = getWiFiManager(context)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            connectByOldMethod(wifiManager, ssid, password)
        } else {
            try {
                val wifiNetworkSpecifier = WifiNetworkSpecifier
                    .Builder()
                    .setSsid(ssid)
                    .setWpa2Passphrase(password!!) //what if it's null?
                    .build()
                val networkRequest = NetworkRequest
                    .Builder()
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .setNetworkSpecifier(wifiNetworkSpecifier)
                    .build()
                val connectivityManager =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

                val networkCallback = object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        super.onAvailable(network)
                        Log.d(TAG, "onAvailable: ")
                        connectivityManager.bindProcessToNetwork(network)
//                        connectivityManager.bindProcessToNetwork(null)
                    }

                    override fun onUnavailable() {
                        super.onUnavailable()
                        Log.d(TAG, "onUnavailable: ")
                        connectivityManager.unregisterNetworkCallback(this)
                    }

                }
                connectivityManager.requestNetwork(networkRequest, networkCallback)
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
        }
    }

    private fun connectByOldMethod(
        wifiManager: WifiManager,
        ssid: String,
        password: String?
    ) {
        try {
            val wifiConfig = WifiConfiguration()
            wifiConfig.SSID = "\"" + ssid + "\""
            wifiConfig.preSharedKey = "\"" + password + "\""
            val netId = wifiManager.addNetwork(wifiConfig)
            wifiManager.disconnect();
            wifiManager.enableNetwork(netId, true)
            wifiManager.reconnect();
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}