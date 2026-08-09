package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.models.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SocialDao {

    // Posts
    @Query("SELECT * FROM posts ORDER BY timestamp DESC")
    fun getAllPosts(): Flow<List<PostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: PostEntity): Long

    @Query("UPDATE posts SET likesCount = :likesCount, userReaction = :reaction WHERE id = :postId")
    suspend fun updatePostReaction(postId: Long, likesCount: Int, reaction: String)

    @Query("UPDATE posts SET isBookmarked = :isBookmarked WHERE id = :postId")
    suspend fun updatePostBookmark(postId: Long, isBookmarked: Boolean)

    @Query("DELETE FROM posts WHERE id = :postId")
    suspend fun deletePost(postId: Long)

    // Comments
    @Query("SELECT * FROM comments WHERE postId = :postId ORDER BY timestamp ASC")
    fun getCommentsForPost(postId: Long): Flow<List<CommentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: CommentEntity)

    // Stories
    @Query("SELECT * FROM stories ORDER BY isUserStory DESC, timestamp DESC")
    fun getAllStories(): Flow<List<StoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: StoryEntity)

    @Query("UPDATE stories SET isSeen = 1 WHERE id = :storyId")
    suspend fun markStorySeen(storyId: Long)

    // Chat Conversations
    @Query("SELECT * FROM chat_conversations ORDER BY isPinned DESC, timestamp DESC")
    fun getAllConversations(): Flow<List<ChatConversationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conversation: ChatConversationEntity)

    @Query("UPDATE chat_conversations SET lastMessage = :lastMessage, lastMessageTime = :time, unreadCount = 0, timestamp = :timestamp WHERE partnerId = :partnerId")
    suspend fun updateConversationLastMessage(partnerId: Long, lastMessage: String, time: String, timestamp: Long)

    // Chat Messages
    @Query("SELECT * FROM chat_messages WHERE conversationPartnerId = :partnerId ORDER BY timestamp ASC")
    fun getMessagesForConversation(partnerId: Long): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(message: ChatMessageEntity)

    // Call Logs
    @Query("SELECT * FROM call_logs ORDER BY timestamp DESC")
    fun getAllCallLogs(): Flow<List<CallLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCallLog(callLog: CallLogEntity)

    // User Profile
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfileEntity)
}
