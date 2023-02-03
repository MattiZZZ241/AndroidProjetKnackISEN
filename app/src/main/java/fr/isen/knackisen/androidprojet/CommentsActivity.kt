package fr.isen.knackisen.androidprojet

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.FirebaseError
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import fr.isen.knackisen.androidprojet.adapter.CommentsAdapter
import fr.isen.knackisen.androidprojet.data.model.Comment
import fr.isen.knackisen.androidprojet.data.model.Post
import fr.isen.knackisen.androidprojet.data.model.Reactions
import fr.isen.knackisen.androidprojet.data.model.User
import fr.isen.knackisen.androidprojet.databinding.ActivityCommentsBinding


class CommentsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCommentsBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var commentsAdapter: CommentsAdapter
    private lateinit var listComment: List<Comment>
    private lateinit var parentPost: Post

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

        binding.nameUserPostCommentView.text = parentPost.user.name
        binding.contentPostCommentView.text = parentPost.content
        binding.likesCount.text = parentPost.reactions.like.toString()
        checkButtonState(parentPost)

        binding.likeButton.setOnClickListener {
            onLike(parentPost)
            checkButtonState(parentPost)
        }




        subToComments(parentPost)


        val newComment = fun () {
            val i = Intent(this@CommentsActivity, AddCommentActivity::class.java)
            i.putExtra("post", postString)
            startActivity(i)
        }
       // var commentList = listOf(Comment("test","This is the message", User(1, "Serg")),Comment("id","Another message", User(2, "L'autre")))

        val toCreateComment = fun (ids: String): Unit {
            val i = Intent(this@CommentsActivity, AddCommentActivity::class.java)
            i.putExtra("id", ids)
            startActivity(i)
        }
        commentsAdapter = CommentsAdapter(listComment, toCreateComment)
        binding.listComments.adapter = commentsAdapter
        binding.listComments.layoutManager = LinearLayoutManager(this)
        binding.commentButton.setOnClickListener { newComment() }
    }



    private fun subToComments(post: Post){
        val ref = database.getReference("posts").child(post.id).child("reactions").child("comments")
        Log.d(TAG, "subToComments: $ref")
        ref.get().addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                listComment = listOf()
                for (snapshot in task.result!!.children) {
                    val id = snapshot.child("id").value.toString()
                    val content = snapshot.child("content").value.toString()
                    val name = snapshot.child("user").child("name").value.toString()
                    val userId = snapshot.child("user").child("id").value.toString()
                    val user = User(userId, name)
                    val comment = Comment(id, content, user)
                    listComment += comment
                }
                listComment = listComment.drop(1)
                //list without first post
                commentsAdapter.updateList(listComment)
            } else {
                Log.d("VALUE", task.exception?.message.toString())
            }
        }
    }


    override fun onResume() {
        super.onResume()
        subToComments(parentPost)
    }

    private fun onLike (post: Post): Unit {
        val moreLess: Int = if (post.reactions.userLiked) -1 else 1

        val database = Firebase.database
        val postRef = database.getReference("posts").child(post.id)
        postRef.child("reactions").child("like").setValue(post.reactions.like + moreLess)
        // readDataFromFirebase()

        post.reactions.userLiked = !post.reactions.userLiked
        post.reactions.like += moreLess
        binding.likesCount.text = post.reactions.like.toString()

    }

    private fun checkButtonState(post: Post): Unit {

        if (post.reactions.userLiked){
            binding.likeButton.text = "unlike"
        }
        else{
            binding.likeButton.text = "like"
        }
    }


}