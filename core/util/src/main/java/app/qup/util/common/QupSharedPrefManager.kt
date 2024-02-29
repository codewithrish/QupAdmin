package app.qup.util.common

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QupSharedPrefManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    fun save(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.commit()
    }
    fun save(key: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.commit()
    }
    fun save(key: String, value: Float) {
        val editor = sharedPreferences.edit()
        editor.putFloat(key, value)
        editor.commit()
    }
    fun save(key: String, value: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(key, value)
        editor.commit()
    }
    fun save(key: String, value: Long) {
        val editor = sharedPreferences.edit()
        editor.putLong(key, value)
        editor.commit()
    }
    fun save(key: String, value: MutableSet<String>) {
        val editor = sharedPreferences.edit()
        editor.putStringSet(key, value)
        editor.commit()
    }
    fun getStringValue(key: String): String? {
        return sharedPreferences.getString(key, "")
    }
    fun getBooleanValue(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }
    fun getFloatValue(key: String): Float {
        return sharedPreferences.getFloat(key, 0f)
    }
    fun getLongValue(key: String): Long {
        return sharedPreferences.getLong(key, 0)
    }
    fun getIntValue(key: String): Int {
        return sharedPreferences.getInt(key, 0)
    }
    fun getStringSetValue(key: String): MutableSet<String>? {
        return sharedPreferences.getStringSet(key, setOf<String>())
    }
    fun deleteKey(key: String) {
        val editor = sharedPreferences.edit()
        editor.remove(key).commit()
    }
    fun clearSharedPreference() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.commit()
    }
}