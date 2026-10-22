package com.example.lab1app

data class Question(
    val questionText: String,
    val options: List<String>,
    val correctIndex: Int
)