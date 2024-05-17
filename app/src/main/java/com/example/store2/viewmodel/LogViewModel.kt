package com.example.store2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store2.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {
    private  val _log = MutableSharedFlow<Resource<FirebaseUser>>()
    val log = _log.asSharedFlow()

    private val _resetPass = MutableSharedFlow<Resource<String>>()
    val resetPass = _resetPass.asSharedFlow()

    fun login(email: String, password: String){
        viewModelScope.launch{_log.emit(Resource.Loading())}
        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                viewModelScope.launch {
                    it.user?.let {
                        _log.emit(Resource.Success(it))
                    }
                }
            }
            .addOnFailureListener{
                viewModelScope.launch {
                    _log.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun resetPass(email: String) {
        viewModelScope.launch {
            _resetPass.emit(Resource.Loading())
        }
            firebaseAuth
                .sendPasswordResetEmail(email)
                .addOnSuccessListener {
                  viewModelScope.launch {
                  _resetPass.emit(Resource.Success(email))
                 }
              }
                .addOnFailureListener {
                    viewModelScope.launch {
                        _resetPass.emit(Resource.Error(it.message.toString()))


                }
        }
    }
}