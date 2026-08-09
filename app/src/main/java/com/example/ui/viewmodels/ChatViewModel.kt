package com.example.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.SocialDatabase
import com.example.data.models.*
import com.example.data.repository.SocialRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val db = SocialDatabase.getDatabase(application)
    val repository = SocialRepository(db.socialDao())

    val conversations: StateFlow<List<ChatConversationEntity>> = repository.allConversations
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val callLogs: StateFlow<List<CallLogEntity>> = repository.allCallLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active Chat Partner
    private val _activeConversationPartner = MutableStateFlow<ChatConversationEntity?>(null)
    val activeConversationPartner: StateFlow<ChatConversationEntity?> = _activeConversationPartner.asStateFlow()

    val currentMessages: Flow<List<ChatMessageEntity>> = _activeConversationPartner
        .flatMapLatest { partner ->
            if (partner != null) repository.getMessagesForConversation(partner.partnerId)
            else flowOf(emptyList())
        }

    // Call Modal State
    private val _activeCallState = MutableStateFlow<CallState?>(null)
    val activeCallState: StateFlow<CallState?> = _activeCallState.asStateFlow()

    data class CallState(
        val partnerName: String,
        val partnerAvatarUrl: String,
        val isVideo: Boolean,
        val durationText: String = "00:05",
        val isConnected: Boolean = true
    )

    fun selectConversation(partner: ChatConversationEntity) {
        _activeConversationPartner.value = partner
    }

    fun closeActiveConversation() {
        _activeConversationPartner.value = null
    }

    fun sendMessage(text: String) {
        val partner = _activeConversationPartner.value ?: return
        if (text.isBlank()) return
        viewModelScope.launch {
            repository.sendChatMessage(partner.partnerId, text)

            // Simulate automatic friendly reply after 1.5 seconds if online
            if (partner.isOnline) {
                delay(1500)
                val replyText = generateAutoReply(text, partner.partnerName)
                repository.sendChatMessage(
                    partnerId = partner.partnerId,
                    text = replyText
                )
                // Also update conversation last message
                db.socialDao().insertChatMessage(
                    ChatMessageEntity(
                        conversationPartnerId = partner.partnerId,
                        senderName = partner.partnerName,
                        text = replyText,
                        isFromUser = false,
                        timestampFormatted = "Just now",
                        readStatus = "READ"
                    )
                )
                db.socialDao().updateConversationLastMessage(
                    partnerId = partner.partnerId,
                    lastMessage = replyText,
                    time = "Just now",
                    timestamp = System.currentTimeMillis()
                )
            }
        }
    }

    fun sendVoiceNote(durationSeconds: Int) {
        val partner = _activeConversationPartner.value ?: return
        viewModelScope.launch {
            repository.sendChatMessage(
                partnerId = partner.partnerId,
                text = "Voice message",
                isVoiceNote = true,
                durationSeconds = durationSeconds
            )
        }
    }

    fun startNewChat(name: String, handle: String, avatarUrl: String) {
        viewModelScope.launch {
            val partnerId = repository.createNewConversation(name, handle, avatarUrl)
            _activeConversationPartner.value = ChatConversationEntity(
                partnerId = partnerId,
                partnerName = name,
                partnerHandle = handle,
                partnerAvatarUrl = avatarUrl,
                isOnline = true,
                lastMessage = "Started a new chat! Say hello 👋",
                lastMessageTime = "Just now"
            )
        }
    }

    fun startCall(partnerName: String, partnerAvatarUrl: String, isVideo: Boolean) {
        _activeCallState.value = CallState(
            partnerName = partnerName,
            partnerAvatarUrl = partnerAvatarUrl,
            isVideo = isVideo
        )
    }

    fun endCall() {
        _activeCallState.value = null
    }

    private fun generateAutoReply(userMessage: String, partnerName: String): String {
        val msg = userMessage.lowercase()
        return when {
            msg.contains("coffee") || msg.contains("meet") -> "Sounds awesome! I'll see you there ☕"
            msg.contains("hello") || msg.contains("hey") || msg.contains("hi") -> "Hey! Great to hear from you. How is your day going?"
            msg.contains("design") || msg.contains("app") -> "Loved the design preview! You guys nailed the UI animation 🚀"
            msg.contains("call") -> "Sure! Let's get on a voice call in a few minutes 📞"
            else -> "That's fantastic! Thanks for sharing $partnerName 😊"
        }
    }
}
