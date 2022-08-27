package com.r4wxii.uploadtoimgur

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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

    val context = LocalContext.current

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
        Spacer(Modifier.height(height = 16.dp))
        Button(onClick = {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://api.imgur.com/oauth2/authorize?client_id=${BuildConfig.imgurClientId}&response_type=token")
            )
            context.startActivity(intent)
        }) {
            Text("認証")
        }
    }
}