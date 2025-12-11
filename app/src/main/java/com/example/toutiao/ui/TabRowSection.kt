package com.example.toutiao.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.material3.TabRow
import androidx.compose.material3.Tab
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight

@Composable
fun ChannelTabRow(
    channels: List<String>,
    selectedIndex: Int,
    onChannelSelected: (Int) -> Unit
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .horizontalScroll(scrollState)
            .padding(horizontal = 4.dp)
    ) {
        channels.forEachIndexed { index, text ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 10.dp)
                    .clickable { onChannelSelected(index) }
            ) {
                Text(
                    text = text,
                    fontWeight = if (selectedIndex == index) FontWeight.Bold else FontWeight.Normal,
                    color = if (selectedIndex == index) Color.Black else Color.DarkGray
                )

                if (selectedIndex == index) {
                    Box(
                        Modifier
                            .size(width = 20.dp, height = 3.dp)
                            .background(Color.Red)
                    )
                }
            }
        }
    }
}
