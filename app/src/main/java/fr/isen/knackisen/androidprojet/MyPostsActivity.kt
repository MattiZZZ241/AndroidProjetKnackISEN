package fr.isen.knackisen.androidprojet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import fr.isen.knackisen.androidprojet.adapter.ListPostAdapter
import fr.isen.knackisen.androidprojet.data.model.Comment
import fr.isen.knackisen.androidprojet.data.model.Post
import fr.isen.knackisen.androidprojet.data.model.Reactions
import fr.isen.knackisen.androidprojet.data.model.User
import fr.isen.knackisen.androidprojet.databinding.ActivityMyPostsBinding


class MyPostsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyPostsBinding
    private lateinit var postContainer: List<Post>
    private lateinit var adapter: ListPostAdapter
    var database = Firebase.database
    var reactionsManager = ReactionsManager()
    var currentUserId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPostsBinding.inflate(layoutInflater)
        setContentView(binding.root)



        currentUserId = intent.getStringExtra("UID").toString()
        readDataFromFirebase()
    }


    private fun readDataFromFirebase() {


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

                        commentList.add(
                            Post(
                                commentId, commentContent, commentUser,
                                Reactions(0, false, listOf())
                            )
                        )
                    }
                    if (likes == null) {
                        likes = 0
                    }
                    val reactions = Reactions(likes.toString().toInt(), false, commentList)

                    val post = Post(id, content, user, reactions)
                    if (currentUserId == post.user.id ) {

                        postContainer += post
                        recyclerViewRefresh()
                    }
                }
            } else {
                Log.d("VALUE", task.exception?.message.toString())
            }
        }
    }




    private fun recyclerViewRefresh() {
        val recyclerView = binding.recyclerviewMyPosts
        recyclerView.layoutManager = LinearLayoutManager(this)

        val toCreateComment = fun (post: Post): Unit {
            val i = Intent(this, AddCommentActivity::class.java)
            i.putExtra("post", Gson().toJson(post))
            startActivity(i)
        }

        val onClick = fun (post:Post): Unit {
            val intent = Intent(this, CommentsActivity::class.java)
            intent.putExtra("post", Gson().toJson(post))
            startActivity(intent)
        }

        val onLike = fun (post: Post, button: Button, count: TextView): Unit {
            reactionsManager.clickLike(post, button, count)
        }

        val checkLike = fun (post: Post, button: Button, count: TextView): Unit {
            reactionsManager.checkalreadyliked(post, button, count)
        }

        adapter = ListPostAdapter(arrayListOf(), onClick, toCreateComment, onLike,checkLike)
        recyclerView.adapter = adapter

        // mettre dans le bon ordre les posts (plus récent en premier)
        postContainer = postContainer.reversed()

        adapter.refreshList(postContainer)
    }

    override fun onResume() {
        super.onResume()
        readDataFromFirebase()
    }


}

