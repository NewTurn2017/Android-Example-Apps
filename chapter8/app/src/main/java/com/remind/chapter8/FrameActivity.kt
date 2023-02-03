package com.remind.chapter8

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.remind.chapter8.databinding.ActivityFrameBinding

class FrameActivity : AppCompatActivity() {
    val binding by lazy { ActivityFrameBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val images = (intent.getStringArrayExtra("images")
            ?: emptyArray()).map { uriString -> FrameItem(Uri.parse(uriString)) }.toList()
        val framAdapter = FrameAdapter(images)
        binding.viewPager.adapter = framAdapter
    }
}