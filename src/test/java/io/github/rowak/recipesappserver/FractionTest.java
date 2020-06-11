package io.github.rowak.recipesappserver;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;

import io.github.rowak.recipesappserver.models.Category;
import io.github.rowak.recipesappserver.models.Ingredient;
import io.github.rowak.recipesappserver.models.Recipe;
import io.github.rowak.recipesappserver.models.RecipeHeader;
import io.github.rowak.recipesappserver.sql.RecipesDB;
import io.github.rowak.recipesappserver.tools.Fraction;

public class FractionTest {
	@Test
	public void updateCategoryNumRecipes() {
		Category[] categories = {new Category("dinners", null, 0),
								 new Category("casseroles", "dinners", 3),
								 new Category("chicken", "dinners", 5),
								 new Category("breakfast", null, 0),
								 new Category("desserts", null, 0),
								 new Category("pancakes", "breakfast", 2)};
		for (Category category : categories) {
			Category child = category;
			Category parent;
			while ((parent = getCategoryByName(categories, child.getParent())) != null) {
				parent.setNumRecipes(parent.getNumRecipes() + category.getNumRecipes());
				child = parent;
			}
		}
	}
	
	private Category getCategoryByName(Category[] categories, String name) {
		if (name != null) {
			for (Category category : categories) {
				if (category.getName().equals(name)) {
					return category;
				}
			}
		}
		return null;
	}
	
	
//	@Test
//	public void mixedNumberFraction() {
//		Fraction[] fracs = {new Fraction(24, 16),
//							new Fraction(1, 2),
//							new Fraction(3, 2),
//							new Fraction(7, 2),
//							new Fraction(0, 1),
//							new Fraction(2, 2)};
//		fracs[1].add(fracs[2]);
//		System.out.println(fracs[1].toString());
////		for (Fraction frac : fracs) {
////			System.out.println(frac.toString() + " -> " + frac.toMixedStr());
////		}
//	}
//	
////	@Test
////	public void listRecipeIngredients() throws SQLException, IOException {
////		RecipesDB db = new RecipesDB();
////		db.connect();
////		Recipe recipe = db.getRecipe("");
////		db.disconnect();
////		for (Ingredient ingredient : recipe.getIngredients()) {
////			System.out.println(ingredient.toString(8, recipe.getServings()));
////		}
////	}
//	
//	@Test
//	public void recipesShouldBeListedAsJson() throws SQLException, IOException {
//		RecipesDB db = new RecipesDB();
//		db.connect();
//		Recipe[] recipes = db.getRecipes();
//		db.disconnect();
//		System.out.println("RECIPES_JSON:");
//		for (Recipe recipe : recipes) {
//			System.out.println(recipe.toJSON());
//		}
//	}
//	
//	@Test
//	public void recipesIngredientsShouldBeListed() throws SQLException, IOException {
//		RecipesDB db = new RecipesDB();
//		db.connect();
//		Recipe[] recipes = db.getRecipes();
//		db.disconnect();
//		System.out.println("RECIPES:");
//		for (Recipe recipe : recipes) {
//			System.out.println(recipe);
//			for (Ingredient ingredient : recipe.getIngredients()) {
//				System.out.println("  " + ingredient.toString() + "     : " + ingredient.getCategory());
//			}
//		}
//	}
//	
//	@Test
//	public void categoriesShouldBeListedAsString() throws SQLException, IOException {
//		RecipesDB db = new RecipesDB();
//		db.connect();
//		Category[] categories = db.getCategories();
//		db.disconnect();
//		System.out.println("CATEGORIES:");
//		for (Category category : categories) {
//			System.out.println(category.getName() + "     : " + category.getParent());
//		}
//	}
}
