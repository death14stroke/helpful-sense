package com.andruid.magic.helpfulsense.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andruid.magic.helpfulsense.model.Category
import kotlinx.android.parcel.Parcelize

/**
 * Database entity for storing emergency actions defined by user
 * @property id unique id for the action [PrimaryKey]
 * @property message text to be sent as SMS along with location
 * @property category type of the alert to be sent
 */
@Parcelize
@Entity(tableName = "actions")
data class Action(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0,
        val message: String,
        val category: Category,
        val order: Int = 0
) : Parcelable