package com.example.drilling_part_1_5

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel: ViewModel() {

    private val usedWords = mutableListOf<String>()
    lateinit var currentWord:String

    private val _currentScrambledWord = MutableLiveData<String>()
    val currentScrambledWord: LiveData<String>
        get() = _currentScrambledWord

    private val _wordCount = MutableLiveData<Int>(0)
    val wordCount: LiveData<Int>
        get() = _wordCount

    private val _score = MutableLiveData<Int>(0)
    val score: LiveData<Int>
        get() = _score

    init {
        getNewWord()
    }

    private fun getNewWord(){
        currentWord = allWordsList.shuffled().random()
        val tempCharArray = currentWord.toCharArray()

        while (String(tempCharArray).equals(currentWord, ignoreCase = true)){
            tempCharArray.shuffle()
        }

        if(usedWords.contains(currentWord)){
            getNewWord()
        }else{
            _currentScrambledWord.value = String(tempCharArray)
            _wordCount.value = _wordCount.value?.inc()
            usedWords.add(currentWord)
        }

    }

    fun getNext():Boolean{
        return if(_wordCount.value!! < MAX_NO_OF_WORDS){
            getNewWord()
            true
        }else false
    }

    fun isCorrectAnswer(playerWord: String): Boolean{
        return if(playerWord.equals(currentWord, ignoreCase = true)){
            _score.value = _score.value?.plus(SCORE_INCREASE)
            true
        }else{
            false
        }
    }

    fun reInitializeGame(){
        _score.value = 0
        _wordCount.value = 0
        usedWords.clear()
        getNewWord()
    }
}