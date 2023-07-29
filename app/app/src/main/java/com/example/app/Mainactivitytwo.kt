package com.example.app
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.app.R.id.letter

class Mainactivitytwo : AppCompatActivity() {
    private lateinit var letterSequence: TextView
    private lateinit var resetButton: Button
    private lateinit var checkButton: Button
    private lateinit var clue: Button
    private lateinit var lifeCount: TextView
    private lateinit var toastMessage: Toast
    private var wordToGuess: String = ""
    private var shuffledWord: String = ""
    private var lives: Int = 3
    private var score: Int = 0
    private var bestScore: Int = 0
    private var selectedLetters: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.yes)
        fun showClueDialog(clue: String) {
            val dialogView =
                LayoutInflater.from(this).inflate(R.layout.my_custom_dialog, null)
            val tvClueText = dialogView.findViewById<TextView>(R.id.cluetext)
            tvClueText.text = clue
            val btnDismiss = dialogView.findViewById<Button>(R.id.okay)
            val dialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .create()
            btnDismiss.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
        letterSequence = findViewById(letter)
        resetButton = findViewById(R.id.reset_button)
        checkButton = findViewById(R.id.check_button)
        clue = findViewById(R.id.clue)
        lifeCount = findViewById(R.id.life_count)
        resetButton.setOnClickListener {
            toastMessage = Toast.makeText(this, "Reset", Toast.LENGTH_SHORT)
        }
        val intent = intent
        wordToGuess = intent.getStringExtra("wordToGuess").toString()
        val clueText = intent.getStringExtra("clue")
        clue.text = "Clue: $clueText"
        shuffledWord = wordToGuess.shuffle()
        generateLetterSequence()
        lifeCount.text = "Lives: $lives"
        resetButton.setOnClickListener {
            resetGame()
        }
        checkButton.setOnClickListener {
        }
    }

    private fun String.shuffle(): String {
        val charArray = toCharArray()
        charArray.shuffle()
        return String(charArray)
    }

    private fun generateLetterSequence() {
        for (i in shuffledWord.indices) {
            val letter = shuffledWord[i].toString()
            val tile = layoutInflater.inflate(R.layout.yes, null)
            tile.findViewById<TextView>(R.id.letter).text = letter
            tile.setOnClickListener {
                selectLetter(tile, letter)
            }
            val params = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
//                    val childview = findViewById<TextView>(R.id.firstchildview)
            params.startToStart = letterSequence.id
            params.topToTop = letterSequence.id
        }
    }

    private fun selectLetter(tile: View, letter: String) {
        if (!selectedLetters.contains(letter)) {
            selectedLetters.add(letter)
            tile.findViewById<TextView>(R.id.letter)
        }
    }

    private fun resetGame() {
        selectedLetters.clear()
        val letterSequence =
            findViewById<ConstraintLayout>(androidx.constraintlayout.widget.R.id.constraint)

        for (i in 1 until letterSequence.childCount) {
            val params =
                letterSequence.getChildAt(i).layoutParams as ConstraintLayout.LayoutParams
        }
        for (i in 0 until letterSequence.childCount) {
            val tile = letterSequence.getChildAt(i - 1)
            tile.findViewById<TextView>(letter).background = null
        }
        shuffledWord = wordToGuess.shuffle()
        letterSequence.removeAllViews()
        generateLetterSequence()
        toastMessage.setText("Game reset")
        toastMessage.show()
    }

    private fun checkAnswer() {
        val answer = selectedLetters.joinToString("")
        if (answer.length == wordToGuess.length) {
            if (answer == wordToGuess) {
                score += 1
                resetGame()
                toastMessage.setText("Correct! The word was $wordToGuess")
                toastMessage.show()
            } else {
                lives -= 1
                if (lives == 0) {
                    if (score > bestScore) {
                        bestScore = score
                        saveBestScore()
                    }
                    score = 0
                    resetGame()
                    val dialogView =
                        LayoutInflater.from(this).inflate(R.layout.gameover, null)
                    toastMessage.setText("Game over! The word was $wordToGuess")
                    toastMessage.show()
                } else {
                    val letterSequence =
                        findViewById<ConstraintLayout>(androidx.constraintlayout.widget.R.id.constraint)
                    selectedLetters.clear()
                    for (i in 0 until letterSequence.childCount) {
                        val tile = letterSequence.getChildAt(i)
                        tile.findViewById<TextView>(letter).background = null
                    }
                    shuffledWord = wordToGuess.shuffle()
                    letterSequence.removeAllViews()
                    generateLetterSequence()
                    lifeCount.text = "Lives: $lives"
                    toastMessage.setText("Wrong answer! Try again")
                    toastMessage.show()
                }
            }
        } else {
            toastMessage.setText("Incomplete answer")
            toastMessage.show()
        }
    }

    private fun saveBestScore() {
        val sharedPreferences =
            getSharedPreferences("WORD_GUESSING_GAME", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("BEST_SCORE", bestScore)
        editor.apply()


        return TODO("Provide the return value")
    }
}
