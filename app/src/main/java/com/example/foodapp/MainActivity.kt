package com.example.foodapp

import FoodAppTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import androidx.compose.ui.Alignment
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodAppTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(navController = navController, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController = navController, modifier = modifier)
        }
        composable("signup") {
            SignUpScreen(navController = navController, modifier = modifier)
        }
        composable("welcome") {
            ProgramaPrincipal()
        }
    }
}

@Composable
fun LoginScreen(navController: NavController, modifier: Modifier = Modifier) {
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val errorMessage = remember { mutableStateOf("") }
    val auth = FirebaseAuth.getInstance()

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
    Text(
        text = "Bem-vindo!",
        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.primary
    )

    BasicTextField(
        value = username.value,
        onValueChange = {
            username.value = it
            errorMessage.value = "" // Clear error message when user starts typing
        },
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        MaterialTheme.shapes.small
                    ),
                contentAlignment = Alignment.CenterStart
            ) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 8.dp) // Add internal padding
                ) {
                    if (username.value.isEmpty()) {
                        Text("Email", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    }
                    innerTextField()
                }
            }
        },
        modifier = Modifier.fillMaxWidth()
    )

    BasicTextField(
        value = password.value,
        onValueChange = {
            password.value = it
            errorMessage.value = ""
        },
        visualTransformation = PasswordVisualTransformation(),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        MaterialTheme.shapes.small
                    ),
                contentAlignment = Alignment.CenterStart
            ) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 8.dp) // Add internal padding
                ) {
                    if (password.value.isEmpty()) {
                        Text("Senha", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    }
                    innerTextField()
                }
            }
        },
        modifier = Modifier.fillMaxWidth()
    )

            // Error message display
            if (errorMessage.value.isNotEmpty()) {
                Text(
                    text = errorMessage.value,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Button(
                onClick = {
                    if (username.value.isNotEmpty() && password.value.isNotEmpty()) {
                        auth.signInWithEmailAndPassword(username.value, password.value)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // Login bem-sucedido, redirecionar para a tela de boas-vindas
                                    navController.navigate("welcome") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                } else {
                                    // Mostrar erro específico
                                    errorMessage.value = when (val exception = task.exception) {
                                        is FirebaseAuthInvalidCredentialsException -> "Senha incorreta. Tente novamente."
                                        is FirebaseAuthInvalidUserException -> {
                                            if (exception.errorCode == "ERROR_USER_NOT_FOUND") {
                                                "Utilizador não encontrado. Verifique o email."
                                            } else {
                                                "Erro ao fazer login. Tente novamente."
                                            }
                                        }
                                        else -> "Erro ao fazer login. Tente novamente."
                                    }
                                }
                            }
                    } else {
                        errorMessage.value = "Preencha todos os campos."
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Entrar", fontWeight = FontWeight.Bold)
            }

            TextButton(
                onClick = { navController.navigate("signup") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Não tenho conta", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun SignUpScreen(navController: NavController, modifier: Modifier = Modifier) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val auth = FirebaseAuth.getInstance()

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Crie sua conta",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            BasicTextField(
                value = email.value,
                onValueChange = { email.value = it },
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                MaterialTheme.shapes.small
                            ),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 12.dp, vertical = 8.dp) // Add internal padding
                        ) {
                            if (email.value.isEmpty()) {
                                Text("Email", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                            }
                            innerTextField()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            BasicTextField(
                value = password.value,
                onValueChange = { password.value = it },
                visualTransformation = PasswordVisualTransformation(),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                MaterialTheme.shapes.small
                            ),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 12.dp, vertical = 8.dp) // Add internal padding
                        ) {
                            if (password.value.isEmpty()) {
                                Text("Senha", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                            }
                            innerTextField()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            BasicTextField(
                value = confirmPassword.value,
                onValueChange = { confirmPassword.value = it },
                visualTransformation = PasswordVisualTransformation(),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                MaterialTheme.shapes.small
                            ),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 12.dp, vertical = 8.dp) // Add internal padding
                        ) {
                            if (confirmPassword.value.isEmpty()) {
                                Text("Confirmar Senha", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                            }
                            innerTextField()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (email.value.isNotEmpty() && password.value.isNotEmpty()) {
                        if (password.value == confirmPassword.value) {
                            if (android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) {
                                auth.createUserWithEmailAndPassword(email.value, password.value)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            // Cadastro bem-sucedido, pode navegar ou exibir mensagem
                                            navController.navigate("login", )
                                        } else {
                                            // Mostrar erro
                                            task.exception?.message?.let { error ->
                                                println("Erro: $error")
                                            }
                                        }
                                    }
                            } else {
                                println("Erro: Formato de e-mail inválido.")
                            }
                        } else {
                            println("Erro: As senhas não coincidem.")
                        }
                    } else {
                        println("Erro: Todos os campos são obrigatórios.")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cadastrar", fontWeight = FontWeight.Bold)
            }

            TextButton(
                onClick = { navController.navigate("login") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Já tenho conta", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
        }
    }
}

