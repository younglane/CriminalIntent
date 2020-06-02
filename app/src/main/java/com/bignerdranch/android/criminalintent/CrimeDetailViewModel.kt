package com.bignerdranch.android.criminalintent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.util.*

class CrimeDetailViewModel() : ViewModel() {

    private val crimeRepository = CrimeRepository.get()
    private val crimeIdLiveData = MutableLiveData<UUID>()

    //Returns a new LiveData object, whose value gets updated every time a new value gets set on the
    //trigger LiveData instance(crimeIdLiveData)
    var crimeLiveData: LiveData<Crime?> =
        Transformations.switchMap(crimeIdLiveData) {crimeId ->
            crimeRepository.getCrime(crimeId)
        }

    //know which crime it needs to load
    fun loadCrime(crimeId: UUID){
        crimeIdLiveData.value = crimeId
    }

    //accepts a Crime and writees it to the database
    fun saveCrime(crime: Crime) {
        crimeRepository.updateCrime(crime)
    }
}