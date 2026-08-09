package com.example.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class ReactionType(val emoji: String, val label: String) {
    NONE("", "Like"),
    LIKE("👍", "Like"),
    LOVE("❤️", "Love"),
    HAHA("😆", "Haha"),
    WOW("😮", "Wow"),
    SAD("😢", "Sad"),
    FIRE("🔥", "Fire")
}

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val authorName: String,
    val authorHandle: String,
    val authorAvatarUrl: String,
    val isVerified: Boolean = false,
    val timeAgo: String,
    val contentText: String,
    val mediaUrl: String? = null,
    val mediaAspectRatio: Float = 1.33f, // 4:3 default
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val sharesCount: Int = 0,
    val userReaction: String = ReactionType.NONE.name, // Stored as enum name
    val isBookmarked: Boolean = false,
    val privacyLevel: String = "Public", // Public, Friends, Only Me
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val postId: Long,
    val authorName: String,
    val authorAvatarUrl: String,
    val text: String,
    val timeAgo: String,
    val likesCount: Int = 0,
    val isLiked: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "stories")
data class StoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val authorName: String,
    val authorAvatarUrl: String,
    val storyImageUrl: String,
    val caption: String = "",
    val timeAgo: String = "2h ago",
    val isSeen: Boolean = false,
    val isUserStory: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val conversationPartnerId: Long,
    val senderName: String,
    val text: String,
    val isFromUser: Boolean,
    val isVoiceNote: Boolean = false,
    val voiceDurationSeconds: Int = 0,
    val mediaUrl: String? = null,
    val timestampFormatted: String,
    val readStatus: String = "READ", // SENT, DELIVERED, READ
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "chat_conversations")
data class ChatConversationEntity(
    @PrimaryKey val partnerId: Long,
    val partnerName: String,
    val partnerHandle: String,
    val partnerAvatarUrl: String,
    val isOnline: Boolean,
    val lastMessage: String,
    val lastMessageTime: String,
    val unreadCount: Int = 0,
    val isPinned: Boolean = false,
    val statusText: String = "Hey there! I am using SocialConnect.",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "call_logs")
data class CallLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val contactName: String,
    val contactAvatarUrl: String,
    val isVideoCall: Boolean,
    val isIncoming: Boolean,
    val isMissed: Boolean,
    val timeAgo: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Long = 1, // Current user profile ID
    val name: String,
    val handle: String,
    val bio: String,
    val avatarUrl: String,
    val coverUrl: String,
    val location: String,
    val website: String,
    val followersCount: Int,
    val followingCount: Int,
    val postsCount: Int
)

data class ReelItem(
    val id: Long,
    val authorName: String,
    val authorHandle: String,
    val authorAvatarUrl: String,
    val audioTrackName: String,
    val caption: String,
    val videoBgGradient: List<ULong>,
    val likesCount: Int,
    val commentsCount: Int,
    val isLiked: Boolean = false,
    val isFollowing: Boolean = false
)
