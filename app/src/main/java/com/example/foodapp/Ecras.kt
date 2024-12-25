package com.example.foodapp

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

val db = Firebase.firestore

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
fun Ecra02(navController: NavHostController) {
    val auth = FirebaseAuth.getInstance() // Instância do Firebase Auth
    val db = FirebaseFirestore.getInstance() // Instância do Firestore
    val currentUser = auth.currentUser
    val currentUserId = currentUser?.uid
    var messages by remember { mutableStateOf(listOf<Triple<String, String, String>>()) } // Lista de mensagens (userId, nome, mensagem)
    var showDialog by remember { mutableStateOf(false) } // Controle do diálogo

    // Função para carregar mensagens do Firestore
    LaunchedEffect(Unit) {
        db.collection("messages")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("Firestore", "Erro ao buscar mensagens", error)
                    return@addSnapshotListener
                }
                if (snapshot != null && !snapshot.isEmpty) {
                    val fetchedMessages = snapshot.documents.map { doc ->
                        val userId = doc.getString("userId") ?: ""
                        val name = doc.getString("name") ?: "Anônimo"
                        val message = doc.getString("message") ?: ""
                        Triple(userId, name, message)
                    }
                    messages = fetchedMessages.reversed() // Inverte para mostrar as mais recentes no final
                }
            }
    }

    // Layout principal
    Column(modifier = Modifier.fillMaxSize()) {
        // Lista de mensagens
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            if (messages.isEmpty()) {
                item {
                    Spacer(modifier = Modifier.weight(1f)) // Preenche o espaço acima quando não há mensagens
                }
            } else {
                // Itens das mensagens
                items(messages) { (userId, userName, message) ->
                    val isCurrentUser = userId == currentUserId
                    val displayName = if (isCurrentUser) "Você" else userName
                    MessageItem(
                        userName = displayName,
                        message = message,
                        isCurrentUser = isCurrentUser,
                        userId = userId,
                        navController = navController // Pass the navController here
                    )
                }
            }
        }

        // Botão para abrir o diálogo
        Button(
            onClick = { showDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Adicionar Review")
        }
    }

    // Exibe o diálogo se necessário
    if (showDialog) {
        ReviewDialog(
            onDismiss = { showDialog = false },
            onSubmit = { restaurant, stars, review ->
                val newMessage = hashMapOf(
                    "name" to (currentUser?.displayName ?: "Anônimo"),
                    "userId" to currentUserId,
                    "message" to "Restaurante: $restaurant\nNota: ${"⭐".repeat(stars)}\n$review"
                )
                db.collection("messages").add(newMessage)
                    .addOnSuccessListener {
                        Log.d("Firestore", "Review adicionada com sucesso!")
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firestore", "Erro ao adicionar review", e)
                    }
                showDialog = false
            }
        )
    }
}

