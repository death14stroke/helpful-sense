package com.andruid.magic.helpfulsense.database

import androidx.room.TypeConverter
import com.andruid.magic.helpfulsense.model.Category
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wafflecopter.multicontactpicker.RxContacts.PhoneNumber

/**
 * Type converters for storing complex objects in Room database
 */
object RoomTypeConverters {
    @JvmStatic
    @TypeConverter
    fun categoryToString(category: Category): String = Gson().toJson(category)

    @JvmStatic
    @TypeConverter
    fun stringToCategory(cat: String): Category = Gson().fromJson(cat, Category::class.java)

    @JvmStatic
    @TypeConverter
    fun phoneNumbersToString(phoneNumbers: List<PhoneNumber>): String = Gson().toJson(phoneNumbers)

    @JvmStatic
    @TypeConverter
    fun stringToPhoneNumbers(numbers: String): List<PhoneNumber> =
            Gson().fromJson(numbers, object : TypeToken<List<PhoneNumber>>() {}.type)
}