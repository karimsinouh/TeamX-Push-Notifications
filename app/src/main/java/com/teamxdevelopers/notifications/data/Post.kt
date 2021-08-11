package data

import com.teamxdevelopers.notifications.data.Notification
import com.teamxdevelopers.notifications.data.PostImage

data class Post(
    val kind:String,
    val id:String,
    val published:String,
    val updated:String,
    val url:String,
    val title:String,
    val content:String,
    val images:List<PostImage>?=null,
){
    fun getThumbnail():String{
        return if (images!=null)
            images[0].url
        else
            "https://www.unfe.org/wp-content/uploads/2019/04/SM-placeholder-1024x512.png"
    }

    fun asNotification(): Notification
    = Notification(title,getThumbnail(),id,false,0)

}
