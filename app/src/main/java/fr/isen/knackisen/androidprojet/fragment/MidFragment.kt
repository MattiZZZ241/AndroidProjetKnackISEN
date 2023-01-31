package fr.isen.knackisen.androidprojet.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fr.isen.knackisen.androidprojet.LoginActivity
import fr.isen.knackisen.androidprojet.data.model.Comment
import fr.isen.knackisen.androidprojet.data.model.Post
import fr.isen.knackisen.androidprojet.data.model.Reactions
import fr.isen.knackisen.androidprojet.data.model.User
import fr.isen.knackisen.androidprojet.databinding.FragmentMidBinding

class MidFragment : Fragment() {
    private var _binding: FragmentMidBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMidBinding.inflate(inflater, container, false)

        val database = Firebase.database.getReference("posts")

        val currentUser = User("1", "Example User")
        val comments: List<Comment> = listOf(Comment("1", "Example Comment", currentUser))
        val reactions = Reactions(0, false, comments)

        val key = database.push().key
        
        binding.buttonPost.setOnClickListener {
            val post = prepareData(currentUser, reactions)
            database.child(key!!).setValue(post)
            //writePost(post)
        }

        binding.logoutCreatpost.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    private fun prepareData(currentUser: User, reactions: Reactions): Post {
        val content = binding.inputPost.text.toString()
        return Post("1", content, currentUser, reactions)
    }
}