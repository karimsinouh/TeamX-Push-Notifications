package com.teamxdevelopers.notifications

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamxdevelopers.notifications.api.PostsEndPoint
import com.teamxdevelopers.notifications.data.Notification
import data.Post
import kotlinx.coroutines.launch

class MainViewModel:ViewModel() {

    private val repo=PostsEndPoint()
    val posts= mutableStateListOf<Post>()
    val postToPush= mutableStateOf<Post?>(null)
    var pageToken=""

    init {
        loadPosts()
    }


    fun postAsNull(){
        postToPush.value=null
    }

    fun loadPosts(){
        viewModelScope.launch {
            repo.getPosts {
                pageToken=it.nextPageToken?:""
                posts.addAll(it.items?: emptyList())
            }
        }
    }

    fun push() {
        val post=postToPush.value
        val notification=Notification(
            post?.title?:"",
            post?.getThumbnail()?:"",
            post?.id!!,
            false,
            0
        )
        viewModelScope.launch {
            repo.pushNotification(notification = notification){
                Log.d("wtf",it.status.value.toString())
                Log.d("wtf",it.status.description)
            }
        }
        postAsNull()
    }

}