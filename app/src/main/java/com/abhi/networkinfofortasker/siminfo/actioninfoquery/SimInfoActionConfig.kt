package com.abhi.networkinfofortasker.siminfo.actioninfoquery

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import com.abhi.networkinfofortasker.databinding.SimInfoActionInputBinding
import com.abhi.networkinfofortasker.siminfo.SimInfoQuery
import com.abhi.networkinfofortasker.utils.Tasker.saveConfig
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfig
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput

class SimInfoActionConfig : Activity(), TaskerPluginConfig<SimInfoActionInput> {
    private lateinit var binding: SimInfoActionInputBinding
    private val taskerHelper by lazy { SimInfoActionHelper(this) }
    override val context: Context get() = applicationContext
    override val inputForTasker: TaskerInput<SimInfoActionInput>
        get() = TaskerInput(
            SimInfoActionInput(
                binding.simSlot1.isChecked,
                binding.simSlot2.isChecked,
                binding.simSlotDefaultData.isChecked
            )
        )

    override fun assignFromInput(input: TaskerInput<SimInfoActionInput>) {
        input.regular.run {
            binding.simSlot1.isChecked = chooseSimOne
            binding.simSlot2.isChecked = chooseSimTwo
            binding.simSlotDefaultData.isChecked = chooseDefaultData
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SimInfoActionInputBinding.inflate(layoutInflater)
        setContentView(binding.root)
        taskerHelper.onCreate()
        binding.saveConfigButton.setOnClickListener {
            saveConfig(this, taskerHelper, SimInfoQuery.requiredPermissions)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0) {
            saveConfig(this, taskerHelper, SimInfoQuery.requiredPermissions)
        }
        return super.onKeyDown(keyCode, event)
    }
}