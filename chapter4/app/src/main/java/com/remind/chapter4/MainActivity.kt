package com.remind.chapter4

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.remind.chapter4.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // btnAddUser Click Event
        // move InputActivity
        binding.btnAddUser.setOnClickListener {
            val intent = Intent(this, InputActivity::class.java)
            startActivity(intent)
        }

        binding.btnDelete.setOnClickListener {
            deleteData()
        }

        binding.callLayer.setOnClickListener {
            // Phone App 실행
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${binding.tvEmergencyContactValue.text}")
            startActivity(intent)
        }
    }

    private fun deleteData() {
        getSharedPreferences(USER_INFO, MODE_PRIVATE).edit().clear().apply()
        Toast.makeText(this, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
        getDataAndUIUpdate()
    }

    override fun onResume() {
        super.onResume()
        getDataAndUIUpdate()
    }

    private fun getDataAndUIUpdate() {
        getSharedPreferences(USER_INFO, MODE_PRIVATE).apply {
            val name = getString(NAME, "미정")
            val emergencyNumber = getString(EMERGENCY_CONTACT, "미정")
            val bloodType = getString(BLOOD_TYPE, "미정")
            val birthdate = getString(BIRTHDATE, "미정")
            val warning = getString(WARNING, "미정")

            binding.tvNameValue.text = name
            binding.tvEmergencyContactValue.text = emergencyNumber
            binding.tvBloodTypeValue.text = bloodType
            binding.tvBirthValue.text = birthdate

            binding.tvCautions.isVisible = warning.isNullOrEmpty().not()
            binding.tvCautionsValue.isVisible = warning.isNullOrEmpty().not()
            if (!warning.isNullOrEmpty()) {
                binding.tvCautionsValue.text = warning
            }
        }
    }
}