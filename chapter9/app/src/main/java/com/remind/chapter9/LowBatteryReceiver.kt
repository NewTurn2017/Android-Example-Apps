package com.remind.chapter9

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class LowBatteryReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("LowBatteryReceiver", "onReceive: ${intent.action}")
        when (intent.action) {
            Intent.ACTION_BATTERY_LOW -> {
                Toast.makeText(context, "배터리가 부족해요~", Toast.LENGTH_SHORT).show()
            }

        }
    }
}