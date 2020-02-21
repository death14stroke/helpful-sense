package com.andruid.magic.helpfulsense.database

import android.app.Application
import com.andruid.magic.helpfulsense.database.entity.Action
import com.andruid.magic.helpfulsense.database.entity.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DbRepository {
    companion object {
        private lateinit var database: HelpDatabase
        private lateinit var instance: DbRepository

        fun init(application: Application) {
            database = HelpDatabase(application.applicationContext)
            instance = DbRepository()
        }

        fun getInstance(): DbRepository {
            if (!::instance.isInitialized)
                throw Exception("must call init() first in application class")
            return instance
        }
    }

    fun insert(action: Action) {
        database.actionDao().insert(action)
    }

    fun delete(action: Action) {
        database.actionDao().delete(action)
    }

    fun fetchLiveActions() = database.actionDao().getLiveActions()

    suspend fun fetchActions() = withContext(Dispatchers.IO) { database.actionDao().getAllActions() }

    suspend fun insertAll(contacts: List<Contact>) {
        val oldContacts = fetchContacts()
        val deletedContacts = oldContacts.minus(contacts)
        deletedContacts.forEach { database.contactDao().delete(it) }
        database.contactDao().insertAll(contacts)
    }

    fun delete(contact: Contact) {
        database.contactDao().delete(contact)
    }

    fun fetchLiveContacts() = database.contactDao().getLiveContacts()

    suspend fun fetchContacts() = withContext(Dispatchers.IO) { database.contactDao().getAllContacts() }
}