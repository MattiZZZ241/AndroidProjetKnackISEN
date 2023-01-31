package fr.isen.knackisen.androidprojet

import android.util.Log
import android.widget.Button
import android.widget.TextView
import fr.isen.knackisen.androidprojet.data.model.Reactions

class ReactionsManager {

    val reactions = Reactions(0, false, listOf())

    fun clickLike(
        update: (Reactions, Button, TextView) -> Unit,
        likeButton: Button,
        likeCount: TextView
    ) {
        if (reactions.userLiked) {
            reactions.like--
            reactions.userLiked = false
        } else {
            reactions.like++
            reactions.userLiked = true
        }
        update(reactions, likeButton, likeCount)
    }
}