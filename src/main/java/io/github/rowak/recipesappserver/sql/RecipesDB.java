package io.github.rowak.recipesappserver.sql;

import java.io.IOException;
import java.net.ConnectException;
import java.nio.channels.AlreadyConnectedException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import io.github.rowak.recipesappserver.models.Ingredient;
import io.github.rowak.recipesappserver.models.Recipe;
import io.github.rowak.recipesappserver.models.RecipeHeader;

public class RecipesDB {
	private Connection conn;
	
	public void connect() throws SQLException, IOException {
		if (conn == null) {
			conn = getConnection();
		} else {
			throw new IOException("Already connected");
		}
	}
	
	public void disconnect() throws SQLException, IOException {
		if (conn != null) {
			conn.close();
			conn = null;
		} else {
			throw new IOException("Not connected");
		}
	}
	
	public Recipe getRecipe(String name) throws SQLException, IOException {
		if (conn != null) {
			ResultSet recipeTable = getRecipeTable(name);
			if (recipeTable.next()) {
				int recipeId = recipeTable.getInt("recipe_id");
				ResultSet ingredientsTable = getRecipeIngredientsTable(recipeId);
				List<Ingredient> ingredients = new ArrayList<Ingredient>();
				while (ingredientsTable.next()) {
					ingredients.add(new Ingredient.Builder()
							.setMeasurementQty(ingredientsTable.getString("measurement_qty_amount"))
							.setMeasurementUnit(ingredientsTable.getString("measurement_unit_name"))
							.setName(ingredientsTable.getString("ingredient_name"))
							.setState(ingredientsTable.getString("ingredient_state_name"))
							.setCategory(ingredientsTable.getString("category_name"))
							.build());
				}
				Recipe recipe = new Recipe.Builder()
						.setName(recipeTable.getString("recipe_name"))
						.setCreator(recipeTable.getString("creator_name"))
						.setDescription(recipeTable.getString("recipe_description"))
						.setDirections(recipeTable.getString("recipe_directions"))
						.setCategory(recipeTable.getString("category_name"))
						.setPrepTime(recipeTable.getInt("prep_time"))
						.setCookTime(recipeTable.getInt("cook_time"))
						.setServings(recipeTable.getInt("servings"))
						.setIngredients(ingredients)
						.build();
				return recipe;
			}
			return null;
		}
		throw new IOException("Not connected");
	}
	
//	public RecipeHeader[] getRecipeHeaders() {
//		
//	}
//	
//	public Recipe[] getRecipes() {
//		
//	}
	
	private ResultSet getRecipeTable(String recipeName) throws SQLException {
		Statement stmt = conn.createStatement();
		return stmt.executeQuery("SELECT * FROM ((recipes " + 
				 "JOIN creators " +
				 "ON recipes.creator_id = creators.creator_id) " +
				 "JOIN recipe_categories " +
				 "ON recipes.category_id = recipe_categories.category_id) " +
				 "WHERE recipes.recipe_name = '" + recipeName + "';");
	}
	
	private ResultSet getRecipeIngredientsTable(int recipeId) throws SQLException {
		Statement stmt = conn.createStatement();
		return stmt.executeQuery("SELECT recipe_ingredients.recipe_id, measurement_qty.measurement_qty_amount, " +
				"measurement_units.measurement_unit_name, ingredients.ingredient_name, ingredient_states.ingredient_state_name, " +
				"ingredient_categories.category_name " + 
				"FROM (((((recipe_ingredients " + 
				"JOIN measurement_qty ON recipe_ingredients.measurement_qty_id = measurement_qty.measurement_qty_id) " + 
				"JOIN measurement_units ON recipe_ingredients.measurement_unit_id = measurement_units.measurement_unit_id) " + 
				"JOIN ingredients ON recipe_ingredients.ingredient_id = ingredients.ingredient_id) " + 
				"JOIN ingredient_states ON recipe_ingredients.ingredient_state_id = ingredient_states.ingredient_state_id) " + 
				"JOIN ingredient_categories ON recipe_ingredients.category_id = ingredient_categories.category_id) " +
				"WHERE recipe_id = " + recipeId + ";");
	}
	
	private static Connection getConnection() throws SQLException {
		String dbUrl = System.getenv("JDBC_DATABASE_URL");
	    return DriverManager.getConnection(dbUrl);
	}
}
