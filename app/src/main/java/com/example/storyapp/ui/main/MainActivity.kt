package com.example.storyapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.LoadingStateAdapter
import com.example.storyapp.MapsActivity
import com.example.storyapp.R
import com.example.storyapp.StoryAdapter
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.preference.UserPreference
import com.example.storyapp.preference.dataStore
import com.example.storyapp.ui.addstory.AddStoryActivity
import com.example.storyapp.ui.login.LoginActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val userPreference by lazy { UserPreference.getInstance(dataStore) }
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var adapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        checkLoginSession()
        setupFab()
        observeViewModel()
    }

    private fun setupFab() {
        binding.fab.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkLoginSession() {
        lifecycleScope.launch {
            val user = userPreference.getSession().first()
            if (user.isLogin) {
                setupRecyclerView()
            } else {
                navigateToLogin()
            }
        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        finish()
    }

    private fun setupRecyclerView() {
        adapter = StoryAdapter()
        binding.rvStories.layoutManager = LinearLayoutManager(this)
        binding.rvStories.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter { adapter.retry() }
        )

        lifecycleScope.launch {
            mainViewModel.stories.collectLatest { pagingData ->
                adapter.submitData(lifecycle, pagingData) // Use lifecycle for submitting data
            }
        }
    }

    private fun observeViewModel() {
        mainViewModel.isLoggedOut.observe(this) { isLoggedOut ->
            if (isLoggedOut) {
                Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
                navigateToLogin()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.story_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                lifecycleScope.launch { mainViewModel.logout() }
                true
            }
            R.id.maps -> {
                startActivity(Intent(this, MapsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}