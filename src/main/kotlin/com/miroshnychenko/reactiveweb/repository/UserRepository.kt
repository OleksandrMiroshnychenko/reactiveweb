package com.miroshnychenko.reactiveweb.repository

import com.miroshnychenko.reactiveweb.dto.User
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface UserRepository: ReactiveMongoRepository<User, String>