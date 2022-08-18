package com.task.myapplication.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.task.myapplication.adapter.MoviePagerAdapter
import com.task.myapplication.databinding.ActivityMainBinding
import com.task.myapplication.managers.retrofit.RetrofitService
import com.task.myapplication.models.gson.Data
import com.task.myapplication.repository.MainRepository
import com.task.myapplication.viewmodels.MainViewModel
import com.task.myapplication.viewmodels.factory.MyViewModelFactory
import com.task.myapplication.views.main.DetailsActivity

import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MainViewModel
    private val adapter = MoviePagerAdapter()
    lateinit var binding: ActivityMainBinding

    private lateinit var userData: Data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val retrofitService = RetrofitService.getInstance()
        val mainRepository = MainRepository(retrofitService)
        binding.recyclerview.adapter = adapter

        viewModel = ViewModelProvider(
            this,
            MyViewModelFactory(mainRepository)
        ).get(MainViewModel::class.java)

        viewModel.errorMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        lifecycleScope.launch {
            viewModel.getMovieList().observe(this@MainActivity) {
                it?.let {
                    adapter.submitData(lifecycle, it)
                }
            }
        }

        adapter.onItemClicked = { data, pos ->
            userData = data
            val intent = Intent(this, DetailsActivity::class.java)
            val userId = data.id
            val userEmail = data.email
            val userProfile = data.avatar
            val userFirstName = data.first_name
            val userLastName = data.last_name
            intent.putExtra("USER_ID", userId)
            intent.putExtra("USER_EMAIL", userEmail)
            intent.putExtra("USER_PROFILE", userProfile)
            intent.putExtra("USER_FIRSTNAME", userFirstName)
            intent.putExtra("USER_LASTNAME", userLastName)
            startActivity(intent)
        }


    }

}