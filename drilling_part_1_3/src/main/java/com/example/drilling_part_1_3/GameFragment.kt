package com.example.drilling_part_1_3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.viewModels
import com.example.drilling_part_1_3.databinding.FragmentGameBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class GameFragment : Fragment() {

    private val viewModel:GameViewModel by viewModels()
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

        val playersWord = binding.inputField.text.toString()

        if(viewModel.isAnswerCorrect(playersWord)){
            setErrorText(false)
            if(!viewModel.nextWord()){
                showDialog()
            }
        }else {setErrorText(true)}
        binding.inputField.setText("")
    }

    private fun onSkipButton(){
        setErrorText(false)
        if(viewModel.nextWord()){
            setErrorText(false)
        }else showDialog()
        binding.inputField.setText("")
    }
    private fun exitGame(){
        activity?.finish()
    }

    private fun restartGame(){
        setErrorText(false)
        viewModel.reInitialize()
    }

    private fun setErrorText(error: Boolean){
        if(error){
            binding.inputLayout.isErrorEnabled = true
            binding.inputLayout.error = "Еще разок"
        }else{
            binding.inputLayout.isErrorEnabled = false
            binding.inputField.error = null

        }
    }

    private fun showDialog(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Игра закончена")
            .setMessage(getString(R.string.dialog_message, viewModel.score.value))
            .setCancelable(false)
            .setPositiveButton("Еще раз"){
                _,_ -> restartGame()
            }
            .setNegativeButton("Выйти"){
                    _,_ -> exitGame()
            }.show()

    }


}