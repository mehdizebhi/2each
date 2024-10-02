package model

data class StoredToken(
    val accessToken: String,
    val refreshToken: String,
    val expiresAt: Long
)
