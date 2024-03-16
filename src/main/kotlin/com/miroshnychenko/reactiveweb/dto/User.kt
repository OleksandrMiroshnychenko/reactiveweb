package com.miroshnychenko.reactiveweb.dto

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("users")
data class User(

    var name: String,

    var phone: String
) {
    @Id
    lateinit var id: String

    override fun toString(): String {
        return "User{" +
                "id=" + id +
                '}'
    }
}