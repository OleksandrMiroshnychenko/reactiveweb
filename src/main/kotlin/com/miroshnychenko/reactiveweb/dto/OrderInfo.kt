package com.miroshnychenko.reactiveweb.dto

data class OrderInfo(
    val orderNumber: String,
    val userName: String,
    val phoneNumber: String,
    val productCode: String
) {

    var productName: String? = null

    var productId: String? = null

    constructor(
        orderNumber: String,
        userName: String,
        phoneNumber: String,
        productCode: String,
        productName: String,
        productId: String
    ) : this(orderNumber, userName, phoneNumber, productCode) {
        this.productName = productName
        this.productId = productId
    }
}