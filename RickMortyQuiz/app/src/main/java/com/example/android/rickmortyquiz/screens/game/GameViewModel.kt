package com.example.android.rickmortyquiz.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.android.rickmortyquiz.R

class GameViewModel : ViewModel() {

    data class Question (
        val resId: Int,
        val correctAnswer: Boolean,
        var userAnswer:Boolean? = null
    )

    val questionBank = mutableListOf(
        Question(R.string.question_1, false),
        Question(R.string.question_2, true),
        Question(R.string.question_3, true)
)
    // The current question
    private val _question = MutableLiveData<Int>()
    val question: LiveData<Int>
        get() = _question

    // The current score
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    private var qIndex = 0


    // Event which triggers the end of the game
    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
        get() = _eventGameFinish

    private val _showCheck = MutableLiveData<Boolean>()
    val showCheck: LiveData<Boolean>
        get() = _showCheck

    private val _showX = MutableLiveData<Boolean>()
    val showX: LiveData<Boolean>
        get() = _showX

    private val _enableRadio = MutableLiveData<Boolean>()
    val enableRadio: LiveData<Boolean>
        get() = _enableRadio

    private val _selectedTrue = MutableLiveData<Boolean>()
    val selectedTrue: LiveData<Boolean>
        get() = _selectedTrue

    private val _selectedFalse = MutableLiveData<Boolean>()
    val selectedFalse: LiveData<Boolean>
        get() = _selectedFalse



    init {
        _question.value = questionBank[0].resId
        _score.value = 0
        questionBank.shuffle()
        qIndex = 0
        _showX.value = false
        _showCheck.value = false
        _enableRadio.value = true
        _selectedFalse.value = false
        _selectedTrue.value = false

    }

    fun onTapAnswer(answer:Boolean) {
        val q = questionBank[qIndex]
        q.userAnswer = answer
        _showCheck.value = q.correctAnswer == answer
        _showX.value = q.correctAnswer != answer
        _enableRadio.value = false
        if (answer == q.correctAnswer) {
            _score.value = _score.value?.plus(1)
        }

        var done = true

        for (q in questionBank) {
            if (q.userAnswer == null) {
                done = false
                break
            }
        }

        if (done) {
            onGameFinish()
        }

    }

    fun next() {
        if (qIndex == questionBank.size - 1) {
            qIndex = 0
        } else {
            qIndex ++
        }
        updateVars()

    }

    fun prev() {
        if (qIndex == 0) {
            qIndex = questionBank.size - 1
        } else {
            qIndex --
        }
        updateVars()
    }


    fun updateVars() {
        _question.value = questionBank[qIndex].resId
        _enableRadio.value = questionBank[qIndex].userAnswer == null
        _showCheck.value = false
        _showX.value = false
        if( questionBank[qIndex].userAnswer != null) {
            _selectedTrue.value = questionBank[qIndex].userAnswer == true
            _selectedFalse.value = questionBank[qIndex].userAnswer == false
        } else {
            _selectedTrue.value = false
            _selectedFalse.value = false
        }
    }

    /** Method for the game completed event **/
    fun onGameFinish() {
        _eventGameFinish.value = true
    }

    /** Method for the game completed event **/

    fun onGameFinishComplete() {
        _eventGameFinish.value = false
    }
}