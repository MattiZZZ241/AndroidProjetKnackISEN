package fr.isen.knackisen.androidprojet.fragment


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import fr.isen.knackisen.androidprojet.LoginActivity
import fr.isen.knackisen.androidprojet.MyPostsActivity
import fr.isen.knackisen.androidprojet.PrivateUserInfoActivity
import fr.isen.knackisen.androidprojet.data.model.User
import fr.isen.knackisen.androidprojet.data.model.UserInfo
import fr.isen.knackisen.androidprojet.databinding.FragmentRightBinding
import java.net.URI

class RightFragment : Fragment() {
    private var _binding: FragmentRightBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: FirebaseDatabase
    private lateinit var user : FirebaseUser
    private lateinit var storage : FirebaseStorage
    private lateinit var userConnected : User

    private var UID: String =""
    private var profilePicture: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRightBinding.inflate(inflater, container, false)

        database= Firebase.database
        user = Firebase.auth.currentUser!!
        storage = Firebase.storage

        userConnected = User(user.uid, user.displayName.toString())

        Log.d("UID CHANGED", user.uid)
        UID = user.uid
        getProfilePicture()
        loadData()

        binding.button.setOnClickListener {
            updateData(UserInfo(binding.userInfoName.text.toString(), binding.userInfoAge.text.toString().toInt()))
        }

        binding.privateInfos.setOnClickListener {
            val intent = Intent(activity, PrivateUserInfoActivity::class.java)
            startActivity(intent)
        }

        binding.profilePictureButton.setOnClickListener {
            val intent = Intent()
                .setType("image/*")
                .setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent,"Choose a file"), 100)

        }

        binding.logoutCreatpost.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.MyPostButton.setOnClickListener {
            val intent = Intent(activity, MyPostsActivity::class.java)
            intent.putExtra("UID", userConnected.id)
            startActivity(intent)
        }

        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == -1) {
            profilePicture = data?.data
            Picasso.get().load(profilePicture).fit().centerCrop().into(binding.profilePictureButton)
        }
    }

    private fun loadData(){
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
            } else {
                Log.d("VALUE", task.exception?.message.toString())
            }
        }
    }


    private fun updateData(userInfo: UserInfo){
        val pushDB = database.getReference("userinfos").child(UID)
        Toast.makeText(activity, "Profile updated", Toast.LENGTH_SHORT).show()
        userInfo.uid=UID
        val profileName = userProfileChangeRequest {
            displayName = userInfo.name
        }
        user.updateProfile(profileName)




        pushDB.setValue(userInfo)
        if (profilePicture != null) {
            uploadProfilePicture(profilePicture!!)
        }

    }

    private fun uploadProfilePicture(selectedFile: Uri){


        val storageRef = storage.reference
        val imageRef = storageRef.child("profilePictures/${user.uid}")
        val uploadTask = imageRef.putFile(selectedFile)
        uploadTask.addOnFailureListener {
            Log.e("UPLOAD", "FAILED")
        }.addOnSuccessListener {
            Log.d("UPLOAD", "SUCCESS")
        }
    }

    private fun getProfilePicture()
    {
        binding.profilePictureButton.visibility = View.GONE
        val storageRef = storage.reference
        val imageRef = storageRef.child("profilePictures/${user.uid}")
        imageRef.downloadUrl.addOnSuccessListener {
            Log.d("DOWNLOAD", "SUCCESS")
            Picasso.get().load(it).fit().centerCrop().into(binding.profilePictureButton)
            binding.profilePictureButton.visibility = View.VISIBLE
            binding.progressBar2.visibility = View.GONE
        }.addOnFailureListener {
            Log.e("DOWNLOAD", "FAILED")
            val defaultImage= storageRef.child("profilePictures/defaultPicture.png")
            defaultImage.downloadUrl.addOnSuccessListener {
                Picasso.get().load(it).into(binding.profilePictureButton)
                binding.profilePictureButton.visibility = View.VISIBLE
                binding.progressBar2.visibility = View.GONE
            }.addOnFailureListener() {
                Log.e("DOWNLOAD DEFAULT", "FAILED")
            }
        }
    }
}