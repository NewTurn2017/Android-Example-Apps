package com.remind.chapter8

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.remind.chapter8.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val imageLoadLauncher =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList ->
            updateImages(uriList)
        }
    private lateinit var imageAdapter: ImageAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.apply {
            title = "사진 가져오기"
            setSupportActionBar(this)
        }


        binding.loadImageButton.setOnClickListener {
            checkPermission()
        }
        binding.navigateFrameActivityButton.setOnClickListener {
            navigateToFrameActivity()
        }
        initRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                checkPermission()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun navigateToFrameActivity() {
        val images = imageAdapter.currentList.filterIsInstance<ImageItems.Image>()
            .map { it.uri.toString() }.toTypedArray()
        val intent = Intent(this, FrameActivity::class.java)
            .putExtra("images", images)
        startActivity(intent)
    }

    private fun initRecyclerView() {
        binding.imageRecyclerView.adapter = ImageAdapter(object : ImageAdapter.ItemClickListener {

            override fun onLoadMoreClick() {
                imageLoadLauncher.launch("image/*")
            }
        })
        imageAdapter = ImageAdapter(object : ImageAdapter.ItemClickListener {

            override fun onLoadMoreClick() {
                checkPermission()
            }
        })

        binding.imageRecyclerView.apply {
            adapter = imageAdapter
            layoutManager = GridLayoutManager(this@MainActivity, 2)
        }
    }

    private fun checkPermission() {


        when {
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                loadImage()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                showPermissionInfoDialog()
            }
            else -> {
                requestReadExternalStorage()
            }
        }
    }

    private fun requestReadExternalStorage() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_READ_EXTERNAL_STORAGE
        )
    }

    private fun showPermissionInfoDialog() {
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다.")
            .setMessage("사진 정보를 얻으려면 외부 저장소 권한이 필수로 필요합니다.")
            .setPositiveButton("동의하기") { _, _ ->
                requestReadExternalStorage()
            }
            .setNegativeButton("취소하기") { _, _ ->
                Toast.makeText(this, "권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
            }
            .create()
            .show()
    }

    private fun loadImage() {
        imageLoadLauncher.launch("image/*")
    }

    private fun updateImages(uriList: List<Uri>?) {
        val images = uriList?.map { ImageItems.Image(it) }
        val updatedImages = imageAdapter.currentList.toMutableList().apply {
            addAll(images ?: listOf())
        }
        imageAdapter.submitList(updatedImages)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_READ_EXTERNAL_STORAGE -> {
                val resultCode = grantResults.firstOrNull() ?: PackageManager.PERMISSION_DENIED
                if ((resultCode == PackageManager.PERMISSION_GRANTED)) {
                    loadImage()
                } else {
                    Toast.makeText(this, "권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    companion object {
        const val REQUEST_READ_EXTERNAL_STORAGE = 100
    }
}