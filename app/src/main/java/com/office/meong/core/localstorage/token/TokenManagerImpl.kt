package com.office.meong.core.localstorage.token

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.office.meong.core.common.util.suspendRunCatching
import com.office.meong.core.crypto.CryptoManager
import com.office.meong.core.localstorage.qualifier.AuthDataStore
import com.office.meong.core.localstorage.token.TokenConstant.KEY_ACCESS_TOKEN
import com.office.meong.core.localstorage.token.TokenConstant.KEY_REFRESH_TOKEN
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class TokenManagerImpl @Inject constructor(
    @param:AuthDataStore private val dataStore: DataStore<Preferences>,
    private val cryptoManager: CryptoManager
) : TokenManager {
    @Volatile
    private var cachedAccessToken: String? = null

    override suspend fun saveAccessToken(token: String) {
        suspendRunCatching {
            cachedAccessToken = token
            val encrypted = cryptoManager.encrypt(token)

            dataStore.edit {
                it[KEY_ACCESS_TOKEN] = encrypted
            }
        }.onFailure { Timber.e(it, "AccessToken 저장 실패") }
    }

    override suspend fun saveRefreshToken(token: String) {
        suspendRunCatching {
            val encrypted = cryptoManager.encrypt(token)

            dataStore.edit {
                it[KEY_REFRESH_TOKEN] = encrypted
            }
        }.onFailure { Timber.e(it, "RefreshToken 저장 실패") }
    }

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        suspendRunCatching {
            cachedAccessToken = accessToken
            val encryptedAccess = cryptoManager.encrypt(accessToken)
            val encryptedRefresh = cryptoManager.encrypt(refreshToken)

            dataStore.edit {
                it[KEY_ACCESS_TOKEN] = encryptedAccess
                it[KEY_REFRESH_TOKEN] = encryptedRefresh
            }
        }.onFailure { Timber.e(it, "토큰 일괄 저장 실패") }
    }

    override suspend fun getAccessToken(): String? {
        if (cachedAccessToken != null) {
            return cachedAccessToken
        }

        return suspendRunCatching {
            val encrypted = dataStore.data.map { it[KEY_ACCESS_TOKEN] }.first()
            encrypted?.let { cryptoManager.decrypt(it) }?.also {
                cachedAccessToken = it
            }
        }.onFailure { Timber.e(it, "AccessToken 로드 실패") }.getOrNull()
    }

    override suspend fun getRefreshToken(): String? {
        return suspendRunCatching {
            val encrypted = dataStore.data.map { it[KEY_REFRESH_TOKEN] }.first()
            encrypted?.let {
                cryptoManager.decrypt(it)
            }
        }.onFailure { Timber.e(it, "RefreshToken 로드 실패") }.getOrNull()
    }

    override suspend fun clearTokens() {
        suspendRunCatching {
            cachedAccessToken = null
            dataStore.edit {
                it.remove(KEY_ACCESS_TOKEN)
                it.remove(KEY_REFRESH_TOKEN)
            }
        }.onFailure {
            Timber.e(it, "토큰 삭제 실패")
        }
    }
}
