package com.ahmetbostancikli.combine.pdf.di


import android.content.Context
import com.ahmetbostancikli.combine.pdf.utils.PdfMergeTool
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton



@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    /*App module is allowed to test the app independently.*/
    @Provides
    @Singleton
    fun providePdfMerge(@ApplicationContext context: Context) = PdfMergeTool(context)


}