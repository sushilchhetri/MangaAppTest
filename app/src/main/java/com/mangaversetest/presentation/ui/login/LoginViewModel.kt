package com.mangaversetest.presentation.ui.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.mangaversetest.utils.sharedPreferences.UserPrefRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userPrefRepo: UserPrefRepo) :ViewModel() {
    private val _loginSuccess = mutableStateOf(false)
    val loginSuccess: State<Boolean> = _loginSuccess

    fun login() {
        userPrefRepo.saveIsLogin(isLogin = true)
        _loginSuccess.value = userPrefRepo.getIsLogin()
    }
}