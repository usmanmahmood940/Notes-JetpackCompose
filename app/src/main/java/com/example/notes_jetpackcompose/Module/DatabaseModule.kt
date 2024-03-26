package com.example.notes_jetpackcompose.Module

import android.content.Context
import androidx.room.Room
import com.example.demp_productcrud.Room.NotesDatabase
import com.example.notes_jetpackcompose.Utils.Constants.supabaseKey
import com.example.notes_jetpackcompose.Utils.Constants.supabseUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context):NotesDatabase{
        return Room.databaseBuilder(context,NotesDatabase::class.java,"NotesJetpackDb").build()
    }

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = supabseUrl,
            supabaseKey = supabaseKey
        ) {
            install(Postgrest)
            //install other modules
        }
    }




}