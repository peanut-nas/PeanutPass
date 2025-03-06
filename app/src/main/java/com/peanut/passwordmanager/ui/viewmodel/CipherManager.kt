package com.peanut.passwordmanager.ui.viewmodel

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import com.peanut.passwordmanager.data.models.Account
import com.peanut.passwordmanager.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

object CipherManager {

    fun getCipher(): Cipher = Cipher.getInstance("${KeyProperties.KEY_ALGORITHM_AES}/${KeyProperties.BLOCK_MODE_CBC}/${KeyProperties.ENCRYPTION_PADDING_PKCS7}")

    fun generateKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        keyStore.getKey(Constants.DATABASE_CIPHER_KEY, null)?.let { println("AndroidKeyStore cache");return it as SecretKey }
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            "AndroidKeyStore"
        )
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            Constants.DATABASE_CIPHER_KEY,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            .setUserAuthenticationRequired(true)
            .setInvalidatedByBiometricEnrollment(true)
            .setUserAuthenticationParameters(3600, KeyProperties.AUTH_BIOMETRIC_STRONG or KeyProperties.AUTH_DEVICE_CREDENTIAL)
            .build()
        keyGenerator.init(keyGenParameterSpec)
        return keyGenerator.generateKey()
    }

    suspend fun encrypt(data: String): Pair<String, String> = withContext(Dispatchers.Default) {
        val cipher = getCipher().apply { init(Cipher.ENCRYPT_MODE, generateKey()) }
        cipher.iv.base64() to cipher.doFinal(data.toByteArray()).base64()
    }

    suspend fun decrypt(data: Account): String = withContext(Dispatchers.Default) {
        val cipher = getCipher().apply { init(Cipher.DECRYPT_MODE, generateKey(), IvParameterSpec(data.iv.base64Decode())) }
        String(cipher.doFinal(data.password.base64Decode()))
    }

    private fun ByteArray.base64(): String {
        return android.util.Base64.encodeToString(this, android.util.Base64.DEFAULT)
    }

    private fun String.base64Decode(): ByteArray {
        return android.util.Base64.decode(this, android.util.Base64.DEFAULT)
    }

}