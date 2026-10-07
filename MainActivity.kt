package com.dut.campusconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        val repo = FirebaseAuthRepository()
        setContent {
            MaterialTheme {
                var screen by remember { mutableStateOf("login") }
                Scaffold(
                    bottomBar = {
                        if(screen != "login") NavigationBar {
                            NavigationBarItem(selected = screen=="home", onClick = { screen="home" }, label = {Text("Home")}, icon = {})
                            NavigationBarItem(selected = screen=="papers", onClick = { screen="papers" }, label = {Text("Papers")}, icon = {})
                            NavigationBarItem(selected = screen=="circles", onClick = { screen="circles" }, label = {Text("Circles")}, icon = {})
                            NavigationBarItem(selected = screen=="ai", onClick = { screen="ai" }, label = {Text("AI")}, icon = {})
                        }
                    }
                ) { padding ->
                    Box(Modifier.padding(padding)) {
                        when(screen) {
                            "login" -> LoginScreen(repo) { screen="home" }
                            "home" -> HomeScreen()
                            "papers" -> PastPapersScreen(repo)
                            "circles" -> ModuleCirclesScreen(repo)
                            "ai" -> AIToolsScreen()
                        }
                    }
                }
            }
        }
    }
}
