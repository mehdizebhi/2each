package internal

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import model.StoredToken
import java.nio.file.Paths
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import kotlin.io.path.*

object TokenStorage {
    private val mapper = jacksonObjectMapper()
    private val tokenFile = Paths.get(System.getProperty("user.home"), ".twoeach_credentials.json");
    private val keyFile = Paths.get(System.getProperty("user.home"), ".twoeach_key");

    private val secretKey: SecretKey by lazy {
        if (keyFile.exists()) {
            val keyBytes = keyFile.readBytes()
            javax.crypto.spec.SecretKeySpec(keyBytes, "AES")
        } else {
            val keyGen = KeyGenerator.getInstance("AES")
            keyGen.init(256)
            val key = keyGen.generateKey()
            keyFile.writeBytes(key.encoded)
            key
        }
    }

    private val cipher = Cipher.getInstance("AES/GCM/NoPadding")
    private val iv = ByteArray(12).apply { SecureRandom().nextBytes(this) }

    fun saveToken(token: StoredToken) {
        val json = mapper.writeValueAsString(token)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, GCMParameterSpec(128, iv))
        val encrypted = cipher.doFinal(json.toByteArray(Charsets.UTF_8))
        val data = mapper.writeValueAsString(
            mapOf(
                "iv" to iv.joinToString(","),
                "data" to encrypted.joinToString(",")
            )
        )
        tokenFile.writeText(data)
    }

    fun loadToken(): StoredToken? {
        if (!tokenFile.exists()) return null
        val dataMap: Map<String, String> = mapper.readValue(tokenFile.toFile(), Map::class.java) as Map<String, String>
        val iv = dataMap["iv"]?.split(",")?.map { it.toByte() }?.toByteArray() ?: return null
        val encrypted = dataMap["data"]?.split(",")?.map { it.toByte() }?.toByteArray() ?: return null
        cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(128, iv))
        val decryptedBytes = cipher.doFinal(encrypted)
        val json = String(decryptedBytes, Charsets.UTF_8)
        return mapper.readValue(json, StoredToken::class.java)
    }

    fun deleteToken() {
        tokenFile.deleteIfExists()
    }
}