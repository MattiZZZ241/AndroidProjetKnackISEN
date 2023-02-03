package fr.isen.knackisen.androidprojet.fragment

import fr.isen.knackisen.androidprojet.adapter.ListPostAdapter
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import fr.isen.knackisen.androidprojet.AddCommentActivity
import fr.isen.knackisen.androidprojet.CommentsActivity
import fr.isen.knackisen.androidprojet.ReactionsManager
import fr.isen.knackisen.androidprojet.data.model.Post
import fr.isen.knackisen.androidprojet.data.model.Reactions
import fr.isen.knackisen.androidprojet.data.model.User
import fr.isen.knackisen.androidprojet.databinding.FragmentLeftBinding

class LeftFragment : Fragment() {
    private var _binding: FragmentLeftBinding? = null
    private val binding get() = _binding!!
    private lateinit var postContainer: List<Post>
    private lateinit var adapter: ListPostAdapter
    var database = Firebase.database
    var getUser = Firebase.auth.currentUser
    var storage = Firebase.storage.reference
    val user = User(getUser!!.uid, getUser?.displayName.toString())
    var reactionsManager = ReactionsManager()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLeftBinding.inflate(inflater, container, false)

        readDataFromFirebase()

        return binding.root
    }

    private fun readDataFromFirebase() {
        val database = Firebase.database
        database.reference.child("posts").get().addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                postContainer = listOf()
                for (snapshot in task.result!!.children) {

                    val id = snapshot.child("id").value.toString()
                    val content = snapshot.child("content").value.toString()

                    val name = snapshot.child("user").child("name").value.toString()
                    val userId = snapshot.child("user").child("id").value.toString()
                    val user = User(userId, name)
                    var likes = snapshot.child("reactions").child("like").value


                    // get comment list
                    val commentList = mutableListOf<Post>()
                    for (comment in snapshot.child("comments").children) {
                        val commentId = comment.child("id").value.toString()
                        val commentContent = comment.child("content").value.toString()

                        val commentName = comment.child("user").child("name").value.toString()
                        val commentUserId = comment.child("user").child("id").value.toString()
                        val commentUser = User(commentUserId, commentName)

                        commentList.add(Post(commentId, commentContent, commentUser,Reactions(0,false, listOf())))
                    }
                    if (likes == null) {
                        likes = 0
                    }
                    val reactions = Reactions(likes.toString().toInt(), false, commentList)

                    val post = Post(id, content, user, reactions)

                    postContainer += post
                }
                recyclerViewRefresh()

            } else {
                Log.d("VALUE", task.exception?.message.toString())
            }
        }
    }

    private fun recyclerViewRefresh() {
        val recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(activity)

        val toCreateComment = fun (post: Post): Unit {
            val i = Intent(activity, AddCommentActivity::class.java)
            i.putExtra("post", Gson().toJson(post))
            startActivity(i)
        }

        val onClick = fun (post:Post): Unit {
            val intent = Intent(activity, CommentsActivity::class.java)
            intent.putExtra("post", Gson().toJson(post))
            startActivity(intent)
        }

        val onLike = fun (post: Post, button: Button, count:TextView): Unit {
            reactionsManager.clickLike(post, button, count)
        }

        val checkLike = fun (post: Post, button: Button, count:TextView): Unit {
            reactionsManager.checkalreadyliked(post, button, count)
        }

        val changeImage = fun (post: Post, image: ImageView): Unit {
            getProfilePicture(post.user.id, image)


        }

        adapter = ListPostAdapter(arrayListOf(), onClick, toCreateComment, onLike,checkLike, changeImage)
        recyclerView.adapter = adapter

        // mettre dans le bon ordre les posts (plus récent en premier)
        postContainer = postContainer.reversed()

        adapter.refreshList(postContainer)
    }

    private fun getProfilePicture(id: String, imageView: ImageView) {
        val storage = Firebase.storage.reference
        val imageRef = storage.child("profilePictures/${id}")
        imageRef.downloadUrl.addOnSuccessListener {
            Log.d("IMAGE", it.toString())
            Picasso.get().load(it).into(imageView)
        }.addOnFailureListener {
            Log.d("IMAGE", "error")
        }
    }

    override fun onResume() {
        super.onResume()
        readDataFromFirebase()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("onDestroy", "$this onDestroy")
    }
}