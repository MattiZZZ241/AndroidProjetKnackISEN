package fr.isen.knackisen.androidprojet

import ListPostAdapter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
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

                    val name = snapshot.child("user").child("name").value.toString()
                    val userId = snapshot.child("user").child("id").value.toString().toInt()
                    val user = User(userId, name)

                    val comment = Comment(id,content, user)

                    val like = snapshot.child("reactions").child("like").value.toString().toInt()

                    val reactions = Reactions(like, listOf())

                    val post = Post(id, content, user, reactions)


                    postContaiener += post
                }
                Log.i("TAG", "Value is: ${postContaiener}")
                val recyclerView = binding.recyclerview
                recyclerView.layoutManager = LinearLayoutManager(this)
                val adapter = recyclerView.adapter as ListPostAdapter
                adapter.refreshList(postContaiener)

            } else {
                Log.d("VALUE", task.exception?.message.toString())
            }
        }
    }

        override fun onDestroy() {
            super.onDestroy()
            Log.d("onDestroy", "$this onDestroy")
        }

}