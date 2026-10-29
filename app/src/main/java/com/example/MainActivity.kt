package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.BibleRepository
import com.example.data.DwibibleDatabase
import com.example.ui.DwibibleAppScreen
import com.example.ui.DwibibleViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )

        val database = DwibibleDatabase.getInstance(applicationContext)
        val repository = BibleRepository(database.bibleDao())

        setContent {
            val dwibibleViewModel: DwibibleViewModel = viewModel(
                factory = DwibibleViewModel.Factory(repository)
            )
            DwibibleAppScreen(viewModel = dwibibleViewModel)
        }
    }
}
