package com.remind.chapter7

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import com.google.android.material.chip.Chip
import com.remind.chapter7.databinding.ActivityAddBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddActivity : AppCompatActivity() {

    val binding by lazy { ActivityAddBinding.inflate(layoutInflater) }
    private var originWord: Word? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
        binding.addButton.setOnClickListener {
            if (originWord == null) {
                add()
            } else {
                edit()
            }
        }
    }

    private fun add() {
        val text = binding.textInputEditText.text.toString()
        val mean = binding.meanInputEditText.text.toString()
        val type = findViewById<Chip>(binding.typeChipGroup.checkedChipId)?.text?.toString()
        if (text.isEmpty() || mean.isEmpty()) {
            Toast.makeText(this, "단어와 뜻을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        if (type == null) {
            Toast.makeText(this, "타입을 선택해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        val word = Word(text, mean, type)
        CoroutineScope(Dispatchers.Default).launch {
            AppDatabase.getInstance(this@AddActivity)?.wordDao()?.insert(word)
        }
        Toast.makeText(this, "단어가 추가되었습니다.", Toast.LENGTH_SHORT).show()
        val intent = Intent().putExtra("isUpdated", true)
        setResult(RESULT_OK, intent)

        finish()
    }

    private fun edit() {
        val text = binding.textInputEditText.text.toString()
        val mean = binding.meanInputEditText.text.toString()
        val type = findViewById<Chip>(binding.typeChipGroup.checkedChipId)?.text?.toString()
        if (text.isEmpty() || mean.isEmpty()) {
            Toast.makeText(this, "단어와 뜻을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        if (type == null) {
            Toast.makeText(this, "타입을 선택해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        val editWord = originWord?.copy(text = text, mean = mean, type = type)
        CoroutineScope(Dispatchers.Default).launch {
            if (editWord != null) {
                AppDatabase.getInstance(this@AddActivity)?.wordDao()?.update(editWord)
            }
        }
        Toast.makeText(this, "단어가 수정되었습니다.", Toast.LENGTH_SHORT).show()
        val intent = Intent().putExtra("editWord", editWord)
        setResult(RESULT_OK, intent)

        finish()
    }

    private fun initViews() {
        val types = listOf(
            "명사",
            "동사",
            "형용사",
            "부사",
            "전치사",
            "접속사",
            "감탄사",
            "대명사"
        )
        binding.typeChipGroup.apply {
            types.forEach { text ->
                addView(createChip(text))
            }
        }

        binding.textInputEditText.addTextChangedListener {
            it?.let { text ->
                binding.textTextInputLayout.error = when (text.length) {
                    0 -> "단어를 입력해주세요."
                    1 -> "너무 짧아요.(2자 이상)"
                    else -> null
                }
            }
        }

        originWord = intent.getParcelableExtra("editWord", Word::class.java)
        binding.textInputEditText.setText(originWord?.text)
        binding.meanInputEditText.setText(originWord?.mean)
        binding.typeChipGroup.children.forEach { chip ->
            if (chip is Chip && chip.text == originWord?.type) {
                chip.isChecked = true
            }
        }
    }

    private fun createChip(text: String): Chip {
        Chip(this).apply {
            this.text = text
            isClickable = true
            isCheckable = true
            return this
        }
    }
}