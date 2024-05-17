package com.example.store2.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store2.R
import com.example.store2.util.Constants.introduction_key
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroductionViewModel @Inject constructor(
    private val sharedPref: SharedPreferences,
    private val firebaseAuth: FirebaseAuth
)
    : ViewModel() {

    private val _nav = MutableStateFlow(0)
    val nav: StateFlow<Int> = _nav

    companion object {
        const val shopping_activity = 23
        val account_options_fragment = R.id.action_introductionFragment_to_accountOptionsFragment
    }

    init {
        val isButtonClicked = sharedPref.getBoolean(introduction_key, false)
        val user = firebaseAuth.currentUser

        if (user != null) {
            viewModelScope.launch {
                _nav.emit(shopping_activity)
            }
        } else if (isButtonClicked)
            viewModelScope.launch {
                _nav.emit(account_options_fragment)
            }else{
                Unit
            }
        }

    fun startButtonClick(){
        sharedPref.edit().putBoolean(introduction_key,true).apply()
      }

    }
