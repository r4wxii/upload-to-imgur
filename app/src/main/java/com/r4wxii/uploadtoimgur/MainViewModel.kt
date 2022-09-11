package com.r4wxii.uploadtoimgur

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.r4wxii.uploadtoimgur.com.r4wxii.uploadtoimgur.ParseOAuthRedirectQueryToTokenUseCase
import com.r4wxii.uploadtoimgur.com.r4wxii.uploadtoimgur.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val parseOAuthRedirectQueryToTokenUseCase: ParseOAuthRedirectQueryToTokenUseCase,
) : ViewModel() {
    val requiresToken = tokenRepository.flow.map { it.isBlank() }

    fun setRefreshToken(query: String) {
        val refreshToken = parseOAuthRedirectQueryToTokenUseCase(query)
        viewModelScope.launch { tokenRepository.saveToken(refreshToken) }
    }
}