package com.example.lab1app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStreamReader

class QuizActivity : AppCompatActivity() {

    private lateinit var tvQuestionCount: TextView
    private lateinit var tvQuestionText: TextView
    private lateinit var btnOptions: List<Button>

    private val questionList = mutableListOf<Question>()
    private var currentQuestionIndex = 0
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        tvQuestionCount = findViewById(R.id.tvCounter)
        tvQuestionText = findViewById(R.id.tvQuestion)
        btnOptions = listOf(
            findViewById(R.id.btn1),
            findViewById(R.id.btn2),
            findViewById(R.id.btn3),
            findViewById(R.id.btn4)
        )

        loadQuestionsFromAssets()
        showNextQuestion()

        for (i in btnOptions.indices) {
            btnOptions[i].setOnClickListener {
                checkAnswer(i)
            }
        }
    }

    private fun loadQuestionsFromAssets() {
        try {
            val reader = BufferedReader(InputStreamReader(assets.open("questions.txt")))
            var line: String? = reader.readLine()
            while (line != null) {
                val parts = line.split("|")
                if (parts.size == 6) {
                    val questionText = parts[0]
                    val options = listOf(parts[1], parts[2], parts[3], parts[4])
                    val correctIndex = parts[5].toInt()
                    questionList.add(Question(questionText, options, correctIndex))
                }
                line = reader.readLine()
            }
            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showNextQuestion() {
        if (currentQuestionIndex < questionList.size) {
            val currentQuestion = questionList[currentQuestionIndex]

            tvQuestionCount.text = "Вопрос ${currentQuestionIndex + 1} из ${questionList.size}"
            tvQuestionText.text = currentQuestion.questionText

            for (i in btnOptions.indices) {
                btnOptions[i].text = currentQuestion.options[i]
            }
        } else {
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("SCORE", score)
            intent.putExtra("TOTAL", questionList.size)
            startActivity(intent)
            finish()
        }
    }

    private fun checkAnswer(selectedIndex: Int) {
        val currentQuestion = questionList[currentQuestionIndex]

        if (selectedIndex == currentQuestion.correctIndex) {
            score++
        }

        currentQuestionIndex++
        showNextQuestion()
    }
}