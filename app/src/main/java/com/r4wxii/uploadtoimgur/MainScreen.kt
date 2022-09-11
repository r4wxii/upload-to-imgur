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

@Composable
fun MainScreen() {
    val viewModel = hiltViewModel<MainViewModel>()

    val lifecycleOwner = LocalLifecycleOwner.current
    val requiresTokenFlow = remember(lifecycleOwner) {
        viewModel.requiresToken.flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED,
        )
    }
    val (requiresToken, setRequiresToken) = remember {
        mutableStateOf(true)
    }

    LaunchedEffect(lifecycleOwner) {
        requiresTokenFlow.collect(setRequiresToken)
    }

    val (imageUri, setImageUri) = remember {
        mutableStateOf<Uri?>(null)
    }
    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
            onResult = setImageUri,
        )

    val context = LocalContext.current

    if (requiresToken) {
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
        imageUri?.let {
            AsyncImage(model = imageUri, contentDescription = "", modifier = mod)
        }
    }
}