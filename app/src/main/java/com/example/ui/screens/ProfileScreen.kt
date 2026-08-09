package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
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
import com.example.data.models.PostEntity
import com.example.data.models.UserProfileEntity
import com.example.ui.components.PostCard
import com.example.ui.theme.SocialIndigo
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userProfile: UserProfileEntity?,
    userPosts: List<PostEntity>,
    onUpdateProfile: (name: String, handle: String, bio: String, location: String, website: String, avatarUrl: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isEditDialogShowing by remember { mutableStateOf(false) }
    var selectedViewMode by remember { mutableStateOf(0) } // 0 = Posts Feed, 1 = Photo Grid, 2 = Bookmarks

    val profile = userProfile ?: UserProfileEntity(
        id = 1,
        name = "Alex Morgan",
        handle = "@alexmorgan",
        bio = "Digital Creator & Tech Explorer 🚀",
        avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=400&q=80",
        coverUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&w=1200&q=80",
        location = "San Francisco, CA",
        website = "linktr.ee/alexmorgan",
        followersCount = 2840,
        followingCount = 412,
        postsCount = userPosts.size
    )

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Cover Image Banner & Avatar Overlay
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    AsyncImage(
                        model = profile.coverUrl,
                        contentDescription = "Cover Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        contentScale = ContentScale.Crop
                    )

                    // Avatar Overlay
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = 20.dp)
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(4.dp)
                            .clip(CircleShape)
                    ) {
                        AsyncImage(
                            model = profile.avatarUrl,
                            contentDescription = profile.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            // User Info Section
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = profile.name,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Verified",
                                    tint = SocialIndigo,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Text(
                                text = profile.handle,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Edit Profile Button
                        OutlinedButton(
                            onClick = { isEditDialogShowing = true },
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.testTag("edit_profile_button")
                        ) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Edit Profile", fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = profile.bio,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Location",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = profile.location,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = "Website",
                                tint = SocialIndigo,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = profile.website,
                                fontSize = 12.sp,
                                color = SocialIndigo,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Stats Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ProfileStatItem(count = "${userPosts.size}", label = "Posts")
                        ProfileStatItem(count = "${profile.followersCount}", label = "Followers")
                        ProfileStatItem(count = "${profile.followingCount}", label = "Following")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // View Mode Tabs (Timeline vs Grid vs Bookmarks)
                    TabRow(
                        selectedTabIndex = selectedViewMode,
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = SocialIndigo
                    ) {
                        Tab(
                            selected = selectedViewMode == 0,
                            onClick = { selectedViewMode = 0 },
                            icon = { Icon(imageVector = Icons.Default.List, contentDescription = "Timeline") }
                        )
                        Tab(
                            selected = selectedViewMode == 1,
                            onClick = { selectedViewMode = 1 },
                            icon = { Icon(imageVector = Icons.Default.GridOn, contentDescription = "Grid") }
                        )
                        Tab(
                            selected = selectedViewMode == 2,
                            onClick = { selectedViewMode = 2 },
                            icon = { Icon(imageVector = Icons.Default.Bookmark, contentDescription = "Bookmarks") }
                        )
                    }
                }
            }

            // User Posts Content View
            when (selectedViewMode) {
                0 -> {
                    items(userPosts) { post ->
                        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                            PostCard(
                                post = post,
                                onReactionSelect = { },
                                onCommentClick = { },
                                onShareClick = { },
                                onBookmarkToggle = { }
                            )
                        }
                    }
                }
                1 -> {
                    item {
                        val mediaPosts = userPosts.filter { !it.mediaUrl.isNull_or_blank() }
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(mediaPosts) { post ->
                                Box(
                                    modifier = Modifier
                                        .height(100.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                ) {
                                    AsyncImage(
                                        model = post.mediaUrl,
                                        contentDescription = "Post Grid Photo",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        }
                    }
                }
                2 -> {
                    val bookmarkedPosts = userPosts.filter { it.isBookmarked }
                    if (bookmarkedPosts.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No saved posts yet.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    } else {
                        items(bookmarkedPosts) { post ->
                            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                                PostCard(
                                    post = post,
                                    onReactionSelect = { },
                                    onCommentClick = { },
                                    onShareClick = { },
                                    onBookmarkToggle = { }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Edit Profile Dialog
        if (isEditDialogShowing) {
            EditProfileDialog(
                currentProfile = profile,
                onDismiss = { isEditDialogShowing = false },
                onSave = { name, handle, bio, location, website, avatarUrl ->
                    onUpdateProfile(name, handle, bio, location, website, avatarUrl)
                    isEditDialogShowing = false
                }
            )
        }
    }
}

@Composable
fun ProfileStatItem(count: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = count, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onSurface)
        Text(text = label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileDialog(
    currentProfile: UserProfileEntity,
    onDismiss: () -> Unit,
    onSave: (name: String, handle: String, bio: String, location: String, website: String, avatarUrl: String) -> Unit
) {
    var name by remember { mutableStateOf(currentProfile.name) }
    var handle by remember { mutableStateOf(currentProfile.handle) }
    var bio by remember { mutableStateOf(currentProfile.bio) }
    var location by remember { mutableStateOf(currentProfile.location) }
    var website by remember { mutableStateOf(currentProfile.website) }
    var avatarUrl by remember { mutableStateOf(currentProfile.avatarUrl) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Profile", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Display Name") },
                    modifier = Modifier.fillMaxWidth().testTag("edit_profile_name_input")
                )
                OutlinedTextField(
                    value = handle,
                    onValueChange = { handle = it },
                    label = { Text("Username Handle") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text("Bio") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = website,
                    onValueChange = { website = it },
                    label = { Text("Website Link") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(name, handle, bio, location, website, avatarUrl)
                },
                colors = ButtonDefaults.buttonColors(containerColor = SocialIndigo),
                modifier = Modifier.testTag("save_profile_button")
            ) {
                Text("Save Changes", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

private fun String?.isNull_or_blank(): Boolean = this.isNullOrBlank()
