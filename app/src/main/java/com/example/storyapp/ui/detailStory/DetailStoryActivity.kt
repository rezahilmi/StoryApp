package com.example.storyapp.ui.detailStory

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.storyapp.data.api.Story
import com.example.storyapp.databinding.ActivityDetailStoryBinding
import com.example.storyapp.ui.ViewModelFactory

class DetailStoryActivity : AppCompatActivity() {
    companion object {
        const val KEY_ID = "story_id"
    }
    private lateinit var binding: ActivityDetailStoryBinding
    private lateinit var detailStoryViewModel: DetailStoryModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        val factory = ViewModelFactory.getInstance(this)
        detailStoryViewModel = ViewModelProvider(this, factory)[DetailStoryModel::class.java]

        val storyId = intent.getStringExtra(KEY_ID)
        if (storyId != null) {
            detailStoryViewModel.fetchDetailStory(storyId)
            detailStoryViewModel.storyDetail.observe(this) { response ->
                response?.let { detailStoryResponse ->
                    detailStoryResponse.story?.let { story ->
                        setStoryDetailData(story)
                    }
                }
            }
        }
        detailStoryViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setStoryDetailData(story: Story) {
        binding.tvDetailName.text = story.name
        binding.tvDetailDescription.text = story.description
        Glide.with(this).load(story.photoUrl).into(binding.ivDetailPhoto)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
