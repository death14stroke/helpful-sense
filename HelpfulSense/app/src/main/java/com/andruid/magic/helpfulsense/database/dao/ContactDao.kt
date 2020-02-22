package com.andruid.magic.helpfulsense.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.andruid.magic.helpfulsense.database.entity.Contact

/**
 * Dao for [Contact] entity
 */
@Dao
interface ContactDao {
    /**
     * Query to insert/update contact
     * @param contact to be added/updated
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(contact: Contact)

    /**
     * Query to insert list of contacts
     * @param contacts list of contacts to be added/updated
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(contacts: List<Contact>)

    /**
     * Query to fetch all contacts as [LiveData]
     * @return liveData of list of contacts
     */
    @Query("SELECT * FROM contacts")
    fun getLiveContacts(): LiveData<List<Contact>>

    /**
     * Query to fetch all contacts
     * @return list of contacts
     */
    @Query("SELECT * FROM contacts")
    fun getAllContacts(): List<Contact>

    /**
     * Query to delete contact
     * @param contact to be deleted
     */
    @Delete
    fun delete(contact: Contact)
}