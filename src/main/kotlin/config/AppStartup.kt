package config

import util.CryptoUtils

object AppStartup {

    fun init() {
        CryptoUtils.generateAndSaveKeyPair()
    }

    // -------------------------------
    // private helper
    // -------------------------------
}