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
                        likeButton.text = "like"
                        likeCount.text = parent.reactions.like.toString()



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

                        likeButton.text = "unlike"
                        likeCount.text = parent.reactions.like.toString()


                        database.getReference("users/${user.id}/idpostlike/${parent.id}")
                            .setValue(parent.reactions)


                    }
                }
        }

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
    }
}






