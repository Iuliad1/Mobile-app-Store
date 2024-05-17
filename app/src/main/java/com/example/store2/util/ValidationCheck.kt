package com.example.store2.util

import android.util.Patterns

fun validateEmail(email: String): RegValidation{
    if(email.isEmpty())
        return RegValidation.Failed("Field can't be empty")

    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        return RegValidation.Failed("Enter right email format")

    return RegValidation.Success
}

fun validatePassword(password: String): RegValidation{
    if(password.isEmpty())
        return  RegValidation.Failed("Field can't be empty")

    if(password.length < 6)
        return RegValidation.Failed("Password should contain 6 char ")

    return RegValidation.Success
}