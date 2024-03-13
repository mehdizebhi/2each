package repository

import util.CryptoUtils
import java.nio.file.Files
import java.nio.file.Path

object TokenRepository {

    private const val TOKEN_PATH_FILE = "tokenpair.txt"

    fun getAccessToken(): String {
        val tokenPairString = Files.readString(Path.of(TOKEN_PATH_FILE))
        val encryptedAccessToken = tokenPairString.substring(0, tokenPairString.indexOf("\n"))
        return CryptoUtils.decrypt(encryptedAccessToken)
    }

    fun getRefreshToken(): String {
        val tokenPairString = Files.readString(Path.of(TOKEN_PATH_FILE))
        val encryptedRefreshToken = tokenPairString.substring(tokenPairString.indexOf("\n") + 1)
        return CryptoUtils.decrypt(encryptedRefreshToken)
    }
}