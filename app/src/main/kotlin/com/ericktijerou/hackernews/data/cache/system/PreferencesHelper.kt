package com.ericktijerou.hackernews.data.cache.system

import android.content.Context
import android.content.SharedPreferences

class PreferencesHelper constructor(context: Context) {

    companion object {
        private const val PREF_PACKAGE_NAME = "com.ericktijerou.hackernews.preferences"
        private const val PREF_KEY_LAST_CACHE = "last_cache"
    }

    private val pref: SharedPreferences

    init {
        pref = context.getSharedPreferences(PREF_PACKAGE_NAME, Context.MODE_PRIVATE)
    }

    var lastCacheTime: Long
        get() = pref.getLong(PREF_KEY_LAST_CACHE, 0)
        set(lastCache) = pref.edit().putLong(PREF_KEY_LAST_CACHE, lastCache).apply()

}
