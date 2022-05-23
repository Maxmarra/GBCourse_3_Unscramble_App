package com.example.drilling_part_1_2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.drilling_part_1_2.databinding.FragmentGameBinding
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

        binding.submitButton.setOnClickListener { submitButton() }
        binding.skipButton.setOnClickListener { skipButton() }

        viewModel.currentScrambledWord.observe(viewLifecycleOwner){
            newWord -> binding.scrambledWord.text = newWord
        }

        viewModel.score.observe(viewLifecycleOwner){
                newScore -> binding.score.text = getString(R.string.score, newScore)
        }

        viewModel.wordCount.observe(viewLifecycleOwner){
                newCount -> binding.wordCount.text = getString(R.string.word_count, newCount, MAX_NO_OF_WORDS )
        }
    }

    private fun submitButton(){

        val playerWord = binding.editText.text.toString()

        if(viewModel.isValidAnswer(playerWord)){
            setErrorText(false)
            if(!viewModel.nextWord()){
                showDialog()
            }
        }else{
            setErrorText(true)
        }
        binding.editText.setText("")

    }

    private fun skipButton(){
        if(viewModel.nextWord()){
            binding.editText.setText("")
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
        if(error){
            binding.textInputLayout.isErrorEnabled = true
            binding.textInputLayout.error = "Попробуй еще раз"

        }
        else{
            binding.textInputLayout.isErrorEnabled = false
            binding.editText.error = null
        }
    }

    private fun showDialog(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Отлично")
            .setMessage(getString(R.string.dialog_score, viewModel.score.value))
            .setCancelable(false)
            .setPositiveButton("сыграть еще раз"){
                _,_ -> restartGame()
            }
            .setNegativeButton("выход"){
                    _,_ -> endGame()
            }.show()

    }


}