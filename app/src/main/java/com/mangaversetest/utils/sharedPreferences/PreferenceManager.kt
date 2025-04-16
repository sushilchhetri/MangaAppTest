package com.mangaversetest.utils.sharedPreferences

import android.content.SharedPreferences

import javax.inject.Inject

class PreferenceManager @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val editor: SharedPreferences.Editor
) {
    companion object{
        const val isLoginKEY = "IS_LOGIN_KEY"
        const val PREFERENCE_NAME = "com.roya.medical_Pref"
    }

    var isLogin:Boolean
        get(){
            return sharedPreferences.getBoolean(isLoginKEY, false)
        }
        set(value) {
            editor.putBoolean(isLoginKEY, value)
            editor.apply()
        }


    fun clearAllPreferences() {
        editor.clear()
        editor.apply()
    }
}