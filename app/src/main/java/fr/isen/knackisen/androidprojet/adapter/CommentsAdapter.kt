package fr.isen.knackisen.androidprojet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.isen.knackisen.androidprojet.R
import fr.isen.knackisen.androidprojet.ReactionsManager
import fr.isen.knackisen.androidprojet.data.model.Comment
import fr.isen.knackisen.androidprojet.data.model.Reactions

class CommentsAdapter (var list: List<Comment>, val toCreateComment: (String)-> Unit ) : RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>() {

    fun updateList(newList: List<Comment>) {
        list = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_cell, parent, false)
        return CommentsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        var reactions = Reactions(0, false, listOf())
        var reactionsManager = ReactionsManager()
        holder.username.text = list[position].user.name
        holder.content.text = list[position].content
        holder.likeCount.text = "0"
        holder.likeButton.setOnClickListener {
            var refresh = fun (reactions: Reactions, button: Button, likes: TextView) {
                if (reactions.userLiked) {
                    //text button
                    button.text = "Unlike"
                } else {
                    button.text = "Like"
                }
                likes.text = reactions.like.toString()

            }
            reactionsManager.clickLike (refresh, holder.likeButton, holder.likeCount)

        }
        holder.commentButton.setOnClickListener {
            toCreateComment(list[position].id)
        }

    }


    class CommentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username: TextView = itemView.findViewById(R.id.autor_name)
        val content: TextView = itemView.findViewById(R.id.content)
        val likeButton: Button = itemView.findViewById(R.id.likeButton)
        val likeCount: TextView = itemView.findViewById(R.id.likesCount)
        val commentButton: Button = itemView.findViewById(R.id.commentButton)
    }

}