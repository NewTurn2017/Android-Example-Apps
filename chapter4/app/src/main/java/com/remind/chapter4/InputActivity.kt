package com.remind.chapter4

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.remind.chapter4.databinding.ActivityInputBinding

class InputActivity : AppCompatActivity() {

    val binding by lazy { ActivityInputBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.bloodTypeSpinner.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.blood_types,
            android.R.layout.simple_list_item_1
        )

        binding.birthdateLayer.setOnClickListener {
            val datePickerFragment = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    binding.birthdateValueTextView.text = "$year/${month + 1}/$dayOfMonth"
                },
                1990,
                0,
                1
            ).show()
        }

        binding.warningCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) binding.warningEditText.visibility = View.VISIBLE
            else
                binding.warningEditText.visibility = View.GONE
        }

        binding.saveButton.setOnClickListener {
            saveData()
            finish()
        }

    }

    private fun saveData() {

        with(
            getSharedPreferences(USER_INFO, MODE_PRIVATE)
                .edit()
        ) {
            putString(NAME, binding.nameEditText.text.toString())
            putString(BLOOD_TYPE, getBloodType())
            putString(BIRTHDATE, binding.birthdateValueTextView.text.toString())
            putString(WARNING, getWarning())
            putString(EMERGENCY_CONTACT, binding.emergencyContactEditText.text.toString())
            apply()
        }

        Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun getBloodType(): String {
        val bloodAlphabet = binding.bloodTypeSpinner.selectedItem.toString()
        val bloodSign = if (binding.bloodTypePlus.isChecked) "+" else "-"
        return "$bloodAlphabet$bloodSign"
    }

    private fun getWarning(): String {
        return if (binding.warningCheckBox.isChecked) binding.warningEditText.text.toString() else ""
    }
}