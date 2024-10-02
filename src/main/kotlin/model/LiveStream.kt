package model

data class LiveStream(
    val username: String,
    val game: String,
    val title: String,
    val views: Int,
    val type: String,
    val logoPath: String // Path to the streamer's logo image
)