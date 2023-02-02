package fr.isen.knackisen.androidprojet

import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fr.isen.knackisen.androidprojet.data.model.Comment
import fr.isen.knackisen.androidprojet.data.model.Post
import fr.isen.knackisen.androidprojet.data.model.Reactions
import fr.isen.knackisen.androidprojet.data.model.User

class ReactionsManager() {

    val reactions = Reactions(0, false, listOf())
    var database = Firebase.database
    var getUser = Firebase.auth.currentUser
    val user = User(getUser!!.uid, getUser?.displayName.toString())


    fun clickLike(
        parent: Any,
        likeButton: Button,
        likeCount: TextView
    ) {
        // if parent is un Post or an comment
        if (parent is Post) {

            //get reactionstate from database
            database.getReference("users/${user.id}/idpostlike/${parent.id}").get()
                .addOnSuccessListener {
                    // if user already liked
                    if (it != null && it.value != null) {
                        // remove like
                        parent.reactions.like -= 1
                        parent.reactions.userLiked = false
                        // update database
                        database.getReference("posts/${parent.id}/reactions")
                            .setValue(parent.reactions)
                        // update view

                        // envoye du post a la base de donnée user
                        database.getReference("users/${user.id}/idpostlike/${parent.id}")
                            .removeValue()

                    } else {
                        // add like
                        parent.reactions.like += 1
                        parent.reactions.userLiked = true
                        // update database
                        database.getReference("posts/${parent.id}/reactions")
                            .setValue(parent.reactions)

                        database.getReference("users/${user.id}/idpostlike/${parent.id}")
                            .setValue(parent.reactions)


                    }
                }
        }
        else if (parent is Comment) {
            var split = parent.id.split("[", "]")[1]
            split = split.split(", ")[1]

            //get reactionstate from database
            database.getReference("users/${user.id}/idcommentlike/${split}").get()
                .addOnSuccessListener {
                    // if user already liked
                    if (it != null && it.value != null) {
                        // remove like
                        parent.reactions.like -= 1
                        parent.reactions.userLiked = false
                        // update database
                        database.getReference("comments/${split}/reactions")
                            .setValue(parent.reactions)
                        // update view

                        // envoye du post a la base de donnée user
                        database.getReference("users/${user.id}/idcommentlike/${split}")
                            .removeValue()

                    } else {
                        // add like
                        parent.reactions.like += 1
                        parent.reactions.userLiked = true
                        // update database
                        database.getReference("comments/${split}/reactions")
                            .setValue(parent.reactions)

                        database.getReference("users/${user.id}/idcommentlike/${split}")
                            .setValue(parent.reactions)
    }
                }
        }
        checkalreadyliked(parent, likeButton, likeCount)
    }

    fun checkalreadyliked(parent: Any, likeButton: Button, likeCount: TextView) {
        if (parent is Post) {
            database.getReference("users/${user.id}/idpostlike/${parent.id}").get()
                .addOnSuccessListener {
                    if (it != null && it.value != null) {
                        likeButton.text = "Unlike"
                        likeCount.text = parent.reactions.like.toString()
                    } else {
                        likeButton.text = "Like"
                        likeCount.text = parent.reactions.like.toString()
                    }
                }
        }
        else if (parent is Comment) {
            var split = parent.id.split("[", "]")[1]
            split = split.split(", ")[1]

            database.getReference("users/${user.id}/idcommentlike/${split}").get()
                .addOnSuccessListener {
                    if (it != null && it.value != null) {
                        likeButton.text = "Unlike"
                        likeCount.text = parent.reactions.like.toString()
                    } else {
                        likeButton.text = "Like"
                        likeCount.text = parent.reactions.like.toString()
                    }
                }
        }

    }
}






