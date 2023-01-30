package fr.isen.knackisen.androidprojet


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import fr.isen.knackisen.androidprojet.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = Firebase.auth.currentUser
        if(currentUser != null){
            updateUI(Firebase.auth.currentUser)
        }else{
            binding.connexionSignin.setOnClickListener{
                val mail: String = binding.mailSignin.getText().toString().trim()
                val mdp: String = binding.pwSignin.getText().toString().trim()
                if(mail.isEmpty() || mdp.isEmpty()){
                    Toast.makeText(baseContext, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                }else{
                    signIn(mail,mdp)
                }
            }
            binding.inscriptionSignin.setOnClickListener{
                val intent = Intent(this, SignupActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun signIn(email: String, password: String) {
        // [START sign_in_with_email]
        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("connexion", "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("connexion", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
        // [END sign_in_with_email]
    }

    private fun updateUI(user: FirebaseUser?) {
        val intent = Intent(this, CreatePostActivity::class.java)
        startActivity(intent)
    }

}