package com.andruid.magic.helpfulsense.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.andruid.magic.helpfulsense.database.entity.Contact

@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(contact: Contact)

    @Query("SELECT * FROM contacts")
    fun getLiveContacts(): LiveData<List<Contact>>

    @Query("SELECT * FROM contacts")
    fun getAllContacts(): List<Contact>

    @Delete
    fun delete(contact: Contact)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(contacts: List<Contact>)
}