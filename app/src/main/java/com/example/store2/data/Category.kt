package com.example.store2.data

sealed class Category(val category: String){

    object Iuvas: Category("Iuvas")
    object Maicom: Category("Maicom")
    object Viorica: Category("Viorica")
    object Cristina: Category("Cristina")


}