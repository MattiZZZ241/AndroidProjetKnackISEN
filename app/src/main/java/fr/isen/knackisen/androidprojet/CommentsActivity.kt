package fr.isen.knackisen.androidprojet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import fr.isen.knackisen.androidprojet.adapter.CommentsAdapter
import fr.isen.knackisen.androidprojet.data.model.Comment
import fr.isen.knackisen.androidprojet.data.model.User
import fr.isen.knackisen.androidprojet.databinding.ActivityCommentsBinding
import fr.isen.knackisen.androidprojet.databinding.ActivityLoginBinding

class CommentsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCommentsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var newComment = fun () {
            val i = Intent(this@CommentsActivity, AddCommentActivity::class.java)
            startActivity(i)
        }
        var commentList = listOf(Comment("This is the message", User(1, "Serg")),Comment("Another message", User(2, "L'autre")))
        binding.listComments.adapter = CommentsAdapter(commentList)
        binding.listComments.layoutManager = LinearLayoutManager(this)
        binding.newComment.setOnClickListener { newComment() }

    }





}