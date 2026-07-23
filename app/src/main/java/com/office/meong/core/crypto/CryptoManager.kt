package com.office.meong.core.crypto

interface CryptoManager {
    fun encrypt(plaintext: String, aadContext: AadContext): String
    fun decrypt(ciphertext: String, aadContext: AadContext): String
}
