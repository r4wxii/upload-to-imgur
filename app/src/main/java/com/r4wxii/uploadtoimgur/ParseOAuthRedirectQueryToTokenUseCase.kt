package com.r4wxii.uploadtoimgur.com.r4wxii.uploadtoimgur

import android.net.Uri
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

class ParseOAuthRedirectQueryToTokenUseCase {
    operator fun invoke(query: String): String {
        val parsedQuery = Uri.parse("?${query}")
        return parsedQuery.getQueryParameter("refresh_token") ?: ""
    }
}

@Module
@InstallIn(ViewModelComponent::class)
class ParseOAuthCallbackQueriesUseCaseModule {
    @Provides
    fun providesParseOAuthCallbackQueriesUseCase() = ParseOAuthRedirectQueryToTokenUseCase()
}