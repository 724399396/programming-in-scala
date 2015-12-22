package org.stairwaybook.recipe

object SimpleDatabase extends Database with SimpleFoods with SimpleRecipes

object SimpleBrowser extends Browser {
    val database = SimpleDatabase
}
