package com.folkmanis.laiks.ui.screens.np_data

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.io.File
import java.io.FileOutputStream
import java.io.IOError
import java.net.URL
import java.nio.channels.Channels

@Composable
fun NpDataScreen(
//    viewModel: NpDataViewModel = hiltViewModel()
) {
//    val data by viewModel.npData.collectAsStateWithLifecycle()

    var data by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    Column {
        Button(onClick = {
            val file = File(context.filesDir, "npData.json")
            data = download(file)
        }) {
            Text(text = "Download")
        }

        Text(text = data)
    }


//    Text(text = data.toString())
}

fun download(file: File): String {
    return try {
        val url = URL("https://www.nordpoolgroup.com/api/marketdata/page/59?currency=,EUR,EUR,EUR")
        url.openStream().use {
            Channels.newChannel(it).use { rbc ->
                FileOutputStream(file).use { fos ->
                    fos.channel.transferFrom(rbc, 0, Long.MAX_VALUE)
                }
            }
        }
        "Download OK"

    } catch (err: Error) {
        err.toString()
    }
}