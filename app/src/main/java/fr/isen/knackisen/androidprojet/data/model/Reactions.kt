package fr.isen.knackisen.androidprojet.data.model

data class Reactions(
    var like: Int,
    var userLiked: Boolean,
    val comments: List<Post>,
)
