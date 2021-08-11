package com.teamxdevelopers.notifications.api

import android.util.Log
import com.teamxdevelopers.notifications.api.ApiConstants.API_KEY
import com.teamxdevelopers.notifications.api.ApiConstants.BASE_URL
import com.teamxdevelopers.notifications.api.ApiConstants.CONTENT_TYPE
import com.teamxdevelopers.notifications.api.ApiConstants.FCM_BASE_URL
import com.teamxdevelopers.notifications.api.ApiConstants.SERVER_KEY
import com.teamxdevelopers.notifications.data.Notification
import com.teamxdevelopers.notifications.data.ResponsePage
import data.Post
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class PostsEndPoint {

    private val client= HttpClient(Android){
        install(JsonFeature)
    }

    suspend fun getPosts(pageToken:String?="",listener:(ResponsePage<Post>)->Unit){
        val url="${BASE_URL}posts?key=$API_KEY&fetchImages=true${
            if(pageToken!="") "&pageToken=$pageToken" else ""
        }"
        val response=client.get<ResponsePage<Post>>(url)
        listener(response)
    }

    suspend fun search(q:String,listener: (List<Post>) -> Unit){
        val url="${BASE_URL}posts/search?q=$q&key=$API_KEY&fetchImages=true"
        val response=client.get<ResponsePage<Post>>(url)
        listener(response.items?: emptyList())
    }

    suspend fun getPost(postId:String,listener:(Post)->Unit){
        val url="${BASE_URL}posts/$postId?key=$API_KEY&fetchImages=true"
        val response=client.get<Post>(url)
        listener(response)
    }

    suspend fun pushNotification(notification: Notification, onDone:(HttpResponse)->Unit){
        val data=notification.asNotificationData()

        Log.d("wtf",notification.thumbnail)

        val url="${FCM_BASE_URL}fcm/send"

        val response=client.post<HttpResponse>(url){
            headers {
                header("Authorization","key=$SERVER_KEY")
                header("Content-Type", CONTENT_TYPE)

            }
            body=data
        }

        onDone(response)

    }


}