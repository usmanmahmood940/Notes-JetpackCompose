package com.example.notes_jetpackcompose

import android.content.Context
import androidx.room.Room
import com.example.demp_productcrud.Room.NotesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context):NotesDatabase{
        return Room.databaseBuilder(context,NotesDatabase::class.java,"NotesJetpackDb").build()
    }


}