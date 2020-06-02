package com.bignerdranch.android.criminalintent

import android.app.Application

//Called by the system when your application is first loaded in to memory.
//To set up the repository initialization.
class CriminalIntentApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        CrimeRepository.initialize(this)
    }
}