import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.isen.knackisen.androidprojet.R
import fr.isen.knackisen.androidprojet.ReactionsManager
import fr.isen.knackisen.androidprojet.data.model.Post
import fr.isen.knackisen.androidprojet.data.model.Reactions

class ListPostAdapter(private var list: List<Post>, private val OnItemClickListener: (Post) -> Unit,  val toCreateComment: (String)-> Unit) : RecyclerView.Adapter<ListPostAdapter.ViewHolder>() {
    // adapter conteneur
    // RecyclerView contenu
    // Holds the views for adding it to text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        // val imageView: ImageView = itemView.findViewById(R.id.imageview)
        val nameView: TextView = itemView.findViewById(R.id.nameUserPostView)
        val imageView: ImageView = itemView.findViewById(R.id.imagePostView)
        val contentView: TextView = itemView.findViewById(R.id.pseudo2PostView)
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
        var reactionsManager = ReactionsManager()

        val itemsViewModel = list[position]
        holder.nameView.text = itemsViewModel.user.name
        holder.contentView.text = itemsViewModel.content
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

        /* if (itemsViewModel.user.image != "") {
             Picasso.get().load(itemsViewModel.user.image).into(holder.imageView)
         }*/

        holder.itemView.setOnClickListener{
            OnItemClickListener(itemsViewModel)
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun refreshList(newList: List<Post>) {
        list = newList
        notifyDataSetChanged()
    }
    // return the number of the items in the list
    override fun getItemCount(): Int = list.size

}

