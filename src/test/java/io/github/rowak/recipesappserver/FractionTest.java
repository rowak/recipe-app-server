package io.github.rowak.recipesappserver;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.jupiter.api.Test;

import io.github.rowak.recipesappserver.models.Ingredient;
import io.github.rowak.recipesappserver.models.Recipe;
import io.github.rowak.recipesappserver.sql.RecipesDB;
import io.github.rowak.recipesappserver.tools.Fraction;

public class FractionTest {
	@Test
	public void mixedNumberFraction() {
		Fraction[] fracs = {new Fraction(24, 16),
							new Fraction(1, 2),
							new Fraction(3, 2),
							new Fraction(7, 2),
							new Fraction(0, 1),
							new Fraction(2, 2)};
		fracs[1].add(fracs[2]);
		System.out.println(fracs[1].toString());
//		for (Fraction frac : fracs) {
//			System.out.println(frac.toString() + " -> " + frac.toMixedStr());
//		}
	}
	
	@Test
	public void listRecipeIngredients() throws SQLException, IOException {
		RecipesDB db = new RecipesDB();
		db.connect();
		Recipe recipe = db.getRecipe("");
		db.disconnect();
		for (Ingredient ingredient : recipe.getIngredients()) {
			System.out.println(ingredient.toString(4, recipe.getServings()));
		}
	}
}
