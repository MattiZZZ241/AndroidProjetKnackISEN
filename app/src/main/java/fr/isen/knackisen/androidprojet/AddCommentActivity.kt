package fr.isen.knackisen.androidprojet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import fr.isen.knackisen.androidprojet.data.model.Post
import fr.isen.knackisen.androidprojet.data.model.Reactions
import fr.isen.knackisen.androidprojet.data.model.User
import fr.isen.knackisen.androidprojet.databinding.ActivityAddCommentBinding

class AddCommentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddCommentBinding
    private lateinit var commentReference: DatabaseReference
    private lateinit var currentPostRef: DatabaseReference
    private lateinit var userReference: DatabaseReference

    private lateinit var database: FirebaseDatabase
    private lateinit var listComment: List<Post>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = Firebase.database
        commentReference = database.getReference("comments")

        userReference = database.getReference("users")
        listComment = listOf()


        currentPostRef = database.getReference("posts")

        var postString = intent.getStringExtra("post")
        if ( postString.isNullOrEmpty()) {
            Log.e("Get intent", "No parent post")
            finish()
        }
        var parentPost: Post = Gson().fromJson(postString, Post::class.java)
        // put "[-NNLdVxNlfGAPuQSLS-7, -NNLgl8d6NB-l6a8XU9L]" or "-NNLgl8d6NB-l6a8XU9L" in porentId as a list of string
        var parentID:List<String> = listOf(parentPost.id.replace("[", "").replace("]", ""))
        for(id in parentID) {
            currentPostRef = currentPostRef.child(id).child("reactions").child("comments")
        }
        currentPostRef= currentPostRef.parent!!.parent!!
        currentPostRef.get().addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                listComment = listOf()
                for (snapshot in task.result!!.children) {
                    var id = snapshot.child("id").value.toString()
                    var content = snapshot.child("content").value.toString()
                    var name = snapshot.child("user").child("name").value.toString()
                    var userId = snapshot.child("user").child("id").value.toString()

                    var user = User(userId, name)
                    var comment = Post(id, content, user, Reactions(0,false, listOf()))
                    listComment += comment
                }
            } else {
                Log.d("Get BDD", task.exception?.message.toString())
            }
        }

        binding.nameUserPostView.text = parentPost.user.name
        binding.contentPostView.text = parentPost.content
        binding.likesCount.text = parentPost.reactions.like.toString()

        binding.likeButton.setOnClickListener() {
            if(parentPost.reactions.userLiked) {
                parentPost.reactions.like -= 1
                parentPost.reactions.userLiked = false
            } else {
                parentPost.reactions.like += 1
                parentPost.reactions.userLiked = true
            }
            currentPostRef.child("reactions").child("like").setValue(parentPost.reactions.like)
            binding.likesCount.text = parentPost.reactions.like.toString()
        }


        binding.postButton.setOnClickListener( ) {
            // post to the Real time database (firebase)
            var currentUser = Firebase.auth.currentUser
            if (currentUser != null) {
                if (currentUser.uid == null) {
                    Log.e("Get user", "No user connected")
                    finish()
                }
            }
            val user = User(currentUser!!.uid, currentUser.displayName.toString())
            val commentBody = binding.commentText.text.toString()
            binding.commentText.text = null

            var key = commentReference.push().key
            if (key == null) {
                Log.e("Get key", "Key is null")
                return@setOnClickListener
            }

            var idComment = (parentID + key).toString()
            val comment = Post(idComment, commentBody, user,Reactions(0,false, listOf()))

            currentPostRef.child("reactions").child("comments").child(key).setValue(comment)
            userReference.child(user.id).child("comments").child(key).setValue(comment)

            finish()
        }
    }


}