package com.example.foodapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth


@Composable
fun Ecra01() {
    var searchQuery by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        // Logo e Nome da App
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center, // Centraliza horizontalmente
            verticalAlignment = Alignment.CenterVertically // Alinha verticalmente
        ) {
            // Logo Circular
            Image(
                painter = painterResource(id = R.drawable.logo), // Substitua pelo nome correto do recurso
                contentDescription = "Logo da App",
                modifier = Modifier
                    .size(64.dp) // Ajuste o tamanho do logo
                    .clip(CircleShape) // Torna o logo circular
                    .border(
                        width = 10.dp,
                        color = Color(0xFFFE4912), // Cor definida diretamente
                        shape = CircleShape
                    ))

            Spacer(modifier = Modifier.width(12.dp)) // Espaço entre o logo e o texto

            // Nome da App com Fonte Customizada
            Text(
                text = "FOODESS",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontFamily = FontFamily.Monospace, // Aplica a fonte monoespaçada
                    fontWeight = FontWeight.ExtraBold, // Torna o texto ainda mais destacado
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize * 1.2 // Aumenta o tamanho do texto
                ),
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Barra de Pesquisa
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Pesquisar restaurantes...") },
            singleLine = true,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_search_24),
                    contentDescription = "Ícone de pesquisa",
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        // Lista de Restaurantes em Grelha
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // Grelha com 2 colunas
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            // Passando uma lista de restaurantes filtrados
            items(
                items = restaurantList.filter {
                    it.name.contains(searchQuery, ignoreCase = true)
                }
            ) { restaurant ->
                RestaurantItem(restaurant = restaurant) // Item individual
            }
        }
    }
}

@Composable
fun RestaurantItem(restaurant: Restaurant) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Imagem do Restaurante
            Image(
                painter = painterResource(id = restaurant.imageResId),
                contentDescription = restaurant.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )

            // Nome do Restaurante
            Text(
                text = restaurant.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(top = 4.dp)
            )

            // Icone de Like e Percentagem
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Likes",
                    tint = Color.Red,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${restaurant.likePercentage}%",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            // Tempo Médio de Entrega
            Text(
                text = "Entrega: ${restaurant.deliveryTime}",
                fontSize = 12.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
@Composable
fun Ecra02() {
    Column(modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)) {
        Text(text = stringResource(id = R.string.forum_str),
            fontWeight = FontWeight.Bold, color = Color.Gray,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center, fontSize = 18.sp
        )
    }
}
@Composable
fun Ecra03() {
    Column(modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)) {
        Text(text = stringResource(id = R.string.groups_str),
            fontWeight = FontWeight.Bold, color = Color.Gray,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center, fontSize = 18.sp
        )
    }
}
@Composable
fun Ecra04(userViewModel: UserViewModel) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    val showDialog = remember { mutableStateOf(false) } // Controle do diálogo

    LaunchedEffect(Unit) {
        if (currentUser != null) {
            userViewModel.updateUserName(currentUser.displayName ?: "Usuário")
            userViewModel.updateUserEmail(currentUser.email ?: "")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Ícone maior no topo
        Icon(
            painter = painterResource(id = R.drawable.baseline_face_24),
            contentDescription = "Ícone de usuário",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(128.dp) // Ícone maior
                .padding(top = 32.dp, bottom = 16.dp) // Espaço no topo
        )

        // Saudação personalizada
        Text(
            text = "Olá, ${userViewModel.userName}",
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp, // Fonte maior para destaque
            color = MaterialTheme.colorScheme.onBackground
        )

        // Linha divisória
        Divider(
            color = MaterialTheme.colorScheme.primary,
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )

        // Email do usuário
        Text(
            text = "Email: ${userViewModel.userEmail}",
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        // Espaço entre os elementos
        Spacer(modifier = Modifier.height(24.dp))

        // Botão para abrir o diálogo de alteração de senha
        Button(
            onClick = { showDialog.value = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Alterar Palavra-passe", fontWeight = FontWeight.Bold)
        }
    }

    // Popup para alterar a senha
    if (showDialog.value) {
        PasswordChangeDialog(
            onDismiss = { showDialog.value = false },
            onPasswordChange = { oldPassword, newPassword, confirmPassword ->
                changePassword(auth, oldPassword, newPassword, confirmPassword)
                showDialog.value = false
            }
        )
    }
}

@Composable
fun PasswordChangeDialog(
    onDismiss: () -> Unit,
    onPasswordChange: (oldPassword: String, newPassword: String, confirmPassword: String) -> Unit
) {
    val oldPassword = remember { mutableStateOf("") }
    val newPassword = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Alterar Palavra-passe", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Campo para a senha antiga
                OutlinedTextField(
                    value = oldPassword.value,
                    onValueChange = { oldPassword.value = it },
                    label = { Text("Senha antiga") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                // Campo para a nova senha
                OutlinedTextField(
                    value = newPassword.value,
                    onValueChange = { newPassword.value = it },
                    label = { Text("Nova senha") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                // Campo para confirmar a nova senha
                OutlinedTextField(
                    value = confirmPassword.value,
                    onValueChange = { confirmPassword.value = it },
                    label = { Text("Confirme a nova senha") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onPasswordChange(oldPassword.value, newPassword.value, confirmPassword.value)
                }
            ) {
                Text("Alterar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

// Método para alterar a senha
private fun changePassword(
    auth: FirebaseAuth,
    oldPassword: String,
    newPassword: String,
    confirmPassword: String
) {
    val user = auth.currentUser
    val email = user?.email

    if (email != null && oldPassword.isNotEmpty() && newPassword.isNotEmpty()) {
        if (newPassword != confirmPassword) {
            println("Erro: As novas senhas não coincidem.")
            return
        }

        // Reautenticar o usuário antes de alterar a senha
        val credential = EmailAuthProvider.getCredential(email, oldPassword)
        user.reauthenticate(credential)
            .addOnCompleteListener { reauthTask ->
                if (reauthTask.isSuccessful) {
                    user.updatePassword(newPassword)
                        .addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful) {
                                println("Senha alterada com sucesso.")
                            } else {
                                println("Erro ao alterar a senha: ${updateTask.exception?.message}")
                            }
                        }
                } else {
                    println("Erro na reautenticação: ${reauthTask.exception?.message}")
                }
            }
    } else {
        println("Erro: Campos vazios ou usuário inválido.")
    }
}