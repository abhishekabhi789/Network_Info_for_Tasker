package com.abhi.networkinfofortasker.wifi.actionconnectnewwifi

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import com.abhi.networkinfofortasker.databinding.WifiConnectNewActionBinding
import com.abhi.networkinfofortasker.utils.Tasker.chooseVariable
import com.abhi.networkinfofortasker.utils.Tasker.saveConfig
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfig
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput

class ConnectNewWifiConfig : Activity(), TaskerPluginConfig<ConnectNewWifiInput> {
    private lateinit var binding: WifiConnectNewActionBinding
    private val taskerHelper by lazy { ConnectNewWIfiHelper(this) }
    override val context: Context
        get() = applicationContext
    override val inputForTasker: TaskerInput<ConnectNewWifiInput>
        get() = TaskerInput(
            ConnectNewWifiInput(
                binding.wifiSsidInput.text.toString(), binding.wifiPasswordInput.text.toString()
            )
        )

    override fun assignFromInput(input: TaskerInput<ConnectNewWifiInput>) {
        input.regular.run {
            binding.wifiSsidInput.setText(wifiSsid)
            binding.wifiPasswordInput.setText(wifiPassword)
            binding.saveConfigButton.setOnClickListener {
                saveConfig(this@ConnectNewWifiConfig, taskerHelper, null)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = WifiConnectNewActionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        taskerHelper.onCreate()
        binding.chooseSsidVariableButton.setOnClickListener {
            chooseVariable(
                this,
                binding.wifiSsidInput.id,
                taskerHelper
            )
        }
        binding.choosePasswordVariableButton.setOnClickListener {
            chooseVariable(
                this,
                binding.wifiPasswordInput.id,
                taskerHelper
            )
        }
        binding.saveConfigButton.setOnClickListener {
            saveConfig(this, taskerHelper,null)
        }

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0) {
            saveConfig(this, taskerHelper,null)
        }
        return super.onKeyDown(keyCode, event)
    }
}