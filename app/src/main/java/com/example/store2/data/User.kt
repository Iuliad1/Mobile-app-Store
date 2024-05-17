package com.example.store2.data

data class User(
    val firstName: String,
    val lastName: String,
    val email: String,
    val pathImage: String = ""
)
{
    constructor(): this("","","","")
}
