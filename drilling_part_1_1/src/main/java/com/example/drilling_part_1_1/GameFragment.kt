package com.example.drilling_part_1_1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.drilling_part_1_1.databinding.FragmentGameBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class GameFragment : Fragment() {

    val viewModel: GameViewModel by viewModels()
    lateinit var binding: FragmentGameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.submitButton.setOnClickListener { onSubmitWord() }
        binding.skipButton.setOnClickListener { onSkipWord() }

        viewModel.scrambledWord.observe(viewLifecycleOwner){
            newWord-> binding.scrambledWord.text = newWord
        }
        viewModel.score.observe(viewLifecycleOwner){
                score-> binding.score.text = getString(R.string.score, score)
        }
        viewModel.wordCount.observe(viewLifecycleOwner){
                oldWordCount-> binding.wordCount.text = getString(R.string.word_count, oldWordCount, MAX_NO_OF_WORDS)
        }


    }

    private fun onSubmitWord(){
        val playerWord = binding.textInputEditText.text.toString()

        if(viewModel.isUserWordCorrect(playerWord)){
            setErrorTextField(false)
            if(!viewModel.nextWord()){
                showFinalScoreDialog()
            }
        }else setErrorTextField(true)

    }

    private fun onSkipWord(){
            if(viewModel.nextWord()){
                setErrorTextField(false)
            }else{showFinalScoreDialog()}
    }

    private fun setErrorTextField(error: Boolean) {
        if(error){
            binding.textInputLayout.isErrorEnabled = true
            binding.textInputLayout.error = "Попробуй еще раз"
        }else{
            binding.textInputLayout.isErrorEnabled = false
            binding.textInputEditText.error = null


        }
    }

    fun showFinalScoreDialog(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Отлично")
            .setMessage(getString(R.string.you_gained, viewModel.score.value))
            .setCancelable(false)
            .setNegativeButton("Выход"){
                _,_ -> exitGame()
            }
            .setPositiveButton("Сыграть еще раз"){
                _,_ -> restartGame()
            }
            .show()
    }

    fun exitGame(){
        activity?.finish()
    }

    fun restartGame(){
        viewModel.reInitializeData()
        setErrorTextField(false)
    }


}