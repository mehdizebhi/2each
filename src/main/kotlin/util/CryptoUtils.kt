package util

import java.nio.file.Files
import java.nio.file.Path
import java.security.Key
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import javax.crypto.Cipher

object CryptoUtils {
    private const val ALGORITHM = "RSA"
    private const val TRANSFORMATION = "RSA/ECB/PKCS1Padding"
    private const val PATH = "keypair.txt"

    fun encrypt(data: String): String {
        val publicKey = readPublicKeyFromFile()
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val encryptedBytes = cipher.doFinal(data.toByteArray())
        return Base64.getEncoder().encodeToString(encryptedBytes)
    }

    fun decrypt(encryptedData: String): String {
        val privateKey = readPrivateKeyFromFile()
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        val encryptedBytes = Base64.getDecoder().decode(encryptedData)
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes)
    }

    fun generateAndSaveKeyPair() {
        val keyPair = generateKeyPair()

        // Check if the file exists
        if (!Files.exists(Path.of(PATH))) {
            // If the file doesn't exist, generate a new key pair and save it to the file
            saveKeyPairToFile(keyPair)
        }
    }

    private fun readPublicKeyFromFile(): Key {
        val keyPairString = Files.readString(Path.of(PATH))
        val publicKeyStr = keyPairString.substring(0, keyPairString.indexOf("\n"))
        return decodePublicKey(publicKeyStr)
    }

    private fun readPrivateKeyFromFile(): Key {
        val keyPairString = Files.readString(Path.of(PATH))
        val privateKeyStr = keyPairString.substring(keyPairString.indexOf("\n") + 1)
        return decodePrivateKey(privateKeyStr)
    }

    private fun generateKeyPair(): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM)
        keyPairGenerator.initialize(2048)
        return keyPairGenerator.generateKeyPair()
    }

    private fun saveKeyPairToFile(keyPair: KeyPair) {
        val publicKeyStr = Base64.getEncoder().encodeToString(keyPair.public.encoded)
        val privateKeyStr = Base64.getEncoder().encodeToString(keyPair.private.encoded)

        val keyPairString = "$publicKeyStr\n$privateKeyStr"
        Files.write(Path.of(PATH), keyPairString.toByteArray())
    }

    private fun decodePrivateKey(privateKeyStr: String): Key {
        val privateKeyBytes = Base64.getDecoder().decode(privateKeyStr)
        return KeyFactory.getInstance(ALGORITHM).generatePrivate(PKCS8EncodedKeySpec(privateKeyBytes))
    }

    private fun decodePublicKey(publicKeyStr: String): Key {
        val publicKeyBytes = Base64.getDecoder().decode(publicKeyStr)
        return KeyFactory.getInstance(ALGORITHM).generatePublic(X509EncodedKeySpec(publicKeyBytes))
    }
}