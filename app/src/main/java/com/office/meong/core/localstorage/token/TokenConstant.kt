package com.office.meong.core.localstorage.token

import androidx.datastore.preferences.core.stringPreferencesKey

object TokenConstant {
    val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
    val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")
}
