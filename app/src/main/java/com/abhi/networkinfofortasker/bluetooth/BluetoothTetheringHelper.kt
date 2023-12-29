package com.abhi.networkinfofortasker.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.util.Log
import com.abhi.networkinfofortasker.utils.PermissionHelper
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

class BluetoothTetheringHelper(private val context: Context) {
    private val TAG = javaClass.simpleName
    private var instance: Any? = null
    private var setTetheringOn: Method? = null
    private var isTetheringOn: Method? = null
    private val mutex = Any()

    @SuppressLint("PrivateApi")
    fun toggleTethering(shouldTurnOn: Boolean) {
        BluetoothAdapter.getDefaultAdapter()
        runCatching {
            val classBluetoothPan = Class.forName("android.bluetooth.BluetoothPan")
            setTetheringOn =
                classBluetoothPan.getDeclaredMethod("setBluetoothTethering", Boolean::class.java)
            isTetheringOn = classBluetoothPan.getDeclaredMethod("isTetheringOn")
            val btPanConstructor = classBluetoothPan.getDeclaredConstructor(
                Context::class.java,
                BluetoothProfile.ServiceListener::class.java
            )
            btPanConstructor?.isAccessible = true
            instance = btPanConstructor?.newInstance(context, BTPanServiceListener(shouldTurnOn))
        }.onFailure {
            Log.e(TAG, "toggleTethering: Error on accessing reflection methods", it)
        }
    }

    inner class BTPanServiceListener(private val shouldTurnOn: Boolean) :
        BluetoothProfile.ServiceListener {
        override fun onServiceConnected(profile: Int, proxy: BluetoothProfile) {
            Log.i(TAG, "BTPan proxy connected");
            try {
                synchronized(mutex) {
                    setTetheringOn?.invoke(instance, shouldTurnOn)
                    val isOn = isTetheringOn?.invoke(instance) as? Boolean ?: false
                    Log.d(TAG, "onServiceConnected: isTetheringOn $isOn")
                }
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        }

        override fun onServiceDisconnected(profile: Int) {}
    }

    companion object {
        val requiredPermissions = listOf(
            PermissionHelper.Permission.WRITE_SETTINGS
        )
    }
}

