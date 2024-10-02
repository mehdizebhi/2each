package model

data class SuggestedStream(
    val username: String,
    val title: String,
    val views: Int,
    val thumbnailPath: String // Path to the stream's thumbnail image
)