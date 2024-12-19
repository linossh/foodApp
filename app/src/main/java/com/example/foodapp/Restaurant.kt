package com.example.foodapp

data class Restaurant(
    val name: String,
    val imageResId: Int, // ID da imagem
    val likePercentage: Int,
    val deliveryTime: String
)

// Dados fictícios para os restaurantes
val restaurantList = listOf(
    Restaurant("Pizza Palace", R.drawable.pizza_restaurant, 95, "15-20 min"),

    Restaurant("Burger Spot", R.drawable.burger_restaurant, 90, "10-15 min")
    /**
    Restaurant("Sushi Express", R.drawable.sushi, 92, "20-25 min"),
    Restaurant("Pasta House", R.drawable.pasta, 88, "25-30 min"),
    Restaurant("Taco Town", R.drawable.taco, 85, "15-20 min"),
     **/

)