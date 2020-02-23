package com.andruid.magic.helpfulsense.database

import android.app.Application
import androidx.lifecycle.LiveData
import com.andruid.magic.helpfulsense.database.entity.Action
import com.andruid.magic.helpfulsense.database.entity.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository class for the database [HelpDatabase]
 */
class DbRepository {
    companion object {
        private lateinit var database: HelpDatabase
        private lateinit var instance: DbRepository

        /**
         * Create the database and repository instance
         * @param application current application
         */
        fun init(application: Application) {
            database = HelpDatabase(application.applicationContext)
            instance = DbRepository()
        }

        /**
         * Get static instance of this class
         * @return static instance of the repository
         */
        fun getInstance(): DbRepository {
            if (!::instance.isInitialized)
                throw Exception("must call init() first in application class")
            return instance
        }
    }

    /**
     * Helper to insert/update action in the database
     * @param action to be added/updated
     */
    fun insert(action: Action) {
        database.actionDao().insert(action)
    }

    /**
     * Helper to insert list of actions in the database by deleting all previous actions
     * @param actions list of actions
     */
    suspend fun insertAllActions(actions: List<Action>) {
        val oldActions = fetchActions()
        database.actionDao().delete(*oldActions.toTypedArray())
        database.actionDao().insert(*actions.toTypedArray())
    }

    /**
     * Helper to delete action from the database
     * @param action to be deleted
     */
    fun delete(action: Action) {
        database.actionDao().delete(action)
    }

    /**
     * Helper to get all the actions in the database as [LiveData]
     * @return liveData of list of actions
     */
    fun fetchLiveActions() = database.actionDao().getLiveActions()

    /**
     * Helper to get all the actions in the database
     * @return list of actions
     */
    suspend fun fetchActions() = withContext(Dispatchers.IO) { database.actionDao().getAllActions() }

    /**
     * Helper to insert list of contacts in the database by deleting all previous contacts
     * @param contacts list of contacts
     */
    suspend fun insertAllContacts(contacts: List<Contact>) {
        val oldContacts = fetchContacts()
        val deletedContacts = oldContacts.minus(contacts)
        database.contactDao().delete(*deletedContacts.toTypedArray())
        database.contactDao().insert(*contacts.toTypedArray())
    }

    /**
     * Helper to delete a contact from the database
     * @param contact to be deleted
     */
    fun delete(contact: Contact) {
        database.contactDao().delete(contact)
    }

    /**
     * Helper to get all the saved contacts as [LiveData]
     * @return liveData of list of contacts
     */
    fun fetchLiveContacts() = database.contactDao().getLiveContacts()

    /**
     * Helper to get all the saved contacts
     * @return list of contacts
     */
    suspend fun fetchContacts() = withContext(Dispatchers.IO) { database.contactDao().getAllContacts() }
}