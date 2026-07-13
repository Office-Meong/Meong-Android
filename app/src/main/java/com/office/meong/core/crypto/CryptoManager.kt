package com.office.meong.core.crypto

interface CryptoManager {
    fun encrypt(plaintext: String): String
    fun decrypt(ciphertext: String): String
}
