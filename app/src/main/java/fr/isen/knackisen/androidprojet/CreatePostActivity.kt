package fr.isen.knackisen.androidprojet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
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
    }

    private fun prepareData(currentUser : User, reactions : Reactions) : Post{
        val content = binding.inputPost.text.toString()
        return Post(1, content, currentUser, reactions)
    }

    private fun writePost(post : Post){
        //TODO : Write post to database
    }
}