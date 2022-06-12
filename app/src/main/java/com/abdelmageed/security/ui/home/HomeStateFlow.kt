package com.abdelmageed.security.ui.home

sealed class HomeStateFlow() {
    object loading : HomeStateFlow()
    class encrypted(val pair: ByteArray) : HomeStateFlow()
    class decrypted(val text: String) : HomeStateFlow()
    class error(val e: Throwable) : HomeStateFlow()
}