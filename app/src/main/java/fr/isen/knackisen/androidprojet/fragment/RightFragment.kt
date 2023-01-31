package fr.isen.knackisen.androidprojet.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fr.isen.knackisen.androidprojet.PrivateUserInfoActivity
import fr.isen.knackisen.androidprojet.data.model.UserInfo
import fr.isen.knackisen.androidprojet.databinding.FragmentRightBinding

class RightFragment : Fragment() {
    private var _binding: FragmentRightBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: FirebaseDatabase
    private lateinit var user : FirebaseUser
    private var UID: String =""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRightBinding.inflate(inflater, container, false)

        database= Firebase.database
        user = Firebase.auth.currentUser!!

        makeInvisible()

        Log.d("UID CHANGED", user.uid)
        UID = user.uid

        database.getReference("userinfos").child(UID).get().addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                val userInfo = task.result?.getValue(UserInfo::class.java)
                binding.userInfoAge.setText(userInfo?.age.toString())
                binding.userInfoName.setText(userInfo?.name.toString())
                if (binding.userInfoAge.text.toString() == "null") {
                    binding.userInfoAge.setText("")
                }
                if (binding.userInfoName.text.toString() == "null") {
                    binding.userInfoName.setText("")
                }
                makeVisible()
            } else {
                Log.d("VALUE", task.exception?.message.toString())
            }
        }

        binding.button.setOnClickListener {
            updateData(UserInfo(binding.userInfoName.text.toString(), binding.userInfoAge.text.toString().toInt()))
        }

        binding.PrivateInfos.setOnClickListener {
            val intent = Intent(activity, PrivateUserInfoActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    private fun addData(userInfo: UserInfo){
        val pushDB = database.getReference("userinfos").child(UID)
        userInfo.uid=UID
        pushDB.setValue(userInfo)

    }

    private fun updateData(userInfo: UserInfo){
        val pushDB = database.getReference("userinfos").child(UID)
        Toast.makeText(activity, "Profile updated", Toast.LENGTH_SHORT).show()
        userInfo.uid=UID

        pushDB.setValue(userInfo)
    }

    private fun makeVisible(){
        binding.button.visibility = View.VISIBLE
        binding.userInfoAge.visibility = View.VISIBLE
        binding.userInfoName.visibility = View.VISIBLE
        binding.infoAge.visibility = View.VISIBLE
        binding.infoName.visibility = View.VISIBLE
        binding.PrivateInfos.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    private fun makeInvisible(){
        binding.button.visibility = View.GONE
        binding.userInfoAge.visibility = View.GONE
        binding.userInfoName.visibility = View.GONE
        binding.infoAge.visibility = View.GONE
        binding.infoName.visibility = View.GONE
        binding.PrivateInfos.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }
}