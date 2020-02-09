package com.andruid.magic.helpfulsense.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.andruid.magic.helpfulsense.database.entity.Contact

@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(contact: Contact)

    @Query("SELECT * FROM contacts")
    fun getLiveContacts(): LiveData<List<Contact>>

    @Query("SELECT * FROM contacts")
    fun getAllContacts(): List<Contact>

    @Query("DELETE FROM contacts WHERE contactID = :contactID")
    fun delete(contactID: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(contacts: List<Contact>)
}