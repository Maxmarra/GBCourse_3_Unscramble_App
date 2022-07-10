package com.example.android.unscramble.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    //TODO(1) создай изменяемый список wordsList для хранения
    // использованных слов
    private var wordsList = mutableListOf<String>()

    //TODO(2) создай переменную currentWord с поздней инициализацией
    // для хранения текущего слова из всего списка слов
    private lateinit var currentWord: String

    //TODO(3) создай три переменные типа MutableLiveData
    // строку _currentScrambledWord / и два инта _score и _currentWordCount
    // для хранения готового слова, счета очков и счета пройденных слов
    // добавь для них backing property типа LiveData
    // всем в скобках задай начальное значение ()
    private val _currentScrambledWord = MutableLiveData<String>("")
    val currentScrambledWord: LiveData<String>
        get() = _currentScrambledWord

    private val _score = MutableLiveData(0)
    val score: LiveData<Int>
        get() = _score

    private val _currentWordCount = MutableLiveData(0)
    val currentWordCount: LiveData<Int>
        get() = _currentWordCount

    //TODO(5) инициализируй метод  getNextWord()
    init {
        getNextWord()
    }

    //TODO(4) создай метод  getNextWord() для получения очередного
    // слова из списка слов
    // 1. сохрани в currentWord перемешанное рандомное слово
    // из списка файла ListofWords
    // 2. положи его во временную переменную val tempWord
    // в виде массива букв
    // 3. сделай проверку через цикл while()
    // на то чтобы перемешанные буквы слова случайно
    // не выдали это самое слово
    // пока tempWord приведенный к строке (втрой способ())
    // равен текущему слову, с игнором
    // заглавных букв, перемешивай tempWord
    // 4. проверь есть ли в списке пройденных слов это слово
    // и если есть то снова вызывай этот метод getNextWord()
    // если wordsList содержит currentWord - вызывай снова getNextWord()
    // иначе присвой _currentScrambledWord приведенное к строке tempWord
    // увеличь _currentWordCount на 1 (используй метод)
    // добавь в список пройденных слов данное слово currentWord
    private fun getNextWord(){

        currentWord = allWordsList.shuffled().random()
        val tempWord = currentWord.toCharArray()

        while (String(tempWord).equals(currentWord, false)) {
            tempWord.shuffle()
        }
        if (wordsList.contains(currentWord)) {
            getNextWord()
        } else {
            _currentScrambledWord.value = String(tempWord)
            _currentWordCount.value = (_currentWordCount.value)?.inc()
            wordsList.add(currentWord)
        }
    }

    //TODO(6) ин
    fun nextWord(): Boolean {
        return if (_currentWordCount.value!! < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else false
    }


    fun isUserWordCorrect(playerWord: String): Boolean {
        if (playerWord.equals(currentWord, true)) {
            _score.value = (_score.value)?.plus(SCORE_INCREASE)
            return true
        }
        return false
    }

    fun reinitializeData() {
        _score.value = 0
        _currentWordCount.value = 0
        wordsList.clear()
        getNextWord()
    }
}