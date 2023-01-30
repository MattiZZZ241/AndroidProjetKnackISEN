import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import fr.isen.knackisen.androidprojet.R
import fr.isen.knackisen.androidprojet.data.model.Post

class ListPostAdapter(private val list: ArrayList<Post>, private val OnItemClickListener: (Post) -> Unit) : RecyclerView.Adapter<ListPostAdapter.ViewHolder>() {
    // adapter conteneur
    // RecyclerView contenu
    // Holds the views for adding it to text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        // val imageView: ImageView = itemView.findViewById(R.id.imageview)
        val nameView: TextView = itemView.findViewById(R.id.nameUserPostView)
        val imageView: ImageView = itemView.findViewById(R.id.imagePostView)
        val contentView: TextView = itemView.findViewById(R.id.contentPostView)
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

        val itemsViewModel = list[position]
        holder.nameView.text = itemsViewModel.user.name
        holder.contentView.text = itemsViewModel.content



       /* if (itemsViewModel.user.image != "") {
            Picasso.get().load(itemsViewModel.user.image).into(holder.imageView)
        }*/

        holder.itemView.setOnClickListener{
            OnItemClickListener(itemsViewModel)
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun refreshList(newList: List<Post>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
    // return the number of the items in the list
    override fun getItemCount(): Int = list.size

}

