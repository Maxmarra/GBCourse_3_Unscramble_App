package com.example.drilling_part_1_1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    val wordList = mutableListOf<String>()
    lateinit var currentWord:String

    private var _scrambledWord = MutableLiveData<String>()
    val scrambledWord: LiveData<String>
            get()= _scrambledWord

    private var _score = MutableLiveData<Int>(0)
    val score: LiveData<Int>
        get()= _score

    private var _wordCount = MutableLiveData<Int>(0)
    val wordCount: LiveData<Int>
        get()= _wordCount

    init {
        getNextWord()
    }

    fun getNextWord(){
        currentWord = allWordsList.shuffled().random()
        val tempWord = currentWord.toCharArray()

        while(String(tempWord) == currentWord){
            tempWord.shuffle()
        }
        if(wordList.contains(currentWord)){
            getNextWord()
        }else{
         _scrambledWord.value = String(tempWord)
         _wordCount.value = _wordCount.value?.inc()
         wordList.add(currentWord)
        }
    }

    fun nextWord() : Boolean{
        return if(_wordCount.value!! < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        }else false

    }

    fun isUserWordCorrect(playerWord:String): Boolean{
        return if(playerWord.equals(currentWord, ignoreCase = true)) {
            _score.value = _score.value?.plus(SCORE_INCREASE)
            true
        } else false
    }

    fun reInitializeData(){
        _score.value = 0
        _wordCount.value = 0
        wordList.clear()
        getNextWord()
    }

}