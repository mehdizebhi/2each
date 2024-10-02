package model

data class Stream(
    val id: String,
    val userId: String,
    val userName: String,
    val gameId: String,
    val gameName: String,
    val type: String,
    val title: String,
    val viewerCount: Int,
    val startedAt: String,
    val language: String,
    val thumbnailUrl: String,
    val tagIds: List<String>,
    val isMature: Boolean
)