package fr.isen.knackisen.androidprojet.data.model

import androidx.lifecycle.MutableLiveData

data class PostContainer(
    val postsList: MutableLiveData<Post>,
)


