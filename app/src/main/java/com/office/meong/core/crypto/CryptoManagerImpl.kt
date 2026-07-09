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
import java.nio.charset.StandardCharsets
import javax.inject.Inject

class CryptoManagerImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : CryptoManager {

    private val aead: Aead

    // Keystore가 self-test에서 정상 판정됐는지 여부 및 fallback 발생률 모니터링용으로 노출
    val isUsingKeystore: Boolean

    init {
        try {
            AeadConfig.register()

            val manager = AndroidKeysetManager.Builder()
                .withSharedPref(context, KEYSET_NAME, PREF_FILE_NAME)
                .withKeyTemplate(KeyTemplate.createFrom(PredefinedAeadParameters.AES256_GCM))
                .withMasterKeyUri(MASTER_KEY_URI)
                .build()

            aead = manager.keysetHandle.getPrimitive(RegistryConfiguration.get(), Aead::class.java)
            isUsingKeystore = manager.isUsingKeystore
        } catch (e: Exception) {
            throw IllegalStateException("암호화 초기화 실패", e)
        }
    }

    override fun encrypt(plaintext: String): String {
        return try {
            val plaintextBytes = plaintext.toByteArray(CHARSET)
            val encryptedBytes = aead.encrypt(plaintextBytes, ASSOCIATED_DATA)
            Base64.encodeToString(encryptedBytes, BASE64_FLAGS)
        } catch (e: Exception) {
            throw EncryptionException("암호화 실패", e)
        }
    }

    override fun decrypt(ciphertext: String): String {
        return try {
            val encryptedBytes = Base64.decode(ciphertext, BASE64_FLAGS)
            val decryptedBytes = aead.decrypt(encryptedBytes, ASSOCIATED_DATA)
            String(decryptedBytes, CHARSET)
        } catch (e: Exception) {
            throw EncryptionException("복호화 실패", e)
        }
    }

    companion object {
        private const val MASTER_KEY_URI = "android-keystore://meong_master_key"
        private const val KEYSET_NAME = "meong_tink_keyset"
        private const val PREF_FILE_NAME = "meong_tink_keyset_prefs"
        private val CHARSET = StandardCharsets.UTF_8
        private const val BASE64_FLAGS = Base64.NO_WRAP

        // null 대신 빈 배열로. AAD는 "이 암호문이 어떤 컨텍스트에서 만들어졌는지"를
        // 바인딩하는 용도라, 범용 CryptoManager를 다른 데이터에도 재사용할 계획이 있다면
        // 여기 고정 문자열(예: "login_token") 바이트를 넣는 걸 고려할 것.
        private val ASSOCIATED_DATA = ByteArray(0)
    }
}
