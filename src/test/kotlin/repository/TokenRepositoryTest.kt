package repository

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class TokenRepositoryTest {

    @Test
    fun getAccessToken() {
        println(TokenRepository.getAccessToken())
    }

    @Test
    fun getRefreshToken() {
        println(TokenRepository.getRefreshToken())
    }
}