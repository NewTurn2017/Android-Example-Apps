package com.remind.chapter6

import android.graphics.Color
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.remind.chapter6.databinding.ActivityMainBinding
import com.remind.chapter6.databinding.DialogCountdownSettingBinding
import java.util.*
import kotlin.concurrent.timer


class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var countDownSecond = 5
    private var currentCountDownDeciSecond = countDownSecond * 10
    private var currentDeciSecond = 0
    private var timer: Timer? = null
    private var labCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.countDownTextView.setOnClickListener {
            showSecondPickerDialog()
        }

        binding.startButton.setOnClickListener {
            start()
            updateButtonIsPlay(true)
        }
        binding.stopButton.setOnClickListener {
            stopAlertDailog()

        }
        binding.pauseButton.setOnClickListener {
            pause()
            updateButtonIsPlay(false)
        }
        binding.labButton.setOnClickListener {
            lab()
        }

        initCountDownViews()
    }

    private fun initCountDownViews() {
        currentCountDownDeciSecond = countDownSecond * 10
        binding.countDownTextView.text = String.format("%02d", countDownSecond)
        binding.progressBar.progress = 100
    }

    // show time picker dialog with second 1sec to 60sec
    // if time is selected, set time to countDownTextView
    private fun showSecondPickerDialog() {
        AlertDialog.Builder(this).apply {
            val bindingDialog = DialogCountdownSettingBinding.inflate(layoutInflater)
            with(bindingDialog.countdownSecondPicker) {
                maxValue = 20
                minValue = 0
                value = countDownSecond
            }

            setView(bindingDialog.root)
            setTitle("카운트 다운 시간 설정")

            setPositiveButton("확인") { dialog, _ ->
                bindingDialog.countdownSecondPicker.value.let {
                    countDownSecond = it
                    currentCountDownDeciSecond = countDownSecond * 10
                    binding.countDownTextView.text = String.format("%02d", it)
                }
                dialog.dismiss()
            }
            setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
        }.show()
    }

    private fun updateButtonIsPlay(isPlay: Boolean) {
        if (isPlay) {
            binding.startButton.isVisible = false
            binding.stopButton.isVisible = false
            binding.pauseButton.isVisible = true
            binding.labButton.isVisible = true
        } else {
            binding.startButton.isVisible = true
            binding.stopButton.isVisible = true
            binding.pauseButton.isVisible = false
            binding.labButton.isVisible = false
        }
    }

    private fun start() {
        timer = timer(initialDelay = 0, period = 100) {

            if (currentCountDownDeciSecond == 0) {

                currentDeciSecond++
                val minutes = currentDeciSecond.div(10) / 60
                val second = currentDeciSecond.div(10) % 60
                val deciSecond = currentDeciSecond % 10
                runOnUiThread {
                    binding.countDownLayer.isVisible = false
                    binding.timeTextView.text = String.format("%02d:%02d", minutes, second)
                    binding.tickTextView.text = deciSecond.toString()
                }

            } else {
                currentCountDownDeciSecond--
                val second = currentCountDownDeciSecond.div(10)
                val progress = (currentCountDownDeciSecond / (countDownSecond * 10f)) * 100
                runOnUiThread {
                    binding.countDownTextView.text = String.format("%02d", second)
                    binding.progressBar.progress = progress.toInt()
                }
            }
            if (currentDeciSecond == 0 && currentCountDownDeciSecond < 31 && currentCountDownDeciSecond % 10 == 0) {
                // ToneGenerator

                beep()
            }


        }
    }

    private fun beep() {
        val toneType = if (currentCountDownDeciSecond == 0) {
            ToneGenerator.TONE_CDMA_ABBR_ALERT
        } else {
            ToneGenerator.TONE_PROP_BEEP
        }
        val toneGenerator = ToneGenerator(AudioManager.STREAM_ALARM, ToneGenerator.MAX_VOLUME)
        toneGenerator.startTone(toneType, 100)
    }

    private fun pause() {
        timer?.cancel()
        timer = null
    }

    private fun stop() {
        timer?.cancel()
        timer = null
        currentDeciSecond = 0
        binding.timeTextView.text = "00:00"
        binding.tickTextView.text = "0"
        binding.countDownLayer.isVisible = true
        labCount = 0
        initCountDownViews()
        binding.labContainerLinearLayout.removeAllViews()
    }

    private fun stopAlertDailog() {
        AlertDialog.Builder(this)
            .setMessage("종료하시겠습니까?")
            .setPositiveButton("네") { dialog, _ ->
                updateButtonIsPlay(false)
                stop()
                dialog.dismiss()

            }
            .setNegativeButton("아니오") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }


    fun lab() {
        TextView(this).apply {
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 0, 20)
            layoutParams = params
            textSize = 20f
            setTextColor(Color.MAGENTA)
            gravity = Gravity.CENTER
            val minutes = currentDeciSecond.div(10) / 60
            val second = currentDeciSecond.div(10) % 60
            val deciSecond = currentDeciSecond % 10
            val labTime = String.format("%02d:%02d:%d", minutes, second, deciSecond)
            text = "$labCount : $labTime"
        }.let {
            labCount++
            // add view always top
            binding.labContainerLinearLayout.addView(it, 0)
        }
    }

}