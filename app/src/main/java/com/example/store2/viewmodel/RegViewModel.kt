package com.example.store2.viewmodel

import androidx.lifecycle.ViewModel
import com.example.store2.data.User
import com.example.store2.util.Constants.u_collection
import com.example.store2.util.RegFieldState
import com.example.store2.util.RegValidation
import com.example.store2.util.Resource
import com.example.store2.util.validateEmail
import com.example.store2.util.validatePassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore_db: FirebaseFirestore
): ViewModel(){

    private val _reg = MutableStateFlow<Resource<User>>(Resource.Unspecified())
     val reg: Flow<Resource<User>> = _reg

    private val _valid = Channel<RegFieldState>()
    val valid = _valid.receiveAsFlow()

    fun createAccWithEmailAndPass(user: User,password: String) {
        if (checkValidation(user, password)) {
            runBlocking {
                _reg.emit(Resource.Loading())
            }
            firebaseAuth.createUserWithEmailAndPassword(user.email, password)
                .addOnSuccessListener {
                    it.user?.let {
                        saveUserInfo(it.uid,user)

                    }
                }
                .addOnFailureListener {
                    _reg.value = Resource.Error(it.message.toString())
                }
        }
        else {
            val regFieldState = RegFieldState(
                validateEmail(user.email),validatePassword(password)
            )
            runBlocking {
                _valid.send(regFieldState)
            }
        }
    }

    private fun saveUserInfo(userUID: String, user: User) {
       firestore_db.collection(u_collection)
           .document(userUID)
           .set(user)
           .addOnSuccessListener {
               _reg.value = Resource.Success(user)
           }
           .addOnFailureListener {
               _reg.value = Resource.Error(it.message.toString())

           }
    }

    private fun checkValidation(user: User, password: String): Boolean {
        val emailValidation = validateEmail(user.email)
        val passwordValidation = validatePassword(password)
        val successReg =
            emailValidation is RegValidation.Success && passwordValidation is RegValidation.Success

        return successReg
    }
}