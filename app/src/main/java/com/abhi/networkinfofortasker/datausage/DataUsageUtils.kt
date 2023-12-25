package com.abhi.networkinfofortasker.datausage

import android.annotation.SuppressLint
import android.content.Context
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager


@SuppressLint("MissingPermission")
fun getSubscriberIdForSim(context: Context, index: Int?): String? {
    if (index == null) return null
    val subscriptionManager =
        context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
    val subInfoList = subscriptionManager.activeSubscriptionInfoList

    if (subInfoList != null && index in 0..1 && index < subInfoList.size) {
        try {
            val id = subInfoList[index].subscriptionId
            return getSubscriberIdUsingReflection(context, id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return null
}

private fun getSubscriberIdUsingReflection(context: Context, id: Int): String? {
    try {
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val c = Class.forName("android.telephony.TelephonyManager")
        val m = c.getMethod("getSubscriberId", Int::class.javaPrimitiveType)
        val o = m.invoke(telephonyManager, id)
        return o as? String
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}