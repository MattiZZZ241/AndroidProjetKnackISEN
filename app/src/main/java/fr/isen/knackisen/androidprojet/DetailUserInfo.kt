package fr.isen.knackisen.androidprojet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fr.isen.knackisen.androidprojet.data.model.UserInfo
import fr.isen.knackisen.androidprojet.databinding.ActivityDetailUserInfoBinding

class DetailUserInfo : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserInfoBinding
    private lateinit var database: FirebaseDatabase
    private var UID: String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user_info)

        binding = ActivityDetailUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database= Firebase.database
        val user = Firebase.auth.currentUser



        if (user != null) {
            Log.d("UID CHANGED", user.uid)
            UID = user.uid

            database.getReference("userinfos").child(UID).get().addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    val userInfo = task.result?.getValue(UserInfo::class.java)
                    binding.userInfoAge.text = userInfo?.age.toString()
                    binding.userInfoName.text = userInfo?.name
                    binding.button.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                } else {
                    Log.d("VALUE", task.exception?.message.toString())
                }
            }
        }
        else{
            binding.progressBar.visibility = View.GONE
            binding.userInfoName.text = "No user connected"
        }





        binding.button.setOnClickListener {
            addData(UserInfo("Hello","",12))
        }


    }

    private fun addData(userInfo: UserInfo){
        val pushDB = database.getReference("userinfos").child(UID)
        userInfo.uid=UID
        pushDB.setValue(userInfo)

    }
}