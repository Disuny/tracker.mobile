package com.example.tracker

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform