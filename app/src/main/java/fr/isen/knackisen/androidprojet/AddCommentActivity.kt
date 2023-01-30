package fr.isen.knackisen.androidprojet

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fr.isen.knackisen.androidprojet.data.model.Comment
import fr.isen.knackisen.androidprojet.data.model.User
import fr.isen.knackisen.androidprojet.databinding.ActivityAddCommentBinding

class AddCommentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddCommentBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var listComment: List<Comment>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var database = Firebase.database
        listComment = listOf()

        database.reference.child("comments").get().addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                listComment = listOf()
                for (snapshot in task.result!!.children) {
                    var id = snapshot.child("id").value.toString()
                    var content = snapshot.child("content").value.toString()
                    var name = snapshot.child("user").child("name").value.toString()
                    var user = User(1, name)
                    var comment = Comment(id, content, user)
                    listComment += comment
                }
                Log.i("TAG", "Value is: ${listComment}")

            } else {
                Log.d("VALUE", task.exception?.message.toString())
            }
        }

        binding.postButton.setOnClickListener( ) {
            // post to the Real time database (firebase)
            val user = User(1, "name")
            val commentBody = binding.commentText.text.toString()
            binding.commentText.text = null

            val comment = Comment( listComment.size.toString(), commentBody, user)
            listComment = listComment.plus(comment)
            database.getReference("comments").setValue(listComment)

            finish()
        }
    }
}