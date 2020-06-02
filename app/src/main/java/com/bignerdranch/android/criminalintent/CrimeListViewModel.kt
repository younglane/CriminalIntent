package com.bignerdranch.android.criminalintent

import androidx.lifecycle.ViewModel

//Tied to the lifecycle of the fragement instead of the MainActivity
class CrimeListViewModel : ViewModel() {

    private val crimeRepository = CrimeRepository.get()
    val crimeListLiveData = crimeRepository.getCrimes()

}