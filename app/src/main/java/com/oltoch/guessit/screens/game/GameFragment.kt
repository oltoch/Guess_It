/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.oltoch.guessit.screens.game

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.oltoch.guessit.R
import com.oltoch.guessit.databinding.GameFragmentBinding
import timber.log.Timber

/**
 * Fragment where the game is played
 */
class GameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel

    private lateinit var binding: GameFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.game_fragment,
            container,
            false
        )

        Timber.i("Called GameViewModelProvider")
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        // Set the view model for data binding. This allows the bound layout access to all of the
        // data in the ViewModel
        binding.gameViewModel = viewModel

        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData Updates
        binding.lifecycleOwner = this
//        No longer needed because of data binding that's been setup in the xml
//        binding.correctButton.setOnClickListener {
//            viewModel.onCorrect()
//        }
//        binding.skipButton.setOnClickListener {
//            viewModel.onSkip()
//        }

//        viewModel.score.observe(viewLifecycleOwner, { newScore ->
//            binding.scoreText.text = newScore.toString()
//        })
//        viewModel.word.observe(viewLifecycleOwner, { newWord ->
//            binding.wordText.text = newWord
//        })
//        viewModel.currentTime.observe(viewLifecycleOwner, { newTime ->
//            binding.timerText.text = DateUtils.formatElapsedTime(newTime)
//        })
        //Sets up event listening to navigate the player when the game is finished
        viewModel.eventGameFinished.observe(viewLifecycleOwner, { hasFinished ->
            if (hasFinished) {
                gameFinished()
                viewModel.onGameFinishedComplete()
            }
        })
        viewModel.eventBuzz.observe(viewLifecycleOwner, { buzzType ->
            if (buzzType != GameViewModel.BuzzType.NO_BUZZ) {
                buzz(buzzType.pattern)
                viewModel.onBuzzComplete()
            }
        })

        return binding.root
    }

    /**
     * Called when the game is finished
     */
    private fun gameFinished() {
        val action = GameFragmentDirections.actionGameToScore(viewModel.score.value ?: 0)
        findNavController(this).navigate(action)
    }

    private fun buzz(pattern: LongArray) {
        val buzzer = activity?.getSystemService(Activity.VIBRATOR_SERVICE) as Vibrator
        buzzer?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                buzzer.vibrate(VibrationEffect.createWaveform(pattern, -1))
            else
                buzzer.vibrate(pattern, -1)
        }
    }
}