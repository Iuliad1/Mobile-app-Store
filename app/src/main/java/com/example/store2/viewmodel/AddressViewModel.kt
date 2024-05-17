package com.example.store2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store2.data.Address
import com.example.store2.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
): ViewModel() {

    private val  _addNewAddress = MutableStateFlow<Resource<Address>>(Resource.Unspecified())
    val addNewAddress = _addNewAddress.asStateFlow()

    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()

    fun addAddress(address: Address) {
        val errorMessages = mutableListOf<String>()
        val isValid = validateInfo(address, errorMessages)
        if (isValid) {
            viewModelScope.launch { _addNewAddress.emit(Resource.Loading()) }

            firestore.collection("user").document(auth.uid!!)
                .collection("address").document().set(address)
                .addOnSuccessListener {
                    viewModelScope.launch { _addNewAddress.emit(Resource.Success(address)) }
                }
                .addOnFailureListener {
                    viewModelScope.launch { _addNewAddress.emit(Resource.Error(it.message.toString())) }
                }
        } else {
            val errorMessage = errorMessages.joinToString(separator = "\n")
            viewModelScope.launch {
                _error.emit(errorMessage)
            }
        }
    }

    private fun validateInfo(address: Address, errorMessages: MutableList<String>): Boolean {
        val nameRegex = Regex("^[A-Z][a-zA-Z ]*\$")
        val phoneRegex = Regex("^\\+[0-9]+\$")

        var isValid = true

        if (address.addressTitle.isEmpty() ||
            address.fullName.isEmpty() ||
            address.street.isEmpty() ||
            address.phone.isEmpty() ||
            address.city.isEmpty() ||
            address.state.isEmpty()
        ) {
            errorMessages.add("Toate câmpurile trebuie completate.")
            isValid = false
        }
        if (!nameRegex.matches(address.fullName)) {
            errorMessages.add("Numele trebuie să înceapă cu majusculă și să conțină doar litere.")
            isValid = false
        }
        if (!nameRegex.matches(address.city)) {
            errorMessages.add("Orașul trebuie să înceapă cu majusculă și să conțină doar litere.")
            isValid = false
        }
        if (!nameRegex.matches(address.state)) {
            errorMessages.add("Statul trebuie să înceapă cu majusculă și să conțină doar litere.")
            isValid = false
        }

        if (!phoneRegex.matches(address.phone)) {
            errorMessages.add("Numărul de telefon trebuie să conțină doar cifre și semnul '+'.")
            isValid = false
        }
        return isValid
    }

}