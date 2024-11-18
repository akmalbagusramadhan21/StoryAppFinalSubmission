package com.example.storyapp.ui.detailstory

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.storyapp.data.response.Story
import com.example.storyapp.databinding.ActivityDetailBinding
import com.example.storyapp.ui.main.ViewModelFactory

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels{
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyId = intent.getStringExtra("STORY_ID")
        if (storyId != null) {
            viewModel.getDetailStory(storyId)
            observeDetailStory()
        } else {
            Log.e("DetailActivity", "Story ID is null")
        }
    }

    private fun observeDetailStory() {
        viewModel.detailStory.observe(this) { detailStory ->
            detailStory?.let { populateDetail(it) }
        }
    }

    private fun populateDetail(story: Story) {
        binding.apply {
            tvDetailName.text = story.name
            tvDetailDescription.text = story.description
            Glide.with(this@DetailActivity)
                .load(story.photoUrl)
                .into(ivItemPhoto)
        }
    }
}