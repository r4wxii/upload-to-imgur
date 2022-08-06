package com.r4wxii.uploadtoimgur

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import coil.compose.AsyncImage

@Composable
fun MainScreen() {
    val (imageUri, setImageUri) = remember {
        mutableStateOf<Uri?>(null)
    }
    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
            onResult = setImageUri,
        )

    Box(Modifier.fillMaxSize()) {
        val mod = Modifier
            .fillMaxSize(.6f)
            .align(Alignment.Center)

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