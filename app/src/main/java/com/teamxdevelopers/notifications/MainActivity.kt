package com.teamxdevelopers.notifications

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.teamxdevelopers.notifications.ui.theme.TeamXPushNotificationsTheme
import data.Post

class MainActivity : ComponentActivity() {


    private val vm by viewModels<MainViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TeamXPushNotificationsTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Push Notifications")}
                        )
                    }
                ) {

                   if (vm.posts.isEmpty()){
                       Box(modifier = Modifier.fillMaxSize(),contentAlignment = Alignment.Center) {
                           CircularProgressIndicator()
                       }
                   }else{
                       LazyColumn{
                           items(vm.posts){item->
                               BLogPost(post = item) {
                                   vm.postToPush.value=item
                               }
                               Divider()
                           }
                       }
                   }

                }


                vm.postToPush.value?.let { post->
                    AlertDialog(
                        onDismissRequest = { vm.postAsNull() },
                        confirmButton = {
                            TextButton(onClick = {
                                vm.push()
                            }) {
                                Text(text = "Yes")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { vm.postAsNull() }) {
                                Text(text = "No")
                            }
                        },
                        title ={ Text(text = post.title)},
                        text = { Text(text = "Do you really want to send this post as a notifications?")}
                    )
                }

            }
        }
    }

    @Composable
    fun BLogPost(
        post: Post,
        onClick:()->Unit
    ){
        val imagePainter= rememberImagePainter(data = post.getThumbnail())

        Column(
            modifier= Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Image(
                painter = imagePainter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .height(190.dp)
                    .fillMaxWidth(),
            )

            Text(
                text=post.title,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text=post.published,
                color=MaterialTheme.colors.onBackground.copy(alpha = 0.8f),
                fontSize = 12.sp
            )

        }
    }


}
