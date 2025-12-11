package com.example.toutiao.ui.topBar

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalDensity

@Composable
fun FadingText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF666666),
    fontSize: Float = 14f,
    fadeWidth: Dp = 48.dp,
) {
    val fadePx = with(LocalDensity.current) { fadeWidth.toPx() }

    Box(
        modifier = modifier.drawWithContent {
            drawContent() // 先画文字

            val width = size.width
            if (width > fadePx) {
                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(1f, 1f, 1f, 0f), // 完全不透明 → 保留文字
                            Color(1f, 1f, 1f, 1f)  // 完全透明 → 擦除文字
                        ),
                        startX = width - fadePx,
                        endX = width
                    ),
                )
            }
        }
    ) {
        Text(
            text = text,
            color = color,
            fontSize = fontSize.sp,
            maxLines = 1,
            overflow = TextOverflow.Visible,
            softWrap = false
        )
    }
}