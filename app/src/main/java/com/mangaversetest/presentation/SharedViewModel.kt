package com.mangaversetest.presentation

import androidx.lifecycle.ViewModel
import com.mangaversetest.utils.sharedPreferences.UserPrefRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private val userPrefRepo: UserPrefRepo) :ViewModel() {
    fun getIsLogin()= userPrefRepo.getIsLogin()
}