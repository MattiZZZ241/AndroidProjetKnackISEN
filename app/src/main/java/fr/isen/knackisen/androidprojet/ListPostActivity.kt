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
    private lateinit var postContaiener: MutableList<Post>



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
                for (snapshot in task.result.children) {

                    val id = snapshot.child("id").value.toString()
                    val content = snapshot.child("content").value.toString()

                    val user = User(
                        snapshot.child("user").child("id").value as Int,
                        snapshot.child("user").child("name").value.toString(),
                    )


                  /*  val name = snapshot.child("user").child("name").value.toString()
                    //val image = snapshot.child("user").child("avatar").value.toString()
                    val userID = snapshot.child("user").child("id").value

                    Log.d("name", name)
                    Log.d("id", userID.toString())

                    val user = User(userID as Int, name)


                    val commentContent = snapshot.child("comment").child("content").value.toString()

                    val comment = Comment(commentContent, user)
                    val like = snapshot.child("reactions").child("like").value.toString().toInt()
                    val reactions = Reactions(like, listOf(comment))

                    postContaiener.add(Post(id.toInt(), content, user , reactions))*/

                }
                //recyclerViewRefresh()
            }
        }
    }


    fun recyclerViewRefresh() {
        val recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.adapter = ListPostAdapter(arrayListOf()) { post ->
                /*val intent = Intent(this, ::class.java)
                intent.putExtra("post", post)
                startActivity(intent)*/
            }


        val adapter = recyclerView.adapter as ListPostAdapter
        adapter.refreshList(postContaiener)
    }


        override fun onDestroy() {
            super.onDestroy()
            Log.d("onDestroy", "$this onDestroy")
        }

}