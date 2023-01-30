package fr.isen.knackisen.androidprojet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fr.isen.knackisen.androidprojet.databinding.ActivityAddCommentBinding

class AddCommentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddCommentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}