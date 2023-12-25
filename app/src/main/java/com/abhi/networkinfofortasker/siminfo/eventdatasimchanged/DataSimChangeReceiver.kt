package com.abhi.networkinfofortasker.siminfo.eventdatasimchanged

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SubscriptionManager
import android.util.Log
import com.abhi.networkinfofortasker.siminfo.SimInfoQuery
import com.joaomgcd.taskerpluginlibrary.extensions.requestQuery

class DataConnectionReceiver : BroadcastReceiver() {

    @SuppressLint("InlinedApi")
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == SubscriptionManager.ACTION_DEFAULT_SUBSCRIPTION_CHANGED) {
            val simInfo = SimInfoQuery().getDataSimOperator(context)
            Log.d(
                javaClass.simpleName,
                "onReceive: ${
                    intent.getStringExtra(SubscriptionManager.ACTION_DEFAULT_SUBSCRIPTION_CHANGED)
                        .toString()
                }"
            )
            DataSimChangedConfig::class.java.requestQuery(context, simInfo)
        }
    }
}