package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.models.*

@Database(
    entities = [
        PostEntity::class,
        CommentEntity::class,
        StoryEntity::class,
        ChatMessageEntity::class,
        ChatConversationEntity::class,
        CallLogEntity::class,
        UserProfileEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SocialDatabase : RoomDatabase() {
    abstract fun socialDao(): SocialDao

    companion object {
        @Volatile
        private var INSTANCE: SocialDatabase? = null

        fun getDatabase(context: Context): SocialDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SocialDatabase::class.java,
                    "social_connect_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
