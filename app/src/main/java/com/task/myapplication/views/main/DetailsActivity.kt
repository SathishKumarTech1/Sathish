package com.task.myapplication.views.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.task.myapplication.R
import com.task.myapplication.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        var firstName = intent.getStringExtra("USER_FIRSTNAME")
        var lastName = intent.getStringExtra("USER_LASTNAME")
        var profile = intent.getStringExtra("USER_PROFILE")
        binding.name.text = firstName + " " + lastName
        Glide.with(this).load(profile).into(binding.imageview)
    }
}