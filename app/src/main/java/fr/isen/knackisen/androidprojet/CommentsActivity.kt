package fr.isen.knackisen.androidprojet

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import fr.isen.knackisen.androidprojet.adapter.ListPostAdapter
import fr.isen.knackisen.androidprojet.data.model.Post
import fr.isen.knackisen.androidprojet.data.model.Reactions
import fr.isen.knackisen.androidprojet.data.model.User
import fr.isen.knackisen.androidprojet.databinding.ActivityCommentsBinding


class CommentsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCommentsBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var currentPostRef: DatabaseReference
    private lateinit var commentsAdapter: ListPostAdapter
    private lateinit var listComment: List<Post>
    private lateinit var parentPost: Post
    private var reactionsManager = ReactionsManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listComment = listOf()
        database = Firebase.database



        val postString = intent.getStringExtra("post")
        parentPost = Gson().fromJson(postString, Post::class.java)

        if ( postString.isNullOrEmpty()) {
            finish()
        }
        val parentPost: Post = Gson().fromJson(postString, Post::class.java)

        currentPostRef = database.getReference("posts")
        // put "[-NNLdVxNlfGAPuQSLS-7, -NNLgl8d6NB-l6a8XU9L]" or "-NNLgl8d6NB-l6a8XU9L" in porentId as a list of string
        var parentID:List<String> = parentPost.id.replace("[", "").replace("]", "").split(", ")
        Log.d("Comment", "Parent IDs: $parentID")
        for(id in parentID) {
            Log.d("Comment", "Parent ID: $id")
            currentPostRef = currentPostRef.child(id).child("reactions").child("comments")
        }


        binding.nameUserPostCommentView.text = parentPost.user.name
        binding.contentPostCommentView.text = parentPost.content
        binding.likesCount.text = parentPost.reactions.like.toString()
        var reactionsManager = ReactionsManager()
        reactionsManager.checkalreadyliked(parentPost, binding.likeButton, binding.likesCount)
        binding.likeButton.setOnClickListener {

            reactionsManager.clickLike(parentPost, binding.likeButton,binding.likesCount)
        }




        subToComments(parentPost)


        val newComment = fun () {
            val i = Intent(this@CommentsActivity, AddCommentActivity::class.java)
            i.putExtra("post", postString)
            startActivity(i)
        }
       // var commentList = listOf(Comment("test","This is the message", User(1, "Serg")),Comment("id","Another message", User(2, "L'autre")))

        val toCreateComment = fun (post: Post): Unit {
            val i = Intent(this@CommentsActivity, AddCommentActivity::class.java)
            i.putExtra("post", Gson().toJson(post))
            startActivity(i)
        }

        val onLike = fun (post: Post, button: Button, count:TextView): Unit {
          //  reactionsManager.clickLike(post, button, count)
        }

        val checkLike = fun (post: Post, button: Button, count:TextView): Unit {
              // var reactions = Reactions(0, false, listOf())
            //  reactionsManager.checkalreadyliked(post, button, count)
        }
        val onComment = fun (post:Post): Unit {
            val intent = Intent(this@CommentsActivity, CommentsActivity::class.java)
            intent.putExtra("post", Gson().toJson(post))
            startActivity(intent)
        }
        commentsAdapter = ListPostAdapter(listComment, onComment, toCreateComment, onLike,  checkLike)
        binding.listComments.adapter = commentsAdapter
        binding.listComments.layoutManager = LinearLayoutManager(this)
        binding.commentButton.setOnClickListener { newComment() }
    }



    private fun subToComments(post: Post){

        Log.d("Comment", "Current post ref: $currentPostRef")


        currentPostRef.get().addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                listComment = listOf()
                Log.d("Comment", task.result.toString())
                for (snapshot in task.result!!.children) {
                    Log.d("Comment", "the inside ${snapshot.value}")
                    val id = snapshot.child("id").value.toString()
                    val content = snapshot.child("content").value.toString()
                    val name = snapshot.child("user").child("name").value.toString()
                    val userId = snapshot.child("user").child("id").value.toString()
                    val user = User(userId, name)
                    val comment = Post(id, content, user, Reactions(0,false, listOf()))
                    listComment += comment
                }
                listComment = listComment.drop(1)
                //list without first post




                commentsAdapter.refreshList(listComment )
            } else {
                Log.d("VALUE", task.exception?.message.toString())
            }
        }
    }


    override fun onResume() {
        super.onResume()
        subToComments(parentPost)
    }


}