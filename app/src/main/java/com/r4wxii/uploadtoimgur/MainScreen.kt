package com.r4wxii.uploadtoimgur

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.Flow

@Composable
fun MainScreen() {
    val viewModel = hiltViewModel<MainViewModel>()
    val state = rememberMainState(viewModel.requiresToken)

    MainScreenContent(state)
}

@Composable
private fun MainScreenContent(state: MainState) {
    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
            onResult = state.setImageUri,
        )


    if (state.requiresToken) {
        val context = LocalContext.current

        AlertDialog(
            onDismissRequest = { },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            ),
            confirmButton = {
                TextButton(
                    onClick = {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://api.imgur.com/oauth2/authorize?client_id=${BuildConfig.imgurClientId}&response_type=token")
                        )
                        context.startActivity(intent)
                    }
                ) {
                    Text("開く")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                    }
                ) {
                    Text("アプリを閉じる")
                }
            },
            title = { Text("トークンがありません") },
            text = { Text("認証ページを開きますか?") },
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        val mod = Modifier
            .fillMaxSize(.6f)

        Box(
            mod
                .background(Color.Gray)
                .clickable {
                    launcher.launch("image/*")
                },
        )
        state.imageUri?.let { uri ->
            AsyncImage(model = uri, contentDescription = "", modifier = mod)
        }
    }
}

private data class MainState(
    val requiresToken: Boolean,
    val imageUri: Uri?,
    val setImageUri: (Uri?) -> Unit,
)

@Composable
private fun rememberMainState(
    requiresTokenFlow: Flow<Boolean>,
): MainState {
    val (requiresToken, setRequiresToken) = remember {
        mutableStateOf(true)
    }

    val (imageUri, setImageUri) = remember {
        mutableStateOf<Uri?>(null)
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    val requiresAuthFlow = remember(lifecycleOwner) {
        requiresTokenFlow.flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED,
        )
    }

    LaunchedEffect(lifecycleOwner) {
        requiresAuthFlow.collect(setRequiresToken)
    }

    return remember(requiresToken, imageUri) {
        MainState(requiresToken = requiresToken, imageUri = imageUri, setImageUri)
    }
}
