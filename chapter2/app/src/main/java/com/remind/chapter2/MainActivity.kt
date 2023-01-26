package com.remind.chapter2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    var count = 0
    lateinit var status: TextView
    lateinit var resetButton: Button
    lateinit var increaseButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        status = findViewById(R.id.tvStatus)
        resetButton = findViewById(R.id.btnReset)
        increaseButton = findViewById(R.id.btnIncrease)

        resetButton.setOnClickListener {
            count = 0
            status.text = count.toString()
        }

        increaseButton.setOnClickListener {
            count++
            status.text = count.toString()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        count = savedInstanceState.getInt("count")
        status.text = count.toString()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("count", count)
    }


}