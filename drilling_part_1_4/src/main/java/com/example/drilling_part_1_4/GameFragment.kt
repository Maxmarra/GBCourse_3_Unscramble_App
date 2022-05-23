package com.example.drilling_part_1_4

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels

import com.example.drilling_part_1_4.databinding.FragmentGameBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class GameFragment : Fragment() {

    private val viewModel:GameViewModel by viewModels()
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

        binding.submitButton.setOnClickListener { onSubmitButton() }
        binding.skipButton.setOnClickListener { onSkipButton() }

        viewModel.currentScrambledWord.observe(viewLifecycleOwner){
            binding.scrambledWord.text = it
        }
        viewModel.wordCount.observe(viewLifecycleOwner){
            binding.wordCount.text = getString(R.string.word_count, it, MAX_NO_OF_WORDS)
        }
        viewModel.score.observe(viewLifecycleOwner){
            binding.score.text = getString(R.string.score, it)
        }
    }

    private fun onSubmitButton(){
        val playerWord = binding.textInputEditText.text.toString()
        if(viewModel.isValidAnswer(playerWord)){
            setErrorText(false)
            if(!viewModel.nextWord()){
                showDialog()
            }
        }else setErrorText(true)
    }

    private fun onSkipButton(){

        if(viewModel.nextWord()){
            setErrorText(false)
        }else {
            showDialog()
        }
    }

    private fun endGame(){
        activity?.finish()
    }

    private fun restartGame(){
        viewModel.reInitialize()
        setErrorText(false)

    }

    private fun setErrorText(error: Boolean){
        if(error) {
            binding.textInputLayout.isErrorEnabled = true
            binding.textInputLayout.error = "еще разок"
        }
        else{
            binding.textInputLayout.isErrorEnabled = false
            binding.textInputEditText.text = null
        }

    }

    private fun showDialog(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Отлично")
            .setMessage(getString(R.string.dialog_score, viewModel.score.value))
            .setCancelable(false)
            .setPositiveButton("Еще раз"){
                _,_ -> restartGame()
            }
            .setNegativeButton("Выход"){
                    _,_ -> endGame()
            }.show()
    }

}