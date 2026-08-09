package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.WhatsAppGreen

@Composable
fun VoiceNotePlayer(
    durationSeconds: Int,
    isFromUser: Boolean,
    modifier: Modifier = Modifier
) {
    var isPlaying by remember { mutableStateOf(false) }

    // Waveform bar heights simulation
    val waveformHeights = listOf(12, 18, 28, 16, 22, 32, 24, 14, 20, 26, 18, 10, 22, 16, 28)

    Row(
        modifier = modifier
            .width(210.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (isFromUser) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Play / Pause Circle Button
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(if (isFromUser) MaterialTheme.colorScheme.primary else WhatsAppGreen)
                .clickable { isPlaying = !isPlaying },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = "Play Voice Note",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Waveform Bars
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            waveformHeights.forEachIndexed { index, height ->
                val active = isPlaying && (index % 3 == 0)
                Box(
                    modifier = Modifier
                        .width(3.dp)
                        .height(if (active) (height + 6).dp else height.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(
                            if (isFromUser) MaterialTheme.colorScheme.primary.copy(alpha = if (index < 8) 1f else 0.4f)
                            else WhatsAppGreen.copy(alpha = if (index < 8) 1f else 0.4f)
                        )
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "0:${if (durationSeconds < 10) "0$durationSeconds" else "$durationSeconds"}",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
