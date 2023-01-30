package fr.isen.knackisen.androidprojet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import fr.isen.knackisen.androidprojet.databinding.ActivityLoginBinding
import fr.isen.knackisen.androidprojet.databinding.ActivitySignupBinding


class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.

            binding.inscriptionSignup.setOnClickListener {
                val mail: String = binding.emailSignup.getText().toString().trim()
                val mdp: String = binding.pwSignup.getText().toString().trim()
                if(mail.isEmpty() || mdp.isEmpty()){
                    Toast.makeText(baseContext, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                }else{
                    createAccount(mail, mdp)
                }
            }

            binding.connexionSignup.setOnClickListener{
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }

    }

    private fun createAccount(email: String, password: String) {
        // [START create_user_with_email]
        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("inscription", "createUserWithEmail:success")
                    val user = Firebase.auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("inscription", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "format d'email ou mdp incorect.",
                        Toast.LENGTH_SHORT).show()
                }
            }
        // [END create_user_with_email]
    }


    private fun updateUI(user: FirebaseUser?) {
        if(user != null){
            val intent = Intent(this, PoubelActivity::class.java)
            startActivity(intent)
        }
    }
}