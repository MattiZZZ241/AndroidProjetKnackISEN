package fr.isen.knackisen.androidprojet.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.isen.knackisen.androidprojet.R
import fr.isen.knackisen.androidprojet.data.model.Post

class ListPostAdapter(private var list: List<Post>, private val OnItemClickListener: (Post) -> Unit, val toCreateComment: (Post) -> Unit, val likeAction: (Post, Button, TextView) -> Unit, val checkLike: (Post, Button, TextView) -> Unit, val changePPA: (Post, ImageView) ->Unit) : RecyclerView.Adapter<ListPostAdapter.ViewHolder>() {
    // adapter conteneur
    // RecyclerView contenu
    // Holds the views for adding it to text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        // val imageView: ImageView = itemView.findViewById(R.id.imageview)
        val nameView: TextView = itemView.findViewById(R.id.nameUserPostView)
        val imageView: ImageView = itemView.findViewById(R.id.imagePostView)
        val contentView: TextView = itemView.findViewById(R.id.contentPostView)
        val likeButton: Button = itemView.findViewById(R.id.likeButton)
        val likeCount: TextView = itemView.findViewById(R.id.likesCount)
        val commentButton: Button = itemView.findViewById(R.id.commentButton)
    }
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view_design_list_post, parent, false)
        return ViewHolder(view)
    }

    // binds the list items to a view
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val post = list[position]
        holder.nameView.text = post.user.name
        holder.contentView.text = post.content

        checkLike(post, holder.likeButton, holder.likeCount)

        holder.likeButton.setOnClickListener() {

            likeAction(post, holder.likeButton, holder.likeCount)

        }

        holder.commentButton.setOnClickListener {
            toCreateComment(list[position])
        }

        holder.itemView.setOnClickListener{
            OnItemClickListener(post)
        }

        changePPA(post, holder.imageView)
    }
    @SuppressLint("NotifyDataSetChanged")
    fun refreshList(newList: List<Post>) {
        list = newList
        notifyDataSetChanged()
    }


    // return the number of the items in the list
    override fun getItemCount(): Int = list.size

}

