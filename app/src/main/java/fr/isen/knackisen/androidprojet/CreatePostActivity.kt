package fr.isen.knackisen.androidprojet

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fr.isen.knackisen.androidprojet.data.model.Comment
import fr.isen.knackisen.androidprojet.data.model.Post
import fr.isen.knackisen.androidprojet.data.model.Reactions
import fr.isen.knackisen.androidprojet.data.model.User
import fr.isen.knackisen.androidprojet.databinding.ActivityCreatePostBinding

class CreatePostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreatePostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currentUser = User(1, "Example User")
        val comments : List<Comment> = listOf()
        val reactions = Reactions(0, comments)

        binding = ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonPost.setOnClickListener {
            val post = prepareData(currentUser, reactions)
            writePost(post)
        }

        binding.logoutCreatpost.setOnClickListener{
            Firebase.auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun prepareData(currentUser : User, reactions : Reactions) : Post{
        val content = binding.inputPost.text.toString()
        return Post("1", content, currentUser, reactions)
    }

    private fun writePost(post : Post){
        val database = Firebase.database.getReference("posts")
        database.setValue(post)
    }
}