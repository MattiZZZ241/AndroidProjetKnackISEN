package fr.isen.knackisen.androidprojet

import ListPostAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import fr.isen.knackisen.androidprojet.data.model.Comment
import fr.isen.knackisen.androidprojet.data.model.Post
import fr.isen.knackisen.androidprojet.data.model.Reactions
import fr.isen.knackisen.androidprojet.data.model.User
import fr.isen.knackisen.androidprojet.databinding.ActivityListPostBinding

class ListPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListPostBinding
    private lateinit var postContaiener: List<Post>






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

         readDataFromFirebase()
    }

    private fun readDataFromFirebase() {
        val database = Firebase.database
        database.reference.child("posts").get().addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                postContaiener = listOf()
                for (snapshot in task.result!!.children) {

                    val id = snapshot.child("id").value.toString()
                    val content = snapshot.child("content").value.toString()
                    Log.d("VALUE", content)

                    val name = snapshot.child("user").child("name").value.toString()
                    val userId = snapshot.child("user").child("id").value.toString()
                    val user = User(userId, name)

                    // get comment list
                  val commentList = mutableListOf<Comment>()
                    for (comment in snapshot.child("comment").children) {
                        val commentId = comment.child("id").value.toString()
                        val commentContent = comment.child("content").value.toString()

                        val commentName = comment.child("user").child("name").value.toString()
                        val commentUserId = comment.child("user").child("id").value.toString()
                        val commentUser = User(commentUserId, commentName)

                        commentList.add(Comment(commentId, commentContent, commentUser))
                    }
                    Log.d("like", snapshot.child("reactions").child("like").value.toString())

//                    val like = snapshot.child("reactions").child("like").value.toString().toInt()

                    val reactions = Reactions(2, false, commentList)
                    Log.d("reactions", commentList.toString())

                    val post = Post(id, content, user, reactions)


                    postContaiener += post


                }
                Log.i("TAG", "Value is: ${postContaiener}")
                recyclerViewRefresh()

            } else {
                Log.d("VALUE", task.exception?.message.toString())
            }
        }
    }

    private fun recyclerViewRefresh() {
        val recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(this)
        var toCreateComment = fun (post: Post): Unit {
            val i = Intent(this@ListPostActivity, AddCommentActivity::class.java)

            i.putExtra("post", Gson().toJson(post))
            startActivity(i)
        }
        var onClick = fun (post:Post): Unit {
            Log.d("post", post.toString())
        }
        recyclerView.adapter =
            ListPostAdapter(arrayListOf(), onClick, toCreateComment)

        val adapter = recyclerView.adapter as ListPostAdapter
        adapter.refreshList(postContaiener)

    }

    override fun onDestroy() {
            super.onDestroy()
            Log.d("onDestroy", "$this onDestroy")
        }

}