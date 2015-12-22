package org.stairwaybook.recipe

abstract class Browser {
    val database: Database
    def recipesUsing(food: Food) =
        database.allRecipes.filter(recipe =>
            recipe.ingredients.contains(food))

    def displayCategory(category: Database#FoodCategory) {
        println(category)
    }
}
