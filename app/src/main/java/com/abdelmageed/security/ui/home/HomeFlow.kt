package com.abdelmageed.security.ui.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.abdelmageed.security.utils.Decryptor
import com.abdelmageed.security.utils.Encryptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class HomeFlow {

    val encryptor = Encryptor()
    val decryptor = Decryptor()
    private val ALIAS = "MYALIAS"

    @RequiresApi(Build.VERSION_CODES.M)
    fun encryptData(text: String) = flow<ByteArray> {

        encryptor.encryptText(ALIAS, text)?.let { emit(it) }
        Log.e("flowData", "encIv.." + encryptor.getIv().toString())
        Log.e("flowData", "enccc.." + encryptor.getEncryption().toString())
    }.flowOn(Dispatchers.Main)

    @RequiresApi(Build.VERSION_CODES.M)
    fun decryptData() = flow<String> {
        Log.e("flowData", "iv.." + encryptor.getIv().toString())
        Log.e("flowData", "enc.." + encryptor.getEncryption().toString())
        decryptor.decryptData(ALIAS, encryptor.getEncryption(), encryptor.getIv())
            ?.let { emit(it) }

    }.flowOn(Dispatchers.Main)

}