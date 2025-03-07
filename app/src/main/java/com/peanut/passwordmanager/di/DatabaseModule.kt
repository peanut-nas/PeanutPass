package com.peanut.passwordmanager.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.peanut.passwordmanager.data.AccountDao
import com.peanut.passwordmanager.data.AccountDatabase
import com.peanut.passwordmanager.data.repositories.ReadOnlyAccountRepository
import com.peanut.passwordmanager.util.Constants
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
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        AccountDatabase::class.java,
        Constants.DATABASE_NAME
    ).addMigrations(addEncSupport).build()

    @Singleton
    @Provides
    fun provideDao(database: AccountDatabase) = database.accountDao()

    @Singleton
    @Provides
    fun provideReadOnlyDatabase(dao: AccountDao) = ReadOnlyAccountRepository(dao)

}

val addEncSupport = object : Migration(1, 2) {

    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE ${Constants.DATABASE_TABLE} ADD COLUMN iv TEXT NOT NULL DEFAULT ''")
    }

}