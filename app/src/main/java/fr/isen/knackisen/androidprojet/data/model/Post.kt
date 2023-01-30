package fr.isen.knackisen.androidprojet.data.model

data class Post(
    val id: String,
    val content: String,
    val user: User,
    val reactions: Reactions,
)