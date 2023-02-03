package fr.isen.knackisen.androidprojet

import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import fr.isen.knackisen.androidprojet.data.model.Post
import fr.isen.knackisen.androidprojet.data.model.Reactions
import fr.isen.knackisen.androidprojet.data.model.User

class ReactionsManager() {

    val reactions = Reactions(0, false, listOf())
    var database = Firebase.database
    var getUser = Firebase.auth.currentUser
    val user = User(getUser!!.uid, getUser?.displayName.toString())


    fun clickLike(
        parent: Post,
        likeButton: Button,
        likeCount: TextView
    ) {

        val idList: List<String> = listOf(parent.id.replace("[", "").replace("]", ""))
        Log.d("idList", idList.toString())
        // get the reference of the post
        var postRef = database.getReference("posts")
        for (id in idList) {
            postRef = postRef.child(id).child("reactions").child("comments")
        }
        postRef = postRef.parent!!.parent!!

        Log.d("postRef", postRef.toString())

            //get reactionstate from database
            database.getReference("users/${user.id}/idlike/${idList.last()}").get()
                .addOnSuccessListener {
                    // if user already liked
                    if (it != null && it.value != null) {
                        // remove like
                        parent.reactions.like -= 1
                        parent.reactions.userLiked = false


                        postRef.child("reactions").setValue(parent.reactions)


                        // update view
                        likeButton.text = "like"
                        likeCount.text = parent.reactions.like.toString()



                        // envoye du post a la base de donnée user
                        database.getReference("users/${user.id}/idlike/${idList.last()}").removeValue()

                    } else {
                        // add like
                        parent.reactions.like += 1
                        parent.reactions.userLiked = true

                        // update database
                        postRef.child("reactions").setValue(parent.reactions)

                        likeButton.text = "unlike"
                        likeCount.text = parent.reactions.like.toString()

                        database.getReference("users/${user.id}/idlike/${idList.last()}/path").setValue(idList)
                        database.getReference("users/${user.id}/idlike/${idList.last()}/reactions").setValue(parent.reactions)


                    }
                }
    }

    fun checkalreadyliked(parent: Post, likeButton: Button, likeCount: TextView) {




        // put "[-NNLdVxNlfGAPuQSLS-7, -NNLgl8d6NB-l6a8XU9L]" or "-NNLgl8d6NB-l6a8XU9L" in porentId as a list of string
        var parentID:List<String> = parent.id.replace("[", "").replace("]", "").split(", ")


        val currentId = parentID.last()
        val ref = database.getReference("users/${user.id}/idlike/${currentId}")
            ref.get().addOnSuccessListener {
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






