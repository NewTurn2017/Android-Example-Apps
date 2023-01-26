package com.remind.chapter3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.widget.addTextChangedListener
import com.remind.chapter3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var isCentimeter = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.etInput.addTextChangedListener(centimeterToMeterAddChangeListener)
        binding.btnUnitSwap.setOnClickListener {
            unitSwap()


        }
    }

    private val centimeterToMeterAddChangeListener: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            val inputNumber = if (s.isNullOrEmpty()) 0 else s.toString().toInt()
            val result = inputNumber.times(0.01)
            binding.tvOutput.text = result.toString()
        }
    }

    private val meterToCentimeterAddChangeListener: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            val inputNumber = if (s.isNullOrEmpty()) 0 else s.toString().toInt()
            val result = inputNumber.times(100)
            binding.tvOutput.text = result.toString()
        }
    }


    fun unitSwap() {
        if (isCentimeter) {
            binding.etInput.addTextChangedListener(centimeterToMeterAddChangeListener)
            binding.tvInputUnit.text = "m"
            binding.tvOutputUnit.text = "cm"
            centimeterToMeterAddChangeListener.afterTextChanged(binding.etInput.text)
        } else {
            binding.etInput.addTextChangedListener(meterToCentimeterAddChangeListener)
            binding.tvInputUnit.text = "cm"
            binding.tvOutputUnit.text = "m"
            meterToCentimeterAddChangeListener.afterTextChanged(binding.etInput.text)
        }
        isCentimeter = !isCentimeter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isCentimeter", isCentimeter)
        outState.putString("input", binding.etInput.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        isCentimeter = savedInstanceState.getBoolean("isCentimeter").not()
        binding.etInput.setText(savedInstanceState.getString("input"))
        unitSwap()
    }
}