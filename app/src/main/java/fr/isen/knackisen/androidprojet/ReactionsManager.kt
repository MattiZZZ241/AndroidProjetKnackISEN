package fr.isen.knackisen.androidprojet

import fr.isen.knackisen.androidprojet.data.model.Comment
import fr.isen.knackisen.androidprojet.data.model.Reactions

class ReactionsManager {

    val reactions = Reactions(0, false, listOf())


    public fun clickLike(update: (Reactions) -> Unit) {
        if (reactions.userLiked) {
            reactions.like--
            reactions.userLiked = false
        } else {
            reactions.like++
            reactions.userLiked = true
        }
        update(reactions)
    }

}