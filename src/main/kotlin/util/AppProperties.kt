package util

import java.io.InputStream
import java.util.Properties

object AppProperties {

    private val properties = Properties()

    init {
        // Load properties from the classpath
        val inputStream: InputStream = javaClass.classLoader.getResourceAsStream("application.properties")
        properties.load(inputStream)
    }

    fun getProperty(key: String): String {
        return properties.getProperty(key)
    }
}