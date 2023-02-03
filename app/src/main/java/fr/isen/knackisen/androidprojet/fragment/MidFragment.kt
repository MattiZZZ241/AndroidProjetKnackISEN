package fr.isen.knackisen.androidprojet.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
        var getUser = Firebase.auth.currentUser
        if (getUser != null) {
            if (getUser.uid == null) {
                Log.e("Get user", "No user connected")
                return binding.root
            }
        }
        val user = User(getUser!!.uid, getUser.displayName.toString())

        val currentUser = user
        val comments: List<Post> = listOf(Post("1", "Example Comment", currentUser,Reactions(0,false, listOf())))
        val reactions = Reactions(0, false, comments)

        val key = database.push().key
        if(key.isNullOrEmpty() ) {
            Log.e("ERROR", "Key is null or empty")
            return binding.root
        }

        binding.buttonPost.setOnClickListener {
            val post = prepareData(currentUser, key, reactions)
            database.child(key).setValue(post)
        }
        return binding.root
    }

    private fun prepareData(currentUser: User, key:String, reactions: Reactions): Post {
        val content = binding.inputPost.text.toString()
        return Post(key, content, currentUser, reactions)
    }
}