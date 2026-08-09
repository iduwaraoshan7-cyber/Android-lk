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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallMade
import androidx.compose.material.icons.filled.CallReceived
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.CallLogEntity
import com.example.data.models.ChatConversationEntity
import com.example.ui.theme.SocialCoral
import com.example.ui.theme.WhatsAppGreen
import coil.compose.AsyncImage

@Composable
fun ChatsScreen(
    conversations: List<ChatConversationEntity>,
    callLogs: List<CallLogEntity>,
    onSelectConversation: (ChatConversationEntity) -> Unit,
    onStartCall: (partnerName: String, partnerAvatarUrl: String, isVideo: Boolean) -> Unit,
    onNewChatClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) } // 0 = Chats, 1 = Updates/Statuses, 2 = Calls
    var searchQuery by remember { mutableStateOf("") }

    val filteredConversations = conversations.filter {
        it.partnerName.contains(searchQuery, ignoreCase = true) ||
        it.lastMessage.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .statusBarsPadding()
            ) {
                // Header Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Messages & Calls",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = WhatsAppGreen
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(onClick = { /* Camera status */ }) {
                            Icon(imageVector = Icons.Default.CameraAlt, contentDescription = "Camera", tint = WhatsAppGreen)
                        }
                        IconButton(onClick = { /* Options */ }) {
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More", tint = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }

                // WhatsApp style Tab selector
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = WhatsAppGreen
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Chats", fontWeight = FontWeight.Bold)
                                if (conversations.sumOf { it.unreadCount } > 0) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Badge(containerColor = WhatsAppGreen) {
                                        Text("${conversations.sumOf { it.unreadCount }}", color = Color.White)
                                    }
                                }
                            }
                        }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Updates", fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        text = { Text("Calls", fontWeight = FontWeight.Bold) }
                    )
                }

                if (selectedTab == 0) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search chats & messages...") },
                        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .testTag("chat_search_input"),
                        shape = RoundedCornerShape(24.dp)
                    )
                }
            }
        },
        floatingActionButton = {
            if (selectedTab == 0) {
                FloatingActionButton(
                    onClick = onNewChatClick,
                    containerColor = WhatsAppGreen,
                    contentColor = Color.White,
                    modifier = Modifier
                        .padding(bottom = 60.dp)
                        .testTag("new_chat_fab")
                ) {
                    Icon(imageVector = Icons.Default.Chat, contentDescription = "New Chat")
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> {
                    // Chats List
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        items(filteredConversations, key = { it.partnerId }) { conversation ->
                            ConversationItemRow(
                                conversation = conversation,
                                onClick = { onSelectConversation(conversation) }
                            )
                        }
                    }
                }
                1 -> {
                    // Statuses / Updates Screen
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        item {
                            Text(
                                text = "Recent Status Updates",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = WhatsAppGreen
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                        items(conversations) { conv ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clip(CircleShape)
                                        .background(WhatsAppGreen)
                                        .padding(2.dp)
                                        .clip(CircleShape)
                                ) {
                                    AsyncImage(
                                        model = conv.partnerAvatarUrl,
                                        contentDescription = conv.partnerName,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = conv.partnerName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        text = conv.statusText,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
                2 -> {
                    // Calls History
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        items(callLogs) { log ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onStartCall(log.contactName, log.contactAvatarUrl, log.isVideoCall)
                                    }
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = log.contactAvatarUrl,
                                    contentDescription = log.contactName,
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = log.contactName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = if (log.isIncoming) Icons.Default.CallReceived else Icons.Default.CallMade,
                                            contentDescription = "Call Direction",
                                            tint = if (log.isMissed) SocialCoral else WhatsAppGreen,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = log.timeAgo,
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                                IconButton(onClick = {
                                    onStartCall(log.contactName, log.contactAvatarUrl, log.isVideoCall)
                                }) {
                                    Icon(
                                        imageVector = if (log.isVideoCall) Icons.Default.Videocam else Icons.Default.Call,
                                        contentDescription = "Call",
                                        tint = WhatsAppGreen
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ConversationItemRow(
    conversation: ChatConversationEntity,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .testTag("chat_conversation_${conversation.partnerId}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            AsyncImage(
                model = conversation.partnerAvatarUrl,
                contentDescription = conversation.partnerName,
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            if (conversation.isOnline) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(WhatsAppGreen)
                        .align(Alignment.BottomEnd)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = conversation.partnerName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = conversation.lastMessageTime,
                    fontSize = 11.sp,
                    color = if (conversation.unreadCount > 0) WhatsAppGreen else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (conversation.isPinned) {
                        Icon(
                            imageVector = Icons.Default.PushPin,
                            contentDescription = "Pinned",
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Text(
                        text = conversation.lastMessage,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (conversation.unreadCount > 0) {
                    Badge(
                        containerColor = WhatsAppGreen,
                        contentColor = Color.White,
                        modifier = Modifier.padding(start = 6.dp)
                    ) {
                        Text("${conversation.unreadCount}")
                    }
                }
            }
        }
    }
}
