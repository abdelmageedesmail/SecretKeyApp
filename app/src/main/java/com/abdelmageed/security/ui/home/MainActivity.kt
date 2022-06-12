package com.abdelmageed.security.ui.home

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider

import androidx.lifecycle.lifecycleScope
import com.abdelmageed.security.R
import com.abdelmageed.security.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: HomeViewModel

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpBinding()
        setUpViewModel()
        getStates()
    }

    private fun getStates() {
        lifecycleScope.launchWhenCreated {
            viewModel.stateFlow.collect {
                when (it) {
                    is HomeStateFlow.error -> {
                        binding.tvResult.setText(it.e.toString())
                        Log.e("error", "Error Message..." + it.e.toString())
                    }
                    is HomeStateFlow.decrypted -> {
                        binding.tvResult.setText(it.text)
                    }
                }
            }
        }
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        binding.btnSave.setOnClickListener {
            if (!binding.etText.text.toString().isEmpty()) {
                viewModel.encryptDate(binding.etText.text.toString())
            } else {
                binding.etText.error = "empty"
            }
        }

        binding.btnGetResult.setOnClickListener {
            viewModel.decryptDate()
        }
    }

    private fun setUpBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
    }
}