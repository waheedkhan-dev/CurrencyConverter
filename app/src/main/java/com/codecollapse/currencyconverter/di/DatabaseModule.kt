package com.codecollapse.currencyconverter.di

import android.content.Context
import androidx.room.Room
import com.codecollapse.currencyconverter.source.local.dao.ExchangeRateDao
import com.codecollapse.currencyconverter.source.local.database.CCAppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideCCAppDb(@ApplicationContext context: Context): CCAppDatabase {
        return Room.databaseBuilder(
            context, CCAppDatabase::class.java,
            CCAppDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideExchangeRateDao(pdfViewerAppDatabase: CCAppDatabase): ExchangeRateDao {
        return pdfViewerAppDatabase.exchangeRateDao()
    }
}