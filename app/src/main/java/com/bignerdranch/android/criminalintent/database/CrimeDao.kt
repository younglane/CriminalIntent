package com.bignerdranch.android.criminalintent.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.bignerdranch.android.criminalintent.Crime
import java.util.*

//Lets Room know that CrimeDao is one of your data access objects.
@Dao
interface CrimeDao{

    //Both get functions are meat to pull information out of the database
    //Gets a list of crimes
    @Query("SELECT * FROM crime")
    fun getCrimes(): LiveData<List<Crime>>

    //Gets a specific crime
    @Query("SELECT * FROM crime WHERE id=(:id)")
    fun getCrime(id: UUID): LiveData<Crime?>

    //Uses the ID stored in that crime to find the associated row and updates the data
    @Update
    fun updateCrime(crime: Crime)

    //Adds the crime to the database
    @Insert
    fun addCrime(crime: Crime)

}