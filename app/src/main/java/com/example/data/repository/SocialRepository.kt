package com.example.data.repository

import com.example.data.local.SocialDao
import com.example.data.models.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class SocialRepository(private val dao: SocialDao) {

    val allPosts: Flow<List<PostEntity>> = dao.getAllPosts()
    val allStories: Flow<List<StoryEntity>> = dao.getAllStories()
    val allConversations: Flow<List<ChatConversationEntity>> = dao.getAllConversations()
    val userProfile: Flow<UserProfileEntity?> = dao.getUserProfile()
    val allCallLogs: Flow<List<CallLogEntity>> = dao.getAllCallLogs()

    init {
        // Seed initial data if database is empty
        CoroutineScope(Dispatchers.IO).launch {
            seedDatabaseIfEmpty()
        }
    }

    private suspend fun seedDatabaseIfEmpty() {
        val profile = dao.getUserProfile().firstOrNull()
        if (profile == null) {
            // Seed Profile
            dao.insertOrUpdateProfile(
                UserProfileEntity(
                    id = 1,
                    name = "Alex Morgan",
                    handle = "@alexmorgan",
                    bio = "Digital Creator & Tech Explorer 🚀 | Building the future of social apps ✨",
                    avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=400&q=80",
                    coverUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&w=1200&q=80",
                    location = "San Francisco, CA",
                    website = "linktr.ee/alexmorgan",
                    followersCount = 2840,
                    followingCount = 412,
                    postsCount = 48
                )
            )

            // Seed Stories
            dao.insertStory(
                StoryEntity(
                    authorName = "Your Story",
                    authorAvatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=400&q=80",
                    storyImageUrl = "",
                    caption = "Add to your story",
                    timeAgo = "Tap to add",
                    isSeen = true,
                    isUserStory = true
                )
            )
            dao.insertStory(
                StoryEntity(
                    authorName = "Sarah Jenkins",
                    authorAvatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=400&q=80",
                    storyImageUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=800&q=80",
                    caption = "Sunset vibes in Bali 🌅✨",
                    timeAgo = "1h ago",
                    isSeen = false
                )
            )
            dao.insertStory(
                StoryEntity(
                    authorName = "Liam Chen",
                    authorAvatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=400&q=80",
                    storyImageUrl = "https://images.unsplash.com/photo-1517694712202-14dd9538aa97?auto=format&fit=crop&w=800&q=80",
                    caption = "Late night coding session 💻⚡",
                    timeAgo = "3h ago",
                    isSeen = false
                )
            )
            dao.insertStory(
                StoryEntity(
                    authorName = "Sophia Martinez",
                    authorAvatarUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=400&q=80",
                    storyImageUrl = "https://images.unsplash.com/photo-1544787219-7f47ccb76574?auto=format&fit=crop&w=800&q=80",
                    caption = "Morning matcha latte 🍵",
                    timeAgo = "5h ago",
                    isSeen = true
                )
            )
            dao.insertStory(
                StoryEntity(
                    authorName = "David Kim",
                    authorAvatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=400&q=80",
                    storyImageUrl = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?auto=format&fit=crop&w=800&q=80",
                    caption = "Live concert lights! 🔥🎶",
                    timeAgo = "7h ago",
                    isSeen = true
                )
            )

            // Seed Posts
            val post1Id = dao.insertPost(
                PostEntity(
                    authorName = "Sarah Jenkins",
                    authorHandle = "@sarah_j",
                    authorAvatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=400&q=80",
                    isVerified = true,
                    timeAgo = "2h ago",
                    contentText = "Just launched my new mobile design system! What do you all think? Clean layout, vibrant gradient accents, and seamless dark mode support. Feedback welcomed! 🎨✨ #UIUX #Design #AndroidDev",
                    mediaUrl = "https://images.unsplash.com/photo-1507238691740-187a5b1d37b8?auto=format&fit=crop&w=1000&q=80",
                    likesCount = 142,
                    commentsCount = 18,
                    sharesCount = 6,
                    userReaction = ReactionType.LOVE.name,
                    isBookmarked = true
                )
            )
            val post2Id = dao.insertPost(
                PostEntity(
                    authorName = "Liam Chen",
                    authorHandle = "@liam_tech",
                    authorAvatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=400&q=80",
                    isVerified = false,
                    timeAgo = "4h ago",
                    contentText = "Nothing beats a hot artisanal coffee and passing unit tests on a Saturday morning ☕💻🚀 Who else is coding today?",
                    mediaUrl = "https://images.unsplash.com/photo-1501339847302-ac426a4a7cbb?auto=format&fit=crop&w=1000&q=80",
                    likesCount = 89,
                    commentsCount = 7,
                    sharesCount = 2,
                    userReaction = ReactionType.LIKE.name
                )
            )
            dao.insertPost(
                PostEntity(
                    authorName = "Sophia Martinez",
                    authorHandle = "@sophia_travels",
                    authorAvatarUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=400&q=80",
                    isVerified = true,
                    timeAgo = "6h ago",
                    contentText = "Exploring the neon-lit alleyways of Shinjuku, Tokyo 🇯🇵✨ The energy here at midnight is unreal!",
                    mediaUrl = "https://images.unsplash.com/photo-1503899036084-c55cdd92da26?auto=format&fit=crop&w=1000&q=80",
                    likesCount = 310,
                    commentsCount = 34,
                    sharesCount = 12,
                    userReaction = ReactionType.FIRE.name
                )
            )
            dao.insertPost(
                PostEntity(
                    authorName = "Tech Pulse",
                    authorHandle = "@techpulse_io",
                    authorAvatarUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&w=400&q=80",
                    isVerified = true,
                    timeAgo = "12h ago",
                    contentText = "AI multimodal models are transforming mobile UX rapidly. From instant photo editing to natural voice conversations, what AI feature are you most excited to see in social apps? 🤖💡",
                    mediaUrl = null,
                    likesCount = 524,
                    commentsCount = 89,
                    sharesCount = 45,
                    userReaction = ReactionType.WOW.name
                )
            )

            // Seed Comments
            dao.insertComment(
                CommentEntity(
                    postId = post1Id,
                    authorName = "Liam Chen",
                    authorAvatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=400&q=80",
                    text = "The color palette is super clean! Love the subtle elevation shadows.",
                    timeAgo = "1h ago",
                    likesCount = 12
                )
            )
            dao.insertComment(
                CommentEntity(
                    postId = post1Id,
                    authorName = "Alex Morgan",
                    authorAvatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=400&q=80",
                    text = "Incredible work Sarah! Would love to feature this in our showcase.",
                    timeAgo = "30m ago",
                    likesCount = 5
                )
            )
            dao.insertComment(
                CommentEntity(
                    postId = post2Id,
                    authorName = "David Kim",
                    authorAvatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=400&q=80",
                    text = "Pour over or espresso today? ☕",
                    timeAgo = "2h ago",
                    likesCount = 3
                )
            )

            // Seed Chat Conversations
            val sarahId = 101L
            val liamId = 102L
            val sophiaId = 103L
            val davidId = 104L

            dao.insertConversation(
                ChatConversationEntity(
                    partnerId = sarahId,
                    partnerName = "Sarah Jenkins",
                    partnerHandle = "@sarah_j",
                    partnerAvatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=400&q=80",
                    isOnline = true,
                    lastMessage = "Perfect! See you at 3 PM for coffee ☕",
                    lastMessageTime = "10:42 AM",
                    unreadCount = 2,
                    isPinned = true,
                    statusText = "Designing the future 🎨 ✨"
                )
            )
            dao.insertConversation(
                ChatConversationEntity(
                    partnerId = liamId,
                    partnerName = "Liam Chen",
                    partnerHandle = "@liam_tech",
                    partnerAvatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=400&q=80",
                    isOnline = true,
                    lastMessage = "Sent a voice note 🎙️ (0:14)",
                    lastMessageTime = "Yesterday",
                    unreadCount = 0,
                    isPinned = true,
                    statusText = "At the gym 🏋️‍♂️"
                )
            )
            dao.insertConversation(
                ChatConversationEntity(
                    partnerId = sophiaId,
                    partnerName = "Sophia Martinez",
                    partnerHandle = "@sophia_travels",
                    partnerAvatarUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=400&q=80",
                    isOnline = false,
                    lastMessage = "Loved your recent post! Tokyo photos were stunning 📸",
                    lastMessageTime = "Yesterday",
                    unreadCount = 0,
                    isPinned = false,
                    statusText = "Traveling around Japan 🇯🇵"
                )
            )
            dao.insertConversation(
                ChatConversationEntity(
                    partnerId = davidId,
                    partnerName = "David Kim",
                    partnerHandle = "@david_k",
                    partnerAvatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=400&q=80",
                    isOnline = false,
                    lastMessage = "Check out this link when you get a chance!",
                    lastMessageTime = "2 days ago",
                    unreadCount = 0,
                    isPinned = false,
                    statusText = "Busy coding 🚀"
                )
            )

            // Seed Chat Messages with Sarah
            dao.insertChatMessage(
                ChatMessageEntity(
                    conversationPartnerId = sarahId,
                    senderName = "Sarah Jenkins",
                    text = "Hey Alex! Hope you're having an awesome week.",
                    isFromUser = false,
                    timestampFormatted = "10:35 AM",
                    readStatus = "READ"
                )
            )
            dao.insertChatMessage(
                ChatMessageEntity(
                    conversationPartnerId = sarahId,
                    senderName = "Alex Morgan",
                    text = "Hey Sarah! Doing great, thanks. How's the design system coming along?",
                    isFromUser = true,
                    timestampFormatted = "10:38 AM",
                    readStatus = "READ"
                )
            )
            dao.insertChatMessage(
                ChatMessageEntity(
                    conversationPartnerId = sarahId,
                    senderName = "Sarah Jenkins",
                    text = "It just launched! Are we still meeting for coffee at 3 PM today?",
                    isFromUser = false,
                    timestampFormatted = "10:40 AM",
                    readStatus = "READ"
                )
            )
            dao.insertChatMessage(
                ChatMessageEntity(
                    conversationPartnerId = sarahId,
                    senderName = "Alex Morgan",
                    text = "Yes absolutely! Let's meet at Blue Bottle Coffee downtown.",
                    isFromUser = true,
                    timestampFormatted = "10:41 AM",
                    readStatus = "READ"
                )
            )
            dao.insertChatMessage(
                ChatMessageEntity(
                    conversationPartnerId = sarahId,
                    senderName = "Sarah Jenkins",
                    text = "Perfect! See you at 3 PM for coffee ☕",
                    isFromUser = false,
                    timestampFormatted = "10:42 AM",
                    readStatus = "READ"
                )
            )

            // Seed Messages with Liam
            dao.insertChatMessage(
                ChatMessageEntity(
                    conversationPartnerId = liamId,
                    senderName = "Liam Chen",
                    text = "Hey Alex, I recorded a quick audio note on the backend schema update.",
                    isFromUser = false,
                    timestampFormatted = "Yesterday 4:15 PM",
                    readStatus = "READ"
                )
            )
            dao.insertChatMessage(
                ChatMessageEntity(
                    conversationPartnerId = liamId,
                    senderName = "Liam Chen",
                    text = "Voice message",
                    isFromUser = false,
                    isVoiceNote = true,
                    voiceDurationSeconds = 14,
                    timestampFormatted = "Yesterday 4:16 PM",
                    readStatus = "READ"
                )
            )

            // Seed Call Logs
            dao.insertCallLog(
                CallLogEntity(
                    contactName = "Sarah Jenkins",
                    contactAvatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=400&q=80",
                    isVideoCall = true,
                    isIncoming = true,
                    isMissed = false,
                    timeAgo = "Today, 10:30 AM"
                )
            )
            dao.insertCallLog(
                CallLogEntity(
                    contactName = "Liam Chen",
                    contactAvatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=400&q=80",
                    isVideoCall = false,
                    isIncoming = false,
                    isMissed = false,
                    timeAgo = "Yesterday, 3:20 PM"
                )
            )
            dao.insertCallLog(
                CallLogEntity(
                    contactName = "Sophia Martinez",
                    contactAvatarUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=400&q=80",
                    isVideoCall = true,
                    isIncoming = true,
                    isMissed = true,
                    timeAgo = "August 6, 8:15 PM"
                )
            )
        }
    }

    // Repository methods for UI mutations
    suspend fun createPost(text: String, mediaUrl: String?, privacyLevel: String) {
        val user = dao.getUserProfile().firstOrNull() ?: return
        dao.insertPost(
            PostEntity(
                authorName = user.name,
                authorHandle = user.handle,
                authorAvatarUrl = user.avatarUrl,
                isVerified = true,
                timeAgo = "Just now",
                contentText = text,
                mediaUrl = if (mediaUrl.isNull_or_empty()) null else mediaUrl,
                privacyLevel = privacyLevel
            )
        )
    }

    private fun String?.isNull_or_empty(): Boolean = this.isNullOrBlank()

    suspend fun reactToPost(postId: Long, reaction: ReactionType, currentLikesCount: Int, currentReaction: String) {
        val newReactionName = if (currentReaction == reaction.name) ReactionType.NONE.name else reaction.name
        val delta = if (currentReaction == ReactionType.NONE.name) {
            1
        } else if (newReactionName == ReactionType.NONE.name) {
            -1
        } else {
            0
        }
        val newLikesCount = (currentLikesCount + delta).coerceAtLeast(0)
        dao.updatePostReaction(postId, newLikesCount, newReactionName)
    }

    suspend fun toggleBookmark(postId: Long, currentBookmarked: Boolean) {
        dao.updatePostBookmark(postId, !currentBookmarked)
    }

    suspend fun deletePost(postId: Long) {
        dao.deletePost(postId)
    }

    fun getCommentsForPost(postId: Long): Flow<List<CommentEntity>> = dao.getCommentsForPost(postId)

    suspend fun addComment(postId: Long, text: String) {
        val user = dao.getUserProfile().firstOrNull() ?: return
        dao.insertComment(
            CommentEntity(
                postId = postId,
                authorName = user.name,
                authorAvatarUrl = user.avatarUrl,
                text = text,
                timeAgo = "Just now"
            )
        )
    }

    fun getMessagesForConversation(partnerId: Long): Flow<List<ChatMessageEntity>> =
        dao.getMessagesForConversation(partnerId)

    suspend fun sendChatMessage(partnerId: Long, text: String, isVoiceNote: Boolean = false, durationSeconds: Int = 0) {
        val message = ChatMessageEntity(
            conversationPartnerId = partnerId,
            senderName = "Alex Morgan",
            text = text,
            isFromUser = true,
            isVoiceNote = isVoiceNote,
            voiceDurationSeconds = durationSeconds,
            timestampFormatted = "Just now",
            readStatus = "READ"
        )
        dao.insertChatMessage(message)
        dao.updateConversationLastMessage(
            partnerId = partnerId,
            lastMessage = if (isVoiceNote) "Voice message 🎙️ ($durationSeconds s)" else text,
            time = "Just now",
            timestamp = System.currentTimeMillis()
        )
    }

    suspend fun createNewConversation(name: String, handle: String, avatarUrl: String): Long {
        val partnerId = System.currentTimeMillis()
        dao.insertConversation(
            ChatConversationEntity(
                partnerId = partnerId,
                partnerName = name,
                partnerHandle = handle,
                partnerAvatarUrl = avatarUrl,
                isOnline = true,
                lastMessage = "Started a new chat! Say hello 👋",
                lastMessageTime = "Just now",
                unreadCount = 0,
                isPinned = false
            )
        )
        return partnerId
    }

    suspend fun createStory(imageUrl: String, caption: String) {
        val user = dao.getUserProfile().firstOrNull() ?: return
        dao.insertStory(
            StoryEntity(
                authorName = user.name,
                authorAvatarUrl = user.avatarUrl,
                storyImageUrl = imageUrl,
                caption = caption,
                timeAgo = "Just now",
                isSeen = false,
                isUserStory = true
            )
        )
    }

    suspend fun updateProfile(name: String, handle: String, bio: String, location: String, website: String, avatarUrl: String) {
        val current = dao.getUserProfile().firstOrNull() ?: return
        dao.insertOrUpdateProfile(
            current.copy(
                name = name,
                handle = handle,
                bio = bio,
                location = location,
                website = website,
                avatarUrl = avatarUrl
            )
        )
    }
}
