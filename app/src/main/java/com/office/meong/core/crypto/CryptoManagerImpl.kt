package com.office.meong.core.crypto

import android.content.Context
import android.util.Base64
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplate
import com.google.crypto.tink.RegistryConfiguration
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.aead.PredefinedAeadParameters
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.nio.charset.StandardCharsets
import java.security.GeneralSecurityException
import javax.inject.Inject

class CryptoManagerImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : CryptoManager {

    private val aead: Aead?

    val availability: CryptoAvailability

    init {
        AeadConfig.register()

        val (manager, resolvedAvailability) = buildKeysetManager()
        availability = resolvedAvailability

        aead = manager?.let {
            try {
                it.keysetHandle.getPrimitive(RegistryConfiguration.get(), Aead::class.java)
            } catch (e: Exception) {
                Timber.e("cryto init Fail ${e.message}. ${availability.name}")
                null
            }
        }
    }

    /**
     * 1) Keystore 연동으로 최대 [MAX_KEYSTORE_ATTEMPTS]회 시도
     * 2) 전부 실패하면 masterKeyUri 없이(소프트웨어 키) 초기화 시도
     * 3) 그것도 실패하면 manager = null, UNAVAILABLE
     */
    private fun buildKeysetManager(): Pair<AndroidKeysetManager?, CryptoAvailability> {
        repeat(MAX_KEYSTORE_ATTEMPTS) { attempt ->
            try {
                val manager = AndroidKeysetManager.Builder()
                    .withSharedPref(context, KEYSET_NAME, PREF_FILE_NAME)
                    .withKeyTemplate(KeyTemplate.createFrom(PredefinedAeadParameters.AES256_GCM))
                    .withMasterKeyUri(MASTER_KEY_URI)
                    .build()

                if (manager.isUsingKeystore) {
                    return manager to CryptoAvailability.HARDWARE_BACKED
                }

                return manager to CryptoAvailability.SOFTWARE_FALLBACK
            } catch (e: Exception) {
                Timber.e("buildKeysetManager Fail ${e.message}")
                if (attempt == MAX_KEYSTORE_ATTEMPTS - 1) {
                    // 마지막 시도까지 예외로 실패 → masterKeyUri 없이 소프트웨어 키로 폴백 시도
                    return try {
                        val fallbackManager = AndroidKeysetManager.Builder()
                            .withSharedPref(context, KEYSET_NAME, PREF_FILE_NAME)
                            .withKeyTemplate(KeyTemplate.createFrom(PredefinedAeadParameters.AES256_GCM))
                            .build()
                        fallbackManager to CryptoAvailability.SOFTWARE_FALLBACK
                    } catch (fallbackError: Exception) {
                        Timber.e("buildKeysetManager retry Fail ${fallbackError.message}")
                        null to CryptoAvailability.UNAVAILABLE
                    }
                }
            }
        }
        return null to CryptoAvailability.UNAVAILABLE
    }

    override fun encrypt(plaintext: String, aadContext: AadContext): String {
        requireHardwareBacked()
        val aead = aead ?: throw CryptoNotHardwareBackedException(availability)
        return try {
            val plaintextBytes = plaintext.toByteArray(CHARSET)
            val encryptedBytes = aead.encrypt(plaintextBytes, associatedData(aadContext))
            Base64.encodeToString(encryptedBytes, BASE64_FLAGS)
        } catch (e: Exception) {
            throw EncryptionFailedException(e)
        }
    }

    override fun decrypt(ciphertext: String, aadContext: AadContext): String {
        requireHardwareBacked()
        val aead = aead ?: throw CryptoNotHardwareBackedException(availability)

        val encryptedBytes = try {
            Base64.decode(ciphertext, BASE64_FLAGS)
        } catch (e: IllegalArgumentException) {
            throw DecryptionMalformedInputException(e)
        }

        return try {
            val decryptedBytes = aead.decrypt(encryptedBytes, associatedData(aadContext))
            String(decryptedBytes, CHARSET)
        } catch (e: GeneralSecurityException) {
            throw DecryptionAuthFailedException(e)
        } catch (e: Exception) {
            throw DecryptionMalformedInputException(e)
        }
    }

    /** SOFTWARE_FALLBACK / UNAVAILABLE 상태에서는 정책상 저장 자체를 거부 */
    private fun requireHardwareBacked() {
        if (availability != CryptoAvailability.HARDWARE_BACKED) {
            throw CryptoNotHardwareBackedException(availability)
        }
    }

    private fun associatedData(aadContext: AadContext): ByteArray =
        aadContext.raw.toByteArray(CHARSET)

    companion object {
        private const val MAX_KEYSTORE_ATTEMPTS = 2
        private const val MASTER_KEY_URI = "android-keystore://meong_master_key"
        private const val KEYSET_NAME = "meong_tink_keyset"
        private const val PREF_FILE_NAME = "meong_tink_keyset_prefs"
        private val CHARSET = StandardCharsets.UTF_8
        private const val BASE64_FLAGS = Base64.NO_WRAP
    }
}
