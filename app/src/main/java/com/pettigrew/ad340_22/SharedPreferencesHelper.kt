package com.pettigrew.ad340_22

import android.content.SharedPreferences


class SharedPreferencesHelper(private val mSharedPreferences: SharedPreferences) {
    /**
     * Saves the given string to [SharedPreferences].
     *
     * @param key key to associate with saved entry [SharedPreferences].
     * @param message contains string entry to save to [SharedPreferences].
     * @return `true` if writing to [SharedPreferences] succeeded. `false`
     * otherwise.
     */
    fun saveEntry(key: String?, message: String?): Boolean {
        // Start a SharedPreferences transaction.
        val editor = mSharedPreferences.edit()
        editor.putString(key, message)

        // Commit changes to SharedPreferences & return success/failure result
        return editor.commit()
    }

    fun getEntry(key: String?): String? {
        return mSharedPreferences.getString(key, "")
    }
}