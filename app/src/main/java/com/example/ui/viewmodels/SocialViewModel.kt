package com.example.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.SocialDatabase
import com.example.data.models.*
import com.example.data.repository.SocialRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SocialViewModel(application: Application) : AndroidViewModel(application) {

    private val db = SocialDatabase.getDatabase(application)
    val repository = SocialRepository(db.socialDao())

    val posts: StateFlow<List<PostEntity>> = repository.allPosts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val stories: StateFlow<List<StoryEntity>> = repository.allStories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userProfile: StateFlow<UserProfileEntity?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Reels mock data
    private val _reels = MutableStateFlow(
        listOf(
            ReelItem(
                id = 1,
                authorName = "Elena Rostova",
                authorHandle = "@elena_design",
                authorAvatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=400&q=80",
                audioTrackName = "Original Sound - Elena Rostova",
                caption = "Sunset timelapse in Amalfi Coast 🌅 Waves crashing against cliffs! #Travel #Amalfi #Reels",
                videoBgGradient = listOf(0xFF1E1B4BUL, 0xFF4338CAUL, 0xFF6366F1UL),
                likesCount = 14200,
                commentsCount = 380,
                isLiked = true,
                isFollowing = false
            ),
            ReelItem(
                id = 2,
                authorName = "Chef Marcus",
                authorHandle = "@marcus_bakes",
                authorAvatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=400&q=80",
                audioTrackName = "Chill Lo-Fi Beats - Midnight Espresso",
                caption = "Making authentic sourdough pizza from scratch! 🍕 Perfect crispy crust recipe in bio.",
                videoBgGradient = listOf(0xFF831843UL, 0xFFBE185DUL, 0xFFF43F5EUL),
                likesCount = 28900,
                commentsCount = 890,
                isLiked = false,
                isFollowing = true
            ),
            ReelItem(
                id = 3,
                authorName = "Aria Chen",
                authorHandle = "@aria_violin",
                authorAvatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=400&q=80",
                audioTrackName = "Aria Chen - Cyberpunk Violin Cover",
                caption = "Acoustic violin cover of Cyberpunk 2077 main theme 🎻⚡ Headphones recommended!",
                videoBgGradient = listOf(0xFF064E3BUL, 0xFF047857UL, 0xFF10B981UL),
                likesCount = 56100,
                commentsCount = 1420,
                isLiked = true,
                isFollowing = true
            )
        )
    )
    val reels: StateFlow<List<ReelItem>> = _reels.asStateFlow()

    // Active Story Viewer State
    private val _activeStory = MutableStateFlow<StoryEntity?>(null)
    val activeStory: StateFlow<StoryEntity?> = _activeStory.asStateFlow()

    // Active Comments Sheet State
    private val _selectedPostForComments = MutableStateFlow<PostEntity?>(null)
    val selectedPostForComments: StateFlow<PostEntity?> = _selectedPostForComments.asStateFlow()

    val currentPostComments: Flow<List<CommentEntity>> = _selectedPostForComments
        .flatMapLatest { post ->
            if (post != null) repository.getCommentsForPost(post.id)
            else flowOf(emptyList())
        }

    fun openStory(story: StoryEntity) {
        _activeStory.value = story
        viewModelScope.launch {
            repository.daoMarkStorySeen(story.id)
        }
    }

    private suspend fun SocialRepository.daoMarkStorySeen(id: Long) {
        db.socialDao().markStorySeen(id)
    }

    fun closeStory() {
        _activeStory.value = null
    }

    fun openComments(post: PostEntity) {
        _selectedPostForComments.value = post
    }

    fun closeComments() {
        _selectedPostForComments.value = null
    }

    fun addComment(postId: Long, text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            repository.addComment(postId, text)
        }
    }

    fun reactToPost(postId: Long, reaction: ReactionType, likesCount: Int, currentReaction: String) {
        viewModelScope.launch {
            repository.reactToPost(postId, reaction, likesCount, currentReaction)
        }
    }

    fun toggleBookmark(postId: Long, isBookmarked: Boolean) {
        viewModelScope.launch {
            repository.toggleBookmark(postId, isBookmarked)
        }
    }

    fun createPost(text: String, mediaUrl: String?, privacyLevel: String) {
        if (text.isBlank() && mediaUrl.isNull_or_blank()) return
        viewModelScope.launch {
            repository.createPost(text, mediaUrl, privacyLevel)
        }
    }

    private fun String?.isNull_or_blank(): Boolean = this.isNullOrBlank()

    fun createStory(imageUrl: String, caption: String) {
        viewModelScope.launch {
            repository.createStory(imageUrl, caption)
        }
    }

    fun toggleLikeReel(reelId: Long) {
        _reels.update { list ->
            list.map { reel ->
                if (reel.id == reelId) {
                    val newLiked = !reel.isLiked
                    val newCount = if (newLiked) reel.likesCount + 1 else reel.likesCount - 1
                    reel.copy(isLiked = newLiked, likesCount = newCount)
                } else reel
            }
        }
    }

    fun updateProfile(name: String, handle: String, bio: String, location: String, website: String, avatarUrl: String) {
        viewModelScope.launch {
            repository.updateProfile(name, handle, bio, location, website, avatarUrl)
        }
    }
}
