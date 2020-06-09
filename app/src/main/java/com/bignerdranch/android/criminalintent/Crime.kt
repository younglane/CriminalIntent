package com.bignerdranch.android.criminalintent

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

//PrimaryKey is a column that holds data that is unique for each entry, or row, so that it can be used
//to look up individual entries.
@Entity
data class Crime (@PrimaryKey val id: UUID = UUID.randomUUID(),
                  var title: String ="",
                  var date: Date = Date(),
                  var isSolved: Boolean = false,
                  var suspect: String = "") {
    val photoFileName
        get() = "IMG_$id.jpg"
}