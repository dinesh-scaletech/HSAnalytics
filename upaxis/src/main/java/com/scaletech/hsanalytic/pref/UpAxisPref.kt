package com.scaletech.hsanalytic.pref

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class UpAxisPref constructor(mContext: Context) {

    var sharedPreferences: SharedPreferences = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE)


    companion object {
        private const val PREF_NAME = "upaxis_data"
        private var instance: UpAxisPref? = null
        internal const val REFERRAL = "referral"

        @Synchronized
        fun getInstance(context: Context): UpAxisPref {
            instance?.let {
                return instance as UpAxisPref
            }?:run{
                instance = UpAxisPref(context)
                return instance as UpAxisPref
            }

        }
    }

    /**
     * Common Set value method for all  types of values
     */
    fun setValue(key: String, value: Any?) {
        when (value) {
            is String? -> edit { it.putString(key, value) }
            is Int -> edit { it.putInt(key, value) }
            is Boolean -> edit { it.putBoolean(key, value) }
            is Float -> edit { it.putFloat(key, value) }
            is Long -> edit { it.putLong(key, value) }
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }
    /**
     * Private inline fun to edit set value in shared preference editor
     */
    private inline fun edit(
        operation: (SharedPreferences.Editor) -> Unit
    ) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        operation(editor)
        editor.apply()
    }


    /**
     * inline common method to get any value from sharedPreference Storage class.
     */
    inline fun <reified T : Any> SharedPreferences.get(key: String, defaultValue: T? = null): T {
        return when (T::class) {
            String::class -> getString(key, defaultValue as? String) as T
            Int::class -> getInt(key, defaultValue as? Int ?: 0) as T
            Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T
            Float::class -> getFloat(key, defaultValue as? Float ?: 0f) as T
            Long::class -> getLong(key, defaultValue as? Long ?: 0L) as T
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    /**
     * Method to get Any value from Shared preference
     */
    inline fun <reified T : Any> getValue(key: String, defaultValue: T? = null): T {
        return sharedPreferences.get(key, defaultValue)
    }

    /**
     * Clear shared preferences which are stored.
     */
    private fun clearUserData() {
        sharedPreferences.edit().clear().apply()
    }

}