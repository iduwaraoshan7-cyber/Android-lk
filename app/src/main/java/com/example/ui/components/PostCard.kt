package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.PostEntity
import com.example.data.models.ReactionType
import com.example.ui.theme.SocialCoral
import com.example.ui.theme.SocialIndigo
import coil.compose.AsyncImage

@Composable
fun PostCard(
    post: PostEntity,
    onReactionSelect: (ReactionType) -> Unit,
    onCommentClick: () -> Unit,
    onShareClick: () -> Unit,
    onBookmarkToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showReactionPicker by remember { mutableStateOf(false) }
    var showDoubleTapHeart by remember { mutableStateOf(false) }

    val currentReactionEnum = try {
        ReactionType.valueOf(post.userReaction)
    } catch (e: Exception) {
        ReactionType.NONE
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("post_card_${post.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(vertical = 12.dp)) {
            // Header Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = post.authorAvatarUrl,
                    contentDescription = post.authorName,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = post.authorName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (post.isVerified) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Verified",
                                tint = SocialIndigo,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${post.authorHandle} • ${post.timeAgo}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Outlined.Public,
                            contentDescription = post.privacyLevel,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
                IconButton(onClick = { /* Options */ }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Options",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Post Text Content with Hashtags
            val formattedText = buildAnnotatedString {
                val words = post.contentText.split(" ")
                words.forEachIndexed { index, word ->
                    if (word.startsWith("#")) {
                        withStyle(style = SpanStyle(color = SocialIndigo, fontWeight = FontWeight.SemiBold)) {
                            append(word)
                        }
                    } else {
                        append(word)
                    }
                    if (index < words.size - 1) append(" ")
                }
            }
            Text(
                text = formattedText,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 14.dp)
            )

            // Post Media with double-tap heart gesture
            if (!post.mediaUrl.isNull_or_blank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onDoubleTap = {
                                    showDoubleTapHeart = true
                                    onReactionSelect(ReactionType.LOVE)
                                }
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = post.mediaUrl,
                        contentDescription = "Post Media",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Double Tap Heart Overlay Animation
                    AnimatedVisibility(
                        visible = showDoubleTapHeart,
                        enter = scaleIn(animationSpec = spring()) + fadeIn(),
                        exit = scaleOut() + fadeOut()
                    ) {
                        LaunchedEffect(Unit) {
                            kotlinx.coroutines.delay(800)
                            showDoubleTapHeart = false
                        }
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Heart",
                            tint = SocialCoral,
                            modifier = Modifier.size(80.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Reaction Count & Comments summary bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Emoji summary badges
                    Row(horizontalArrangement = Arrangement.spacedBy((-4).dp)) {
                        listOf("👍", "❤️", "🔥").forEach { emoji ->
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = emoji, fontSize = 10.sp)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${post.likesCount} reactions",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = "${post.commentsCount} comments • ${post.sharesCount} shares",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.clickable { onCommentClick() }
                )
            }

            Divider(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 14.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            )

            // Multi-Reaction Selector Popup
            AnimatedVisibility(visible = showReactionPicker) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 4.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ReactionType.entries.filter { it != ReactionType.NONE }.forEach { reaction ->
                        Text(
                            text = reaction.emoji,
                            fontSize = 24.sp,
                            modifier = Modifier
                                .clickable {
                                    onReactionSelect(reaction)
                                    showReactionPicker = false
                                }
                                .padding(4.dp)
                        )
                    }
                }
            }

            // Bottom Action Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Like / Reaction Button
                Row(
                    modifier = Modifier
                        .clickable {
                            if (currentReactionEnum == ReactionType.NONE) {
                                onReactionSelect(ReactionType.LIKE)
                            } else {
                                onReactionSelect(ReactionType.NONE)
                            }
                        }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (currentReactionEnum == ReactionType.NONE) {
                        Icon(
                            imageVector = Icons.Outlined.ThumbUp,
                            contentDescription = "Like",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Like",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Text(
                            text = currentReactionEnum.emoji,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = currentReactionEnum.label,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = SocialIndigo
                        )
                    }
                }

                // Long Press Trigger for Reaction Picker
                Text(
                    text = "•••",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.clickable { showReactionPicker = !showReactionPicker }
                )

                // Comment Button
                Row(
                    modifier = Modifier
                        .clickable { onCommentClick() }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ChatBubbleOutline,
                        contentDescription = "Comment",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Comment",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Share Button
                Row(
                    modifier = Modifier
                        .clickable { onShareClick() }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Share,
                        contentDescription = "Share",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Bookmark Toggle Button
                IconButton(
                    onClick = { onBookmarkToggle() },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (post.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (post.isBookmarked) SocialIndigo else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

private fun String?.isNull_or_blank(): Boolean = this.isNullOrBlank()
