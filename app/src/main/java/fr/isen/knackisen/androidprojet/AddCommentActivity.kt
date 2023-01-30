package fr.isen.knackisen.androidprojet

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fr.isen.knackisen.androidprojet.data.model.Comment
import fr.isen.knackisen.androidprojet.data.model.User
import fr.isen.knackisen.androidprojet.databinding.ActivityAddCommentBinding

class AddCommentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddCommentBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = Firebase.database.getReference("comments")

        database.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                binding.post.text  = snapshot.getValue().toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

        binding.postButton.setOnClickListener( ) {
            // post to the Real time database (firebase)
            val user = User(1, "name")
            val commentBody = binding.commentText.text.toString()
            binding.commentText.text = null
            val commentId = database.push().key
            if (commentId == null ){
                Log.w(TAG, "Couldn't get push key for comments")
                return@setOnClickListener
            }
            val comment = Comment( commentId, commentBody, user)

            Log.i("commentId", commentId.toString())
            database.child(commentId.toString()).setValue(comment)

            finish()
        }
    }
}