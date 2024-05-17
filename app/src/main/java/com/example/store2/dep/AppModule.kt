package com.example.store2.dep

import android.app.Application
import android.content.Context.MODE_PRIVATE
import com.example.store2.farebase.FirebaseCommon
import com.example.store2.util.Constants.introductionSPref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFbaseFstoreDB() = Firebase.firestore

    @Provides
    fun provideIntoductionSPref(application: Application)
    = application.getSharedPreferences(introductionSPref,MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideFirebaseCommon(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore
    ) = FirebaseCommon(firestore,firebaseAuth)

    @Provides
    @Singleton
    fun provideFirebaseStorage() = FirebaseStorage.getInstance().reference


}
