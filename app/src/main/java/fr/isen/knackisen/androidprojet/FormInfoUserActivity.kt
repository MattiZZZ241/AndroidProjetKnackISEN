package fr.isen.knackisen.androidprojet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fr.isen.knackisen.androidprojet.data.model.UserInfo
import fr.isen.knackisen.androidprojet.databinding.ActivityCommentsBinding
import fr.isen.knackisen.androidprojet.databinding.ActivityFormInfoUserBinding

class FormInfoUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormInfoUserBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var user : FirebaseUser
    private var UID: String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormInfoUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database= Firebase.database
        user = Firebase.auth.currentUser!!


        Log.d("UID CHANGED", user.uid)
        UID = user.uid


        binding.buttonFIUA.setOnClickListener {
            if (binding.userInfoNameFIUA.text.toString().isEmpty() || binding.userInfoAgeFIUA.text.toString().isEmpty() || binding.radioGroupFUIA.checkedRadioButtonId == -1) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            } else {
                val radioButton = binding.radioGroupFUIA.findViewById<RadioButton>(binding.radioGroupFUIA.checkedRadioButtonId)
                updateData(UserInfo(binding.userInfoNameFIUA.text.toString(), binding.userInfoAgeFIUA.text.toString().toInt(),"",radioButton.text.toString()))
            }
        }

    }


    private fun updateData(userInfo: UserInfo){
        val pushDB = database.getReference("userinfos").child(UID)
        Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
        userInfo.uid=UID
        pushDB.setValue(userInfo)

        val profileName = userProfileChangeRequest {
            displayName = userInfo.name
        }
        user.updateProfile(profileName)

        val intent = Intent(this@FormInfoUserActivity, MainActivity::class.java)
        startActivity(intent)
    }
}