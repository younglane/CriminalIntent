package com.bignerdranch.android.criminalintent

import androidx.lifecycle.ViewModel

//Tied to the lifecycle of the fragement instead of the MainActivity
class CrimeListViewModel : ViewModel() {

    private val crimeRepository = CrimeRepository.get()
    val crimeListLiveData = crimeRepository.getCrimes()

    //similar to adding a crime in repository, but just stored in the ViewModel
    fun addCrime(crime: Crime) {
        crimeRepository.addCrime(crime)
    }
}