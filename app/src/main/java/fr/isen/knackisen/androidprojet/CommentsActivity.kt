package fr.isen.knackisen.androidprojet

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.FirebaseError
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fr.isen.knackisen.androidprojet.adapter.CommentsAdapter
import fr.isen.knackisen.androidprojet.data.model.Comment
import fr.isen.knackisen.androidprojet.data.model.Post
import fr.isen.knackisen.androidprojet.data.model.User
import fr.isen.knackisen.androidprojet.databinding.ActivityCommentsBinding


class CommentsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCommentsBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var commentsAdapter: CommentsAdapter
private lateinit var listComment: List<Comment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listComment = listOf()
        database = Firebase.database
        subToComments()


        var newComment = fun () {
            val i = Intent(this@CommentsActivity, AddCommentActivity::class.java)
            startActivity(i)
        }
       // var commentList = listOf(Comment("test","This is the message", User(1, "Serg")),Comment("id","Another message", User(2, "L'autre")))

        var toCreateComment = fun (ids: String): Unit {
            val i = Intent(this@CommentsActivity, AddCommentActivity::class.java)
            i.putExtra("id", ids)
            startActivity(i)
        }
        commentsAdapter = CommentsAdapter(listComment, toCreateComment)
        binding.listComments.adapter = commentsAdapter
        binding.listComments.layoutManager = LinearLayoutManager(this)
        binding.newComment.setOnClickListener { newComment() }
    }


    private fun subToComments(){
        database.reference.child("comments").get().addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                listComment = listOf()
                for (snapshot in task.result!!.children) {
                    var id = snapshot.child("id").value.toString()
                    var content = snapshot.child("content").value.toString()
                    var name = snapshot.child("user").child("name").value.toString()
                    var userId = snapshot.child("user").child("id").value.toString()
                    var user = User(userId, name)
                    var comment = Comment(id, content, user)
                    listComment += comment
                }
                commentsAdapter.updateList(listComment)

            } else {
                Log.d("VALUE", task.exception?.message.toString())
            }
        }
    }


    override fun onResume() {
        super.onResume()
        subToComments()
    }


}