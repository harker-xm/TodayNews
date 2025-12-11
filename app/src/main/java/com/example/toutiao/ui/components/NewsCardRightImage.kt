package com.example.toutiao.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun NewsCardRightImage(
    title: String,
    source: String,
    commentCount: Int,
    imageUrl: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(title)
            Spacer(modifier = Modifier.height(8.dp))
            Text("$source · $commentCount 评论")
        }

        Spacer(modifier = Modifier.width(8.dp))

        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            contentScale = ContentScale.Crop
        )
    }
}
