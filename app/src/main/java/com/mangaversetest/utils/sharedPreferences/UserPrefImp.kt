package com.mangaversetest.utils.sharedPreferences


class UserPrefImp (private val prefManager:PreferenceManager): UserPrefRepo {


    override fun getIsLogin(): Boolean = prefManager.isLogin

    override fun saveIsLogin(isLogin: Boolean) {
        prefManager.isLogin = isLogin
    }

    override fun clearPreference() {
        prefManager.clearAllPreferences()
    }



}