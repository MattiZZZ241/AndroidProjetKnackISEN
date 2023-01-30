package fr.isen.knackisen.androidprojet

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fr.isen.knackisen.androidprojet.data.model.PostContainer
import fr.isen.knackisen.androidprojet.databinding.ActivityListPostBinding

class ListPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListPostBinding
    private lateinit var postContaiener: PostContainer
    private var database: DatabaseReference?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListPostBinding.inflate(layoutInflater)
        setContentView(binding.root)


       /* Firebase.database.getReference("posts").addValueEventListener(object : ValueEventListener {
             override fun onDataChange(snapshot: DataSnapshot) {
                postContaiener = snapshot.getValue(PostContainer::class.java)!!
                Log.d("ListPostActivity", "postContainer: $postContaiener")
            }

             override fun onCancelled(error: DatabaseError) {
                Log.d("ListPostActivity", "onCancelled: $error")
            }
        })*/

        binding.button.setOnClickListener{



        }

    }






    override fun onDestroy() {
        super.onDestroy()
        Log.d("onDestroy", "$this onDestroy")
    }





}