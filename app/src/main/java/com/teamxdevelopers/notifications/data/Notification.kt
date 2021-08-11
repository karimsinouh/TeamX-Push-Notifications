package com.teamxdevelopers.notifications.data

import com.teamxdevelopers.notifications.data.NotificationData


class Notification (
    val title:String,
    val thumbnail:String,
    val postId:String,
    val seen:Boolean,
    val id:Int,
){
    fun asNotificationData(): NotificationData
    = NotificationData("/topics/general",this)
}