package fr.isen.knackisen.androidprojet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.isen.knackisen.androidprojet.R
import fr.isen.knackisen.androidprojet.ReactionsManager
import fr.isen.knackisen.androidprojet.data.model.Post

class CommentsAdapter (var post: List<Post>, val toCreateComment: (Post)-> Unit, val likeAction: (Post, Button, TextView) -> Unit, val onComment: (post:Post) -> Unit, val checkLike: (Post, Button, TextView) -> Unit) : RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>() {

    fun updateList(newList: List<Post>) {
        post = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_cell, parent, false)
        return CommentsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return post.size
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        var reactionsManager = ReactionsManager()
        var postCurrent: Post = post[position]

        holder.username.text = postCurrent.user.name
        holder.content.text = postCurrent.content
       // checkLike(postCurrent, holder.likeButton, holder.likeCount)
        reactionsManager.checkalreadyliked(postCurrent, holder.likeButton, holder.likeCount)
        holder.likeButton.setOnClickListener() {
            likeAction(postCurrent, holder.likeButton, holder.likeCount)
            reactionsManager.clickLike(postCurrent, holder.likeButton, holder.likeCount)
        }


        holder.commentButton.setOnClickListener {
            toCreateComment(postCurrent)
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