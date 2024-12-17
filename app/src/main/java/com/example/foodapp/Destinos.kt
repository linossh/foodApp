package com.example.foodapp

sealed class Destino(val route: String, val icon: Int, val title: String) {
    object Ecra01 : Destino(route = "ecra01", icon = R.drawable.baseline_home_24, title = "Home")
    object Ecra02 : Destino(route = "ecra02", icon = R.drawable.baseline_forum_24, title = "Forum")
    object Ecra03 : Destino(route = "ecra03", icon = R.drawable.baseline_groups_24, title = "Group")
    object Ecra04 : Destino(route = "ecra04", icon = R.drawable.baseline_face_24, title = "Profile")
    companion object {
        val toList = listOf(Ecra01, Ecra02, Ecra03, Ecra04)
    }
}