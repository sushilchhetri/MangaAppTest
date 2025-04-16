package com.mangaversetest.utils.sharedPreferences

interface UserPrefRepo {

    fun getIsLogin():Boolean
    fun saveIsLogin(isLogin:Boolean)


    /* Clear Preference */
    fun clearPreference()



}