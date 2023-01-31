package fr.isen.knackisen.androidprojet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import fr.isen.knackisen.androidprojet.data.model.Comment
import fr.isen.knackisen.androidprojet.data.model.Post
import fr.isen.knackisen.androidprojet.data.model.User
import fr.isen.knackisen.androidprojet.databinding.ActivityAddCommentBinding

class AddCommentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddCommentBinding
    private lateinit var postReference: DatabaseReference
    private lateinit var commentsReference: DatabaseReference
    private lateinit var userReference: DatabaseReference

    private lateinit var database: FirebaseDatabase
    private lateinit var listComment: List<Comment>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = Firebase.database
        postReference = database.getReference("posts")
        commentsReference = database.getReference("comments")
        userReference = database.getReference("users")
        listComment = listOf()



        var postString = intent.getStringExtra("post")
        if ( postString.isNullOrEmpty()) {
            Log.e("Get intent", "No parent post")
            finish()
        }
        var parentPost: Post = Gson().fromJson(postString, Post::class.java)
        var parentID:List<String> = parentPost.id.split(",").map { it.trim() }
        for(id in parentID) {
            postReference = postReference.child(id).child("comments")
        }
        postReference= postReference.parent!!
        postReference.get().addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                listComment = listOf()
                for (snapshot in task.result!!.children) {
                    var id = snapshot.child("id").value.toString()
                    var content = snapshot.child("content").value.toString()
                    var name = snapshot.child("user").child("name").value.toString()
                    var user = User("1", name)
                    var comment = Comment(id, content, user)
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
            postReference.child("reactions").child("like").setValue(parentPost.reactions.like)
            binding.likesCount.text = parentPost.reactions.like.toString()
        }

        binding.postButton.setOnClickListener( ) {
            // post to the Real time database (firebase)
            val user = User("1", "name")
            val commentBody = binding.commentText.text.toString()
            binding.commentText.text = null

            var key = commentsReference.push().key
            if (key == null) {
                Log.e("Get key", "Key is null")
                return@setOnClickListener
            }

            var idComment = (parentID + key).toString()
            val comment = Comment(idComment, commentBody, user)

            postReference.child("reactions").child("comments").child(key).setValue(comment)
            commentsReference.child(key).setValue(comment)
            userReference.child(user.id).child("comments").child(key).setValue(comment)

            finish()
        }
    }


}