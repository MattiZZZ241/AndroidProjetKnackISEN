package fr.isen.knackisen.androidprojet.data.model

data class Comment(
    val content: String,
    val user: User,
) : java.io.Serializable {

}
