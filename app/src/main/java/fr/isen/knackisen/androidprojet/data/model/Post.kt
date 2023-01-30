package fr.isen.knackisen.androidprojet.data.model

data class Post(
    val id: Int,
    val content: String,
    val user: User,
    val reactions: Reactions,
): java.io.Serializable{

}
