//
// Created by TURBO on 6/14/2022.
//
#include <jni.h>

JNIEXPORT jstring JNICALL
Java_com_abdelmageed_security_ui_home_MainActivity_getNativeKey1(JNIEnv *env, jobject instance) {

 return (*env)->  NewStringUTF(env, "TmF0aXZlNWVjcmV0UEBzc3cwcmQxTestAddString1");
}

JNIEXPORT jstring JNICALL
Java_com_abdelmageed_security_ui_home_MainActivity_getNativeKey2(JNIEnv *env, jobject instance) {

 return (*env)->NewStringUTF(env, "TmF0aXZlNWVjcmV0UEBzc3cwcmQyTestAddString2");
}