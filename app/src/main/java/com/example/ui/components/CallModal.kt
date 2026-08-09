package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.SocialCoral
import coil.compose.AsyncImage

@Composable
fun CallModal(
    contactName: String,
    contactAvatarUrl: String,
    isVideo: Boolean,
    onEndCall: () -> Unit
) {
    var isMuted by remember { mutableStateOf(false) }
    var isVideoOff by remember { mutableStateOf(!isVideo) }
    var secondsElapsed by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(1000)
            secondsElapsed++
        }
    }

    val minutes = secondsElapsed / 60
    val secs = secondsElapsed % 60
    val durationText = String.format("%02d:%02d", minutes, secs)

    Dialog(
        onDismissRequest = onEndCall,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0F172A))
                .testTag("call_modal_screen")
        ) {
            if (isVideo && !isVideoOff) {
                // Video Call Background simulation
                AsyncImage(
                    model = contactAvatarUrl,
                    contentDescription = "Video Stream",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f))
                )
            }

            // Top Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!isVideo || isVideoOff) {
                    AsyncImage(
                        model = contactAvatarUrl,
                        contentDescription = contactName,
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Text(
                    text = contactName,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (isVideo) "SocialConnect Video Call • $durationText" else "SocialConnect Voice Call • $durationText",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }

            // Bottom Call Controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 50.dp, start = 30.dp, end = 30.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mute Button
                IconButton(
                    onClick = { isMuted = !isMuted },
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(if (isMuted) Color.White else Color.White.copy(alpha = 0.2f))
                ) {
                    Icon(
                        imageVector = if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                        contentDescription = "Mute",
                        tint = if (isMuted) Color.Black else Color.White
                    )
                }

                // Video Toggle Button
                IconButton(
                    onClick = { isVideoOff = !isVideoOff },
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(if (isVideoOff) Color.White else Color.White.copy(alpha = 0.2f))
                ) {
                    Icon(
                        imageVector = if (isVideoOff) Icons.Default.VideocamOff else Icons.Default.Videocam,
                        contentDescription = "Video",
                        tint = if (isVideoOff) Color.Black else Color.White
                    )
                }

                // End Call Button
                IconButton(
                    onClick = onEndCall,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(SocialCoral)
                        .testTag("end_call_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.CallEnd,
                        contentDescription = "End Call",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}
