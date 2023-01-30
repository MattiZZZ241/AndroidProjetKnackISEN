package fr.isen.knackisen.androidprojet

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fr.isen.knackisen.androidprojet.adapter.CommentsAdapter
import fr.isen.knackisen.androidprojet.data.model.Comment
import fr.isen.knackisen.androidprojet.data.model.User
import fr.isen.knackisen.androidprojet.databinding.ActivityCommentsBinding
import fr.isen.knackisen.androidprojet.databinding.ActivityLoginBinding

class CommentsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCommentsBinding
    private lateinit var database: DatabaseReference
    private lateinit var commentsAdapter: CommentsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.database.getReference("comments")

        database.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
               /* var listString: String = snapshot.getValue().toString()
                listString.split("Comment").map {
                    This is it
                    * {-NN1qEMr1Z_MuSrl8V_Q={id=-NN1qEMr1Z_MuSrl8V_Q, user={name=name, id=1}, content=ddik}, -NN1rqloI9VJf0fxtIwq={id=-NN1rqloI9VJf0fxtIwq, user={name=name, id=1}, content=fht}, -NN1opE3W5ylgyMdTI7X={user={name=name, id=1}, content=fiju}, -NN1o5vqFTxpDkwB5EJY={user={name=name, id=1}, content=gguui}, -NN1sWmMJGzI-iABwXxn={id=-NN1sWmMJGzI-iABwXxn, user={name=name, id=1}, content=dhhn}, user={name=name, id=1}, content=mhhhhtncfiu}

                    var id = it.substringAfter("id=").substringBefore(", user")
                    var content = it.substringAfter("content=").substringBefore("}")
                    var name = it.substringAfter("name=").substringBefore(", id")
                    var user = User(1, name)
                    var comment = Comment(id, content, user)
                    Log.i("TAG", "Value is: ${comment}")
                    *
                    // Fill a variable with the list of comments

                }
                var listList: List<Comment> = listOf()
                commentsAdapter.updateList(listList)
                Log.i("TAG", "Value is: ${snapshot.getValue()}")

                */
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })

        var newComment = fun () {
            val i = Intent(this@CommentsActivity, AddCommentActivity::class.java)
            startActivity(i)
        }
        var commentList = listOf(Comment("test","This is the message", User(1, "Serg")),Comment("id","Another message", User(2, "L'autre")))
        commentsAdapter = CommentsAdapter(commentList)
        binding.listComments.adapter = commentsAdapter
        binding.listComments.layoutManager = LinearLayoutManager(this)
        binding.newComment.setOnClickListener { newComment() }

    }





}