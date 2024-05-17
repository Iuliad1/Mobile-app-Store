package com.example.store2.data.order

sealed class OrderStatus(val status: String) {

    object Ordered: OrderStatus("Ordered")
    object Confirmed: OrderStatus("Confirmed")
    object Shipped: OrderStatus("Shipped")
    object Delivered: OrderStatus("Delivered")
    object Canceled: OrderStatus("Canceled")
    object Returned: OrderStatus("Returned")
}

fun getOrderStatus(status: String): OrderStatus {
    return when(status) {
        "Ordered" -> {
            OrderStatus.Ordered
        }
        "Confirmed" -> {
            OrderStatus.Confirmed
        }
        "Shipped" -> {
            OrderStatus.Shipped
        }
        "Delivered" -> {
            OrderStatus.Delivered
        }
        "Canceled" -> {
            OrderStatus.Delivered
        }
       else -> OrderStatus.Returned
    }
}