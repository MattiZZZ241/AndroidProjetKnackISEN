package fr.isen.knackisen.androidprojet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import fr.isen.knackisen.androidprojet.databinding.ActivityPrivateUserInfoBinding
import kotlin.math.log

class PrivateUserInfoActivity : AppCompatActivity() {
    private lateinit var user : FirebaseUser
    private lateinit var binding: ActivityPrivateUserInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_private_user_info)
        binding = ActivityPrivateUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = Firebase.auth.currentUser!!

        binding.userInfoEmail.setText(user.email.toString())

        binding.privateSaveButton.setOnClickListener {
            updatePrivate()
        }

    }

    private fun updatePrivate()
    {
        if (binding.userInfoCurrentPassword.text.toString() !="")
        {
            Log.i("email", user.email.toString())
            val credential = EmailAuthProvider.getCredential(user.email.toString(), binding.userInfoCurrentPassword.text.toString())
            user.reauthenticate(credential).addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.i("REAUTHENTICATED SUCCESS", "REAUTHENTICATED")
                    user.updateEmail(binding.userInfoEmail.text.toString()).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Log.i("EMAIL CHANGED", binding.userInfoEmail.text.toString())
                        }
                        else{
                            Log.e("EMAIL NOT CHANGED", binding.userInfoEmail.text.toString())
                        }
                    }
                    if (binding.userInfoNewPassword.text.toString() != "")
                    {
                        user.updatePassword(binding.userInfoNewPassword.text.toString()).addOnCompleteListener {
                            if (it.isSuccessful) {
                                Log.i("PASSWORD CHANGED", binding.userInfoNewPassword.text.toString())
                            }
                            else{
                                Log.e("PASSWORD NOT CHANGED", binding.userInfoNewPassword.text.toString())
                            }
                        }

                    }
                    else{
                        Log.i("PASSWORD", "UNCHANGED")
                    }
                    Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
                    binding.userInfoCurrentPassword.setText("")
                    binding.userInfoNewPassword.setText("")
                }
                else{
                    Log.i("REAUTHENTICATED FAILED", it.exception.toString())
                    Toast.makeText(this, "Wrong password", Toast.LENGTH_SHORT).show()
                }
            }
        }
        else
        {
            Toast.makeText(this, "Please provide current password to save", Toast.LENGTH_SHORT).show()
        }


    }
}