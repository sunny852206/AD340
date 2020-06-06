package com.vq37vhr.io.ad340

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

sealed class Location {
    data class Zipcode(val zipcode: String) : Location()
}

private const val KEY_ZIPCODE = "key_zipcode"

class LocationRepository(context: Context) {
    private val preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    private val _savedLocation: MutableLiveData<Location> = MutableLiveData()
    val savedLocation: LiveData<Location> = _savedLocation

    init {
        preferences.registerOnSharedPreferenceChangeListener { sharedPreferences, key ->
            if(key != KEY_ZIPCODE) return@registerOnSharedPreferenceChangeListener
            broadcastSavedZipcode()
        }

        broadcastSavedZipcode()
    }

    fun saveLocation(location: Location){
        when (location){
            is Location.Zipcode -> preferences.edit().putString(KEY_ZIPCODE, location.zipcode).apply()
        }
    }

    private fun broadcastSavedZipcode() {
        val zipcode = preferences.getString(KEY_ZIPCODE, "")
        if (zipcode != null && zipcode.isNotBlank()) {
            _savedLocation.value = Location.Zipcode(zipcode)
        }
    }
}