package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.PostEntity
import com.example.data.models.ReactionType
import com.example.data.models.StoryEntity
import com.example.ui.components.PostCard
import com.example.ui.components.StoryCircleTray
import com.example.ui.theme.SocialCoral
import com.example.ui.theme.SocialIndigo
import com.example.ui.theme.SocialPurple
import coil.compose.AsyncImage

@Composable
fun FeedScreen(
    posts: List<PostEntity>,
    stories: List<StoryEntity>,
    onStoryClick: (StoryEntity) -> Unit,
    onCreatePostClick: () -> Unit,
    onReactionSelect: (postId: Long, reaction: ReactionType, likesCount: Int, currentReaction: String) -> Unit,
    onCommentClick: (PostEntity) -> Unit,
    onBookmarkToggle: (postId: Long, isBookmarked: Boolean) -> Unit,
    onNavigateToChat: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilterTab by remember { mutableStateOf("For You") }
    val filterTabs = listOf("For You", "Following", "Trending")

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .statusBarsPadding()
            ) {
                // Top Action Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Gradient Logo Text
                    Text(
                        text = "SocialConnect",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = SocialIndigo
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onCreatePostClick,
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .testTag("top_bar_add_post_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "New Post",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        IconButton(
                            onClick = onNavigateToChat,
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .testTag("top_bar_chat_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChatBubbleOutline,
                                contentDescription = "Messages",
                                tint = SocialIndigo
                            )
                        }
                    }
                }

                // Filter Tabs Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    filterTabs.forEach { tab ->
                        val isSelected = selectedFilterTab == tab
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedFilterTab = tab },
                            label = {
                                Text(
                                    text = tab,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SocialIndigo,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Stories Tray (Instagram & Facebook style)
            item {
                StoryCircleTray(
                    stories = stories,
                    onStoryClick = onStoryClick,
                    onAddStoryClick = onCreatePostClick
                )
                Divider(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            }

            // Composer Quick Prompt Box
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                        .clickable { onCreatePostClick() }
                        .testTag("composer_box"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=400&q=80",
                            contentDescription = "User Avatar",
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "What's on your mind, Alex?",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = "Photo",
                            tint = SocialPurple,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            // Feed Posts List
            items(posts, key = { it.id }) { post ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    PostCard(
                        post = post,
                        onReactionSelect = { reaction ->
                            onReactionSelect(post.id, reaction, post.likesCount, post.userReaction)
                        },
                        onCommentClick = { onCommentClick(post) },
                        onShareClick = { /* Share dialog */ },
                        onBookmarkToggle = { onBookmarkToggle(post.id, post.isBookmarked) }
                    )
                }
            }
        }
    }
}
