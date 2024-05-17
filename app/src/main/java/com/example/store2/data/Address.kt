package com.example.store2.data

import android.location.Address
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    val addressTitle: String,
    val fullName: String,
    val street: String,
    val phone: String,
    val city: String,
    val state: String
):Parcelable {

    constructor(): this("","","","","","")
}
