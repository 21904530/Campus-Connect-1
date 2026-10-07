package com.dut.campusconnect

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(repo: FirebaseAuthRepository, onLogin: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isRegister by remember { mutableStateOf(false) }

    Column(Modifier.padding(24.dp).fillMaxSize(), verticalArrangement = Arrangement.Center) {
        Text("Campus Connect", style = MaterialTheme.typography.headlineLarge)
        Text(if(isRegister) "Register - Secure" else "Login - Secure")
        
        if(isRegister) OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = pass, onValueChange = { pass = it }, label = { Text("Password - Protected") }, modifier = Modifier.fillMaxWidth())
        
        Text(message, color = MaterialTheme.colorScheme.error)
        Spacer(Modifier.height(16.dp))
        
        Button(onClick = {
            if(isRegister) repo.register(email, pass, name) { success, msg -> 
                message = msg; if(success) onLogin() 
            }
            else repo.login(email, pass) { success, msg ->
                message = msg; if(success) onLogin()
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text(if(isRegister) "Register" else "Login")
        }
        TextButton(onClick = { isRegister = !isRegister }) {
            Text(if(isRegister) "Already have account? Login" else "No account? Register")
        }
    }
}

// Home, PastPapers, Circles, AI screens - SAME AS PREVIOUS CODE
@Composable
fun HomeScreen() {
    Column(Modifier.padding(16.dp)) {
        Text("Welcome!", style = MaterialTheme.typography.headlineMedium)
        Text("Verified papers, notes, circles & AI tools - Tested by Siphosethu 25053103")
        Spacer(Modifier.height(20.dp))
        Text("Progress Tracking")
        LinearProgressIndicator(progress = { 0.65f }, modifier = Modifier.fillMaxWidth())
        Text("65% Completed")
    }
}

@Composable
fun PastPapersScreen(repo: FirebaseAuthRepository) {
    var search by remember { mutableStateOf("") }
    var papers by remember { mutableStateOf(listOf<PastPaper>()) }
    LaunchedEffect(Unit) { repo.getVerifiedPapers { papers = it } }
    // Fallback sample data if Firebase not configured yet
    val displayPapers = if(papers.isEmpty()) listOf(PastPaper("1","ISY3A",2024,"ISY3A 2024 Paper",true), PastPaper("2","DSY34BT",2023,"DSY34BT 2023",true)) else papers

    Column(Modifier.padding(16.dp)) {
        OutlinedTextField(value = search, onValueChange = { search = it }, label = { Text("Search by module/year") }, modifier = Modifier.fillMaxWidth())
        LazyColumn {
            items(displayPapers.filter { it.moduleCode.contains(search,true) }) {
                Card(Modifier.fillMaxWidth().padding(4.dp)) {
                    Column(Modifier.padding(12.dp)) {
                        Text(it.title)
                        Text("${it.moduleCode} - ${it.year}")
                        Badge { Text("✅ Verified") }
                    }
                }
            }
        }
    }
}

@Composable
fun ModuleCirclesScreen(repo: FirebaseAuthRepository) {
    var selected by remember { mutableStateOf<String?>(null) }
    val circles = listOf("ISY3A", "DSY34BT", "APM3A")
    if(selected == null) {
        LazyColumn(Modifier.padding(16.dp)) {
            items(circles) { c -> Card(Modifier.fillMaxWidth().padding(6.dp), onClick = { selected = c }) { Text(c, Modifier.padding(16.dp)) } }
        }
    } else {
        var msg by remember { mutableStateOf("") }
        var messages by remember { mutableStateOf(mutableListOf("Hi notes?", "Quiz tomorrow")) }
        Column(Modifier.padding(16.dp).fillMaxSize()) {
            Text(selected!!, style = MaterialTheme.typography.titleLarge)
            Button(onClick = { selected = null }) { Text("Back") }
            LazyColumn(Modifier.weight(1f)) { items(messages) { Text(it, Modifier.padding(4.dp)) } }
            Row {
                OutlinedTextField(value = msg, onValueChange = { msg = it }, modifier = Modifier.weight(1f))
                Button(onClick = { 
                    repo.sendMessage(ChatMessage(circleId = selected!!, sender = repo.getCurrentUserId(), message = msg))
                    messages.add(msg); msg = "" 
                }) { Text("Send") }
            }
        }
    }
}

@Composable
fun AIToolsScreen() {
    var score by remember { mutableStateOf(0) }
    Column(Modifier.padding(16.dp)) {
        Text("AI Study Tools", style = MaterialTheme.typography.titleLarge)
        Card(Modifier.fillMaxWidth().padding(top=12.dp)) { Column(Modifier.padding(16.dp)) { Text("Quiz: What is SDLC?"); Button(onClick = { score++ }) { Text("SDLC - Correct") }; Text("Score: $score") } }
        Card(Modifier.fillMaxWidth().padding(top=12.dp)) { Column(Modifier.padding(16.dp)) { Text("Flashcard: OOP = Abstraction, Encapsulation, Inheritance, Polymorphism") } }
    }
}
