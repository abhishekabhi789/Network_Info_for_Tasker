package com.abhi.networkinfofortasker.wifi.actionaddorremovewifi

import android.content.Context
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.util.Log


class WifiHelper {
    private val TAG: String? = javaClass.simpleName

    private fun getWiFiManager(context: Context): WifiManager {
        return context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    fun addOrRemoveNetwork(
        context: Context,
        ssid: String,
        password: String?,
        shouldRemove: Boolean
    ): ActionStatus {
        val wifiManager = getWiFiManager(context)
        if (!wifiManager.isWifiEnabled) {
            wifiManager.setWifiEnabled(true)
            Log.d(TAG, "addOrRemoveNetwork: Turning on wifi")
            val startTime = System.currentTimeMillis()
            val maxWaitTime = 10000L
            while (!wifiManager.isWifiEnabled) {
                if (System.currentTimeMillis() - startTime > maxWaitTime) {
                    Log.e(TAG, "addOrRemoveNetwork: WiFi didn't enable in time.")
                    return ActionStatus.WIFI_DISABLED
                }
                Thread.sleep(100)
            }
        }
        return connectByOldMethod(wifiManager, ssid, password, shouldRemove)
    }


    /**Since the app now targets old sdk version, this new method is not needed.*//*
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun connectQAndAbove(
        wifiManager: WifiManager,
        ssid: String,
        password: String?,
        shouldRemove: Boolean
    ): ActionStatus {
        if (!wifiManager.isWifiEnabled) {
            return ActionStatus.WIFI_DISABLED
        }
        val suggestion = WifiNetworkSuggestion.Builder()
            .setSsid(ssid)
            .setWpa2Passphrase(password!!)
            .build()
        val suggestionList = listOf(suggestion)
        val status = if (shouldRemove) {
            wifiManager.removeNetworkSuggestions(suggestionList)
        } else {
            wifiManager.addNetworkSuggestions(suggestionList)
        }
        return when (status) {
            WifiManager.STATUS_NETWORK_SUGGESTIONS_SUCCESS -> ActionStatus.SUCCESS
            WifiManager.STATUS_NETWORK_SUGGESTIONS_ERROR_REMOVE_INVALID -> ActionStatus.SUCCESS
            else -> {
                Log.d(TAG, "connectQAndAbove: status: $status")
                ActionStatus.FAILED
            }
        }
    }*/

    private fun connectByOldMethod(
        wifiManager: WifiManager, ssid: String, password: String?, shouldRemove: Boolean
    ): ActionStatus {
        var status = ActionStatus.UNKNOWN
        try {
            val wifiConfig = WifiConfiguration()
            wifiConfig.SSID = "\"" + ssid + "\""
            if (password == null) {
                wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE)
            } else
                wifiConfig.preSharedKey = "\"" + password + "\""
            val netId = wifiManager.addNetwork(wifiConfig)
            //This method will add the network to retrieve the netId inorder to remove it.
            //Because wifiManager.configuredNetworks returns nothing, may be due to context issue
            status = if (shouldRemove) {
                val isRemoved = wifiManager.removeNetwork(netId)
                if (isRemoved) ActionStatus.SUCCESS else ActionStatus.FAILED
            } else {
                wifiManager.disconnect()
                wifiManager.enableNetwork(netId, true)
                val isReconnected = wifiManager.reconnect()
                if (isReconnected) ActionStatus.SUCCESS else ActionStatus.FAILED
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return status
    }

    enum class ActionStatus(val status: String) {
        SUCCESS("action success"),
        FAILED("action failed"),
        WIFI_DISABLED("wifi is disabled"),
        UNKNOWN("unknown")
    }
}