package util

import java.nio.file.Files
import java.nio.file.Path
import java.security.Key
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.spec.PKCS8EncodedKeySpec
import java.util.Base64
import javax.crypto.Cipher

object CryptoUtils {
    private const val ALGORITHM = "RSA"
    private const val TRANSFORMATION = "RSA/ECB/PKCS1Padding"

    fun encrypt(data: String, publicKey: Key): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val encryptedBytes = cipher.doFinal(data.toByteArray())
        return Base64.getEncoder().encodeToString(encryptedBytes)
    }

    fun decrypt(encryptedData: String, privateKey: Key): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        val encryptedBytes = Base64.getDecoder().decode(encryptedData)
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes)
    }

    fun generateAndSaveKeyPair(filePath: String) {
        val keyPair = generateKeyPair()

        // Check if the file exists
        if (!Files.exists(Path.of(filePath))) {
            // If the file doesn't exist, generate a new key pair and save it to the file
            saveKeyPairToFile(keyPair, filePath)
        }
    }

    fun readPrivateKeyFromFile(filePath: String): Key {
        val privateKeyStr = Files.readString(Path.of(filePath))
        return decodePrivateKey(privateKeyStr)
    }

    private fun generateKeyPair(): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM)
        keyPairGenerator.initialize(2048)
        return keyPairGenerator.generateKeyPair()
    }

    private fun saveKeyPairToFile(keyPair: KeyPair, filePath: String) {
        val publicKeyStr = Base64.getEncoder().encodeToString(keyPair.public.encoded)
        val privateKeyStr = Base64.getEncoder().encodeToString(keyPair.private.encoded)

        val keyPairString = "$publicKeyStr\n$privateKeyStr"
        Files.write(Path.of(filePath), keyPairString.toByteArray())
    }

    private fun decodePrivateKey(privateKeyStr: String): Key {
        val privateKeyBytes = Base64.getDecoder().decode(privateKeyStr)
        return KeyFactory.getInstance(ALGORITHM).generatePrivate(PKCS8EncodedKeySpec(privateKeyBytes))
    }
}