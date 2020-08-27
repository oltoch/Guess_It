package com.oltoch.guessit.screens.score

import androidx.lifecycle.ViewModel
import timber.log.Timber

class ScoreViewModel(finalScore: Int) : ViewModel() {



    init {

        Timber.i("Final Score is $finalScore")
    }
}