@Composable
fun ReviewDialog(
    onDismiss: () -> Unit,
    onSubmit: (String, Int, String) -> Unit
) {
    var restaurant by remember { mutableStateOf("") }
    var stars by remember { mutableStateOf(3) }
    var review by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Adicionar Review") },
        text = {
            Column {
                // Campo para o nome do restaurante
                OutlinedTextField(
                    value = restaurant,
                    onValueChange = { restaurant = it },
                    label = { Text("Restaurante") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Slider para a nota
                Text(text = "Nota: ${"⭐".repeat(stars)}")
                Slider(
                    value = stars.toFloat(),
                    onValueChange = { stars = it.toInt() },
                    valueRange = 1f..5f,
                    steps = 3,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Campo para o texto da review
                OutlinedTextField(
                    value = review,
                    onValueChange = { review = it },
                    label = { Text("Review") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 4
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                if (restaurant.isNotBlank() && review.isNotBlank()) {
                    onSubmit(restaurant, stars, review)
                }
            }) {
                Text("Enviar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun MessageItem(userName: String, message: String, isCurrentUser: Boolean, userId: String, navController: NavController) {
    val maxWidthDp = (0.8f * LocalConfiguration.current.screenWidthDp).dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!isCurrentUser) {
            IconButton(
                onClick = { navController.navigate("${Destino.profile.route}/$userId") }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_face_24),
                    contentDescription = "Perfil de $userName",
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Column(
            modifier = Modifier
                .widthIn(max = maxWidthDp)
                .background(
                    if (isCurrentUser) Color(0xFFFF6347) else Color(0xFFFFA494),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
        ) {
            Text(
                text = userName,
                fontWeight = FontWeight.Bold,
                color = if (isCurrentUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = message,
                color = if (isCurrentUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun EcraProfile(userId: String, onBack: () -> Unit) {
    val db = FirebaseFirestore.getInstance()
    var userName by remember { mutableStateOf("Carregando...") }
    var userReviews by remember { mutableStateOf(emptyList<String>()) }
    var showAddFriendDialog by remember { mutableStateOf(false) } // Estado do pop-up

    // Buscar dados do usuário e suas reviews
    LaunchedEffect(userId) {
        // Buscar o nome do usuário
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    userName = document.getString("name") ?: "Usuário desconhecido"
                } else {
                    userName = "Usuário não encontrado"
                }
            }
            .addOnFailureListener { e ->
                userName = "Erro ao carregar nome"
            }

        // Buscar reviews do usuário
        db.collection("messages").whereEqualTo("userId", userId).get()
            .addOnSuccessListener { snapshot ->
                userReviews = snapshot.documents.map { it.getString("message") ?: "Sem mensagem" }
            }
            .addOnFailureListener {
                userReviews = listOf("Erro ao carregar reviews.")
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Botão de voltar no canto superior esquerdo
        IconButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                contentDescription = "Voltar",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        // Botão de adicionar amigo no canto superior direito
        IconButton(
            onClick = { showAddFriendDialog = true }, // Abre o diálogo
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_person_add_24),
                contentDescription = "Adicionar amigo",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp), // Deixa espaço para os botões no topo
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Ícone maior no topo
            Icon(
                painter = painterResource(id = R.drawable.baseline_face_24),
                contentDescription = "Ícone de usuário",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(128.dp)
                    .padding(bottom = 16.dp)
            )

            // Nome do usuário
            Text(
                text = userName,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
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

            // Título das reviews
            Text(
                text = "Reviews de $userName:",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp) // Alinha com as reviews
            )

            // Listagem de reviews ou mensagem padrão
            if (userReviews.isEmpty()) {
                Text(
                    text = "Sem reviews disponíveis.",
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.Start)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f) // Preencher o espaço restante
                ) {
                    items(userReviews) { review ->
                        // Item de review com fundo e bordas arredondadas
                        Text(
                            text = review,
                            modifier = Modifier
                                .padding(vertical = 4.dp, horizontal = 8.dp) // Espaçamento entre os itens
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp)) // Borda arredondada
                                .background(Color(0xFFFF6347)) // Fundo com a cor especificada
                                .padding(16.dp), // Espaçamento interno
                            color = Color.White // Cor do texto
                        )
                    }
                }
            }
        }
    }

    // Exibe o pop-up de confirmação para adicionar amigo
    if (showAddFriendDialog) {
        AlertDialog(
            onDismissRequest = { showAddFriendDialog = false },
            title = { Text(text = "Adicionar Amigo") },
            text = { Text(text = "Pretende adicionar $userName à sua lista de amigos?") },
            confirmButton = {
                TextButton(onClick = {
                    val loggedUserId = FirebaseAuth.getInstance().currentUser?.uid
                    if (loggedUserId != null) {
                        db.collection("friends").document(loggedUserId)
                            .set(
                                mapOf("friendsList" to FieldValue.arrayUnion(userId)),
                                SetOptions.merge()
                            )
                            .addOnSuccessListener { Log.d("Firestore", "Amigo adicionado com sucesso.") }
                            .addOnFailureListener { e -> Log.e("Firestore", "Erro ao adicionar amigo.", e) }
                    } else {
                        Log.e("Firestore", "Usuário logado não encontrado.")
                    }
                    showAddFriendDialog = false
                }) {
                    Text("Sim")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddFriendDialog = false }) {
                    Text("Não")
                }
            }
        )
    }
}

@Composable
fun Ecra03() {
    val db = FirebaseFirestore.getInstance()
    val currentUser = FirebaseAuth.getInstance().currentUser
    var friendsList by remember { mutableStateOf(emptyList<Pair<String, String>>()) } // Lista de pares (ID, Nome)

    // Buscar e monitorar a lista de amigos em tempo real
    LaunchedEffect(currentUser?.uid) {
        currentUser?.uid?.let { userId ->
            db.collection("friends").document(userId)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.e("Firestore", "Erro ao ouvir lista de amigos.", e)
                        return@addSnapshotListener
                    }
                    if (snapshot != null && snapshot.exists()) {
                        val friendIds = snapshot.get("friendsList") as? List<String> ?: emptyList()
                        Log.d("Firestore", "IDs dos amigos: $friendIds")

                        if (friendIds.isNotEmpty()) {
                            val friendDetails = mutableListOf<Pair<String, String>>() // Lista de pares (ID, Nome)

                            friendIds.forEach { friendId ->
                                db.collection("users").document(friendId).get()
                                    .addOnSuccessListener { friendDoc ->
                                        val name = friendDoc.getString("name") ?: "Desconhecido"
                                        friendDetails.add(Pair(friendId, name))

                                        // Atualizar a lista quando terminar de buscar os nomes
                                        if (friendDetails.size == friendIds.size) {
                                            friendsList = friendDetails
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e("Firestore", "Erro ao buscar nome do amigo $friendId.", e)
                                    }
                            }
                        } else {
                            friendsList = emptyList()
                        }
                    } else {
                        friendsList = emptyList()
                    }
                }
        }
    }

    // Interface para exibir a lista de amigos
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .wrapContentSize(Alignment.Center)
    ) {
        // Título
        Text(
            text = stringResource(id = R.string.groups_str),
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Exibir a lista de amigos
        if (friendsList.isEmpty()) {
            Text(
                text = "Você ainda não tem amigos.",
                fontWeight = FontWeight.Normal,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(friendsList) { friend ->
                    // Exibir cada amigo como um item da lista
                    Text(
                        text = "Nome: ${friend.second} (ID: ${friend.first})",
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(16.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
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