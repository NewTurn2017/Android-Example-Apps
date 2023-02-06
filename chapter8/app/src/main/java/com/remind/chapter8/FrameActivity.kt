package com.remind.chapter8

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.tabs.TabLayoutMediator
import com.remind.chapter8.databinding.ActivityFrameBinding

class FrameActivity : AppCompatActivity() {
    val binding by lazy { ActivityFrameBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.apply {
            title = "나만의 앨범"
            setSupportActionBar(this)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val images = (intent.getStringArrayExtra("images")
            ?: emptyArray()).map { uriString -> FrameItem(Uri.parse(uriString)) }.toList()
        val framAdapter = FrameAdapter(images)
        binding.viewPager.adapter = framAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            binding.viewPager.currentItem = tab.position
        }.attach()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return false
    }
}