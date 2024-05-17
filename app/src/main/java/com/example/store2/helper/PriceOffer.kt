package com.example.store2.helper

fun Float?.getProductPrice(price: Float): Float{
    //this --> Percentage
    if (this == null)
        return price
    val remainingOffer = 1f - this
    val priceAfterOffer = remainingOffer * price

    return  priceAfterOffer
}