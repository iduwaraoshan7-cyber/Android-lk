package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Tag
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
import com.example.ui.theme.SocialIndigo
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostBottomSheet(
    onDismiss: () -> Unit,
    onSubmitPost: (text: String, mediaUrl: String?, privacyLevel: String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    var mediaUrl by remember { mutableStateOf("") }
    var privacyLevel by remember { mutableStateOf("Public") }

    val sampleImages = listOf(
        "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=800&q=80",
        "https://images.unsplash.com/photo-1517694712202-14dd9538aa97?auto=format&fit=crop&w=800&q=80",
        "https://images.unsplash.com/photo-1503899036084-c55cdd92da26?auto=format&fit=crop&w=800&q=80",
        "https://images.unsplash.com/photo-1544787219-7f47ccb76574?auto=format&fit=crop&w=800&q=80"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .testTag("create_post_bottom_sheet")
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Create Post",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // User Info & Privacy selector
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=400&q=80",
                    contentDescription = "User Avatar",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Alex Morgan",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                            .clickable {
                                privacyLevel = if (privacyLevel == "Public") "Friends" else "Public"
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (privacyLevel == "Public") Icons.Default.Public else Icons.Default.Lock,
                            contentDescription = "Privacy",
                            modifier = Modifier.size(12.dp),
                            tint = SocialIndigo
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = privacyLevel,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = SocialIndigo
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Post Text Area
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text("What's on your mind? Share thoughts or #hashtags...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .testTag("post_input_text"),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Sample Media Attachments
            Text(
                text = "Add Photo or Video Attachment",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(sampleImages) { url ->
                    val isSelected = mediaUrl == url
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(
                                width = if (isSelected) 3.dp else 0.dp,
                                color = if (isSelected) SocialIndigo else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                mediaUrl = if (isSelected) "" else url
                            }
                    ) {
                        AsyncImage(
                            model = url,
                            contentDescription = "Attachment Option",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Post Submit Button
            Button(
                onClick = {
                    if (text.isNotBlank() || mediaUrl.isNotBlank()) {
                        onSubmitPost(text, mediaUrl, privacyLevel)
                        onDismiss()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("submit_post_button"),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SocialIndigo)
            ) {
                Text(text = "Post Now", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
