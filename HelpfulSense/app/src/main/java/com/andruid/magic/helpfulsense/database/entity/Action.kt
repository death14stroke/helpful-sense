package com.andruid.magic.helpfulsense.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andruid.magic.helpfulsense.model.Category
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "actions")
data class Action(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0,
        val message: String,
        val category: Category
) : Parcelable