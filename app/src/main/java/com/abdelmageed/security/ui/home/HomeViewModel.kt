package com.abdelmageed.security.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel() : ViewModel() {

    val homeFlow = HomeFlow()
    private val _stateFlow = MutableStateFlow<HomeStateFlow>(HomeStateFlow.loading);
    val stateFlow = _stateFlow
    @RequiresApi(Build.VERSION_CODES.M)
    fun encryptDate(text: String) {
        viewModelScope.launch {
            homeFlow.encryptData(text).catch {
                _stateFlow.value = HomeStateFlow.error(it)
            }.collect {
                _stateFlow.value = HomeStateFlow.encrypted(it)
            }
        }
    }

    fun decryptDate() {
        viewModelScope.launch {
            homeFlow.decryptData().catch {
                _stateFlow.value = HomeStateFlow.error(it)
            }.collect {
                _stateFlow.value = HomeStateFlow.decrypted(it)
            }
        }
    }
}