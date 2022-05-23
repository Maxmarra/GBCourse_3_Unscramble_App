package com.example.drilling_part_1_5

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.drilling_part_1_5.databinding.FragmentGameBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.lang.Error

class GameFragment : Fragment() {


    val viewModel:GameViewModel by viewModels()
    private lateinit var binding: FragmentGameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.skipButton.setOnClickListener { onSkipButton() }
        binding.submitButton.setOnClickListener { onSubmitButton() }

        viewModel.score.observe(viewLifecycleOwner){newScore->
            binding.score.text = getString(R.string.score, newScore)
        }

        viewModel.currentScrambledWord.observe(viewLifecycleOwner){newWord->
            binding.scrambledWord.text = newWord
        }

        viewModel.wordCount.observe(viewLifecycleOwner){newCount->
            binding.wordCount.text = getString(R.string.word_count, newCount, MAX_NO_OF_WORDS)
        }


    }

    fun onSkipButton(){
            if(viewModel.getNext()){
                setErrorText(false)
            }else {
                showDialog()
            }
        binding.textInputEditText.setText("")
    }

    fun onSubmitButton(){
        val playerWord = binding.textInputEditText.text.toString()

        if(viewModel.isCorrectAnswer(playerWord)){
            setErrorText(false)
            if(!viewModel.getNext()){
                showDialog()
            }
        }else {
            setErrorText(true)
        }
        binding.textInputEditText.setText("")
    }

    fun restartGame(){
        setErrorText(false)
        viewModel.reInitializeGame()
    }

    fun endGame(){
        activity?.finish()
    }

    private fun setErrorText(error: Boolean){
        if(error){
            binding.textInputLayout.isErrorEnabled = true
            binding.textInputLayout.error = "еще разок"
        }else{
            binding.textInputLayout.isErrorEnabled = false
            binding.textInputEditText.error = null
        }
    }

    fun showDialog(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Конец игры")
            .setMessage(getString(R.string.dialog_score, viewModel.score.value))
            .setCancelable(false)
            .setNegativeButton("выйти") {
                _,_ -> endGame()
            }
            .setPositiveButton("еще раз") {
                    _,_ -> restartGame()
            }
            .show()
    }


}