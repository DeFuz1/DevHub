package com.example.devhub

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform