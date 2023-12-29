package com.abhi.networkinfofortasker.bluetooth.actionBluetoothTethering

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import com.abhi.networkinfofortasker.bluetooth.BluetoothTetheringHelper
import com.abhi.networkinfofortasker.databinding.ToggleBtTetheringInputBinding
import com.abhi.networkinfofortasker.utils.Tasker.saveConfig
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfig
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput

class ToggleBTTetheringConfig : Activity(), TaskerPluginConfig<ToggleBTTetherInput> {
    private lateinit var binding: ToggleBtTetheringInputBinding
    private val taskerHelper by lazy { ToggleBTTetheringHelper(this) }
    override val context: Context
        get() = applicationContext
    override val inputForTasker: TaskerInput<ToggleBTTetherInput>
        get() = TaskerInput(
            ToggleBTTetherInput(
                binding.shouldTurnOnSwitch.isChecked
            )
        )

    override fun assignFromInput(input: TaskerInput<ToggleBTTetherInput>) {
        input.regular.run {
            binding.shouldTurnOnSwitch.isChecked = shouldTurnOn
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ToggleBtTetheringInputBinding.inflate(layoutInflater)
        setContentView(binding.root)
        taskerHelper.onCreate()
        binding.saveConfigButton.setOnClickListener {
            saveConfig(this, taskerHelper, BluetoothTetheringHelper.requiredPermissions)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0) {
            saveConfig(this, taskerHelper, BluetoothTetheringHelper.requiredPermissions)
        }
        return super.onKeyDown(keyCode, event)
    }
}