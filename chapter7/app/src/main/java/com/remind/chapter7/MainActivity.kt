package com.remind.chapter7

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.remind.chapter7.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), WordAdapter.ItemClickListener {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var wordAdapter: WordAdapter
    private var selectedWord: Word? = null
    private val updateAddWordResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val isUpdated = result.data?.getBooleanExtra("isUpdated", false)
                if (result.resultCode == RESULT_OK && isUpdated == true) {
                    updateList()
                }
            }
        }

    private val updateEditWordResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val editWord = result.data?.getParcelableExtra("editWord", Word::class.java)
                if (result.resultCode == RESULT_OK && editWord != null) {
                    updateEditWord(editWord)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initRecyclerView()
        binding.addButton.setOnClickListener {
            updateAddWordResult.launch(
                Intent(this, AddActivity::class.java)
            )
        }



        binding.deleteButton.setOnClickListener {
            selectedWord?.let { word ->
                CoroutineScope(Dispatchers.Default).launch {
                    AppDatabase.getInstance(this@MainActivity)?.wordDao()?.delete(word)
                    withContext(Dispatchers.Main) {
                        updateList()
                    }
                }
            }
        }
        binding.editButton.setOnClickListener {
           edit()
        }
    }

    private fun edit() {
        if(selectedWord == null) return
        updateEditWordResult.launch(
            Intent(this, AddActivity::class.java).apply {
                putExtra("editWord", selectedWord)
            }
        )
    }


    private fun initRecyclerView() {

        wordAdapter = WordAdapter(mutableListOf(), this@MainActivity)
        binding.wordRecyclerView.apply {
            adapter = wordAdapter
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            val dividerItemDecoration =
                DividerItemDecoration(this@MainActivity, LinearLayoutManager.VERTICAL)
            addItemDecoration(dividerItemDecoration)
        }
        updateList()
    }

    private fun updateList() {
        val wordDao = AppDatabase.getInstance(this)?.wordDao()
        CoroutineScope(Dispatchers.Default).launch {
            val data = wordDao?.getAll() ?: mutableListOf()
            withContext(Dispatchers.Main) {
                wordAdapter.list = data as MutableList<Word>
                wordAdapter.notifyDataSetChanged()
                if (data.isEmpty()) {
                    binding.textTextView.text = "단어가 없습니다."
                    binding.meanTextView.text = "단어를 추가해주세요."
                } else {
                    selectedWord = data[0]
                    onClick(data[0])
                }
            }
        }
    }

    private fun updateEditWord(word: Word) {
        val index = wordAdapter.list.indexOfFirst { it.id == word.id }
        wordAdapter.list[index] = word
        selectedWord = word
        wordAdapter.notifyItemChanged(index)
        binding.textTextView.text = selectedWord?.text
        binding.meanTextView.text = "${selectedWord?.mean}  [${selectedWord?.type}]"
    }


    override fun onClick(word: Word) {
        selectedWord = word
        binding.textTextView.text = selectedWord?.text
        binding.meanTextView.text = "${selectedWord?.mean}  [${selectedWord?.type}]"
    }
}