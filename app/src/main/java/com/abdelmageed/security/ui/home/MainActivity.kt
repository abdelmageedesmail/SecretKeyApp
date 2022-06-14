package com.abdelmageed.security.ui.home

import android.R
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
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
        getAuth()

        val key1 = String(Base64.decode(getNativeKey1(), Base64.DEFAULT))
        val key2 = String(Base64.decode(getNativeKey2(), Base64.DEFAULT))

        binding.tvResult.text = "Key1-->$key1\nKey2-->$key2"
    }

    external fun getNativeKey1(): String
    external fun getNativeKey2(): String


    init {
        System.loadLibrary("keys")
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
        binding =
            DataBindingUtil.setContentView(this, com.abdelmageed.security.R.layout.activity_main)
        binding.lifecycleOwner = this
    }

    private fun getAuth(): BiometricPrompt {
        val biometricManager = BiometricManager.from(this)


        val executor = ContextCompat.getMainExecutor(this)
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(this@MainActivity, "$errorCode :: $errString", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(
                    this@MainActivity,
                    "UnAuthorized user",
                    Toast.LENGTH_SHORT
                ).show()

            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Toast.makeText(
                    this@MainActivity,
                    "Login success",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }

        val biometricPrompt = BiometricPrompt(this, executor, callback)

        if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
            biometricPrompt.authenticate(showBiometricDialog())
        } else {
            Toast.makeText(this, "Finger print not allowed in this device!!!", Toast.LENGTH_SHORT)
                .show()
        }
        return biometricPrompt
    }

    private fun showBiometricDialog(): BiometricPrompt.PromptInfo {
        val build = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Fingerprint Auth")
            .setDescription("TryFingerPrint")
            .setNegativeButtonText("User App Password")
            .build()
        return build

    }

}