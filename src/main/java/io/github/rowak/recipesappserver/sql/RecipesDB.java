package io.github.rowak.recipesappserver.sql;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import io.github.rowak.recipesappserver.models.Category;
import io.github.rowak.recipesappserver.models.Ingredient;
import io.github.rowak.recipesappserver.models.Recipe;
import io.github.rowak.recipesappserver.models.RecipeHeader;
import io.github.rowak.recipesappserver.net.ImgurImage;

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
				URL url = ImgurImage.getUrl(recipeTable.getString("image_hash"));
				Recipe recipe = new Recipe.Builder()
						.setId(recipeTable.getInt("recipe_id"))
						.setName(recipeTable.getString("recipe_name"))
						.setCreator(recipeTable.getString("creator_name"))
						.setDescription(recipeTable.getString("recipe_description"))
						.setDirections(recipeTable.getString("recipe_directions"))
						.setCategory(recipeTable.getString("category_name"))
						.setImageUrl(url != null ? url.toString() : null)
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
	
//	public RecipeHeader[] getRecipeHeaders() throws SQLException, IOException {
//		if (conn != null) {
//			ResultSet recipesTable = getRecipeHeadersTable();
//			List<RecipeHeader> recipeHeaders = new ArrayList<RecipeHeader>();
//			while (recipesTable.next()) {
//				URL url = ImgurImage.getUrl(recipesTable.getString("image_hash"));
//				recipeHeaders.add(new RecipeHeader.Builder()
//						.setId(recipesTable.getInt("recipe_id"))
//						.setName(recipesTable.getString("recipe_name"))
//						.setCreator(recipesTable.getString("creator_name"))
//						.setDescription(recipesTable.getString("recipe_description"))
//						.setCategory(recipesTable.getString("category_name"))
//						.setImageUrl(url != null ? url.toString() : null)
//						.setPrepTime(recipesTable.getInt("prep_time"))
//						.setCookTime(recipesTable.getInt("cook_time"))
//						.setServings(recipesTable.getInt("servings"))
//						.build());
//			}
//			return recipeHeaders.toArray(new RecipeHeader[0]);
//		}
//		throw new IOException("Not connected");
//	}
	
	public RecipeHeader[] getRecipeHeadersFromCategory(String category)
			throws SQLException, IOException {
		if (conn != null) {
			ResultSet recipesTable = getRecipeHeadersTable(category);
			List<RecipeHeader> recipeHeaders = new ArrayList<RecipeHeader>();
			while (recipesTable.next()) {
				URL url = ImgurImage.getUrl(recipesTable.getString("image_hash"));
				recipeHeaders.add(new RecipeHeader.Builder()
						.setId(recipesTable.getInt("recipe_id"))
						.setName(recipesTable.getString("recipe_name"))
						.setCreator(recipesTable.getString("creator_name"))
						.setDescription(recipesTable.getString("recipe_description"))
						.setCategory(recipesTable.getString("category_name"))
						.setImageUrl(url != null ? url.toString() : null)
						.setPrepTime(recipesTable.getInt("prep_time"))
						.setCookTime(recipesTable.getInt("cook_time"))
						.setServings(recipesTable.getInt("servings"))
						.build());
			}
			return recipeHeaders.toArray(new RecipeHeader[0]);
		}
		throw new IOException("Not connected");
	}
	
	public Recipe[] getRecipes() throws SQLException, IOException {
		if (conn != null) {
			List<Recipe> recipes = new ArrayList<Recipe>();
			ResultSet recipesTable = getRecipesTable();
//			ResultSet ingredientsTable = getIngredientsTable();
			while (recipesTable.next()) {
				List<Ingredient> ingredients = new ArrayList<Ingredient>();
				int recipeId = recipesTable.getInt("recipe_id");
				ResultSet ingredientsTable = getRecipeIngredientsTable(recipeId);
				while (ingredientsTable.next()) {
					ingredients.add(new Ingredient.Builder()
							.setMeasurementQty(ingredientsTable.getString("measurement_qty_amount"))
							.setMeasurementUnit(ingredientsTable.getString("measurement_unit_name"))
							.setName(ingredientsTable.getString("ingredient_name"))
							.setState(ingredientsTable.getString("ingredient_state_name"))
							.setCategory(ingredientsTable.getString("category_name"))
							.build());
				}
				ingredientsTable.getStatement().close();
				URL url = ImgurImage.getUrl(recipesTable.getString("image_hash"));
				recipes.add(new Recipe.Builder()
						.setId(recipesTable.getInt("recipe_id"))
						.setName(recipesTable.getString("recipe_name"))
						.setCreator(recipesTable.getString("creator_name"))
						.setDescription(recipesTable.getString("recipe_description"))
						.setDirections(recipesTable.getString("recipe_directions"))
						.setCategory(recipesTable.getString("category_name"))
						.setImageUrl(url != null ? url.toString() : null)
						.setPrepTime(recipesTable.getInt("prep_time"))
						.setCookTime(recipesTable.getInt("cook_time"))
						.setServings(recipesTable.getInt("servings"))
						.setIngredients(ingredients)
						.build());
			}
			recipesTable.getStatement().close();
			return recipes.toArray(new Recipe[0]);
		}
		throw new IOException("Not connected");
	}
	
	public Category[] getCategories() throws SQLException, IOException {
		if (conn != null) {
			List<Category> categories = new ArrayList<Category>();
			ResultSet categoriesTable = getCategoriesTable();
			while (categoriesTable.next()) {
				categories.add(new Category(categoriesTable.getString("category_name"),
											categoriesTable.getString("category_parent"),
											categoriesTable.getInt("recipe_count")));
			}
			categoriesTable.getStatement().close();
			Category[] categoryArr = categories.toArray(new Category[0]);
			updateCategoryNumRecipes(categoryArr);
			return categoryArr;
		}
		throw new IOException("Not connected");
	}
	
	private void updateCategoryNumRecipes(Category[] categories) {
		for (Category category : categories) {
			Category child = category;
			Category parent;
			while ((parent = getCategoryByName(categories, child.getParent())) != null) {
				parent.setNumRecipes(parent.getNumRecipes() + category.getNumRecipes());
				child = parent;
			}
			if (category.getParent() == null) {
				category.setNumRecipes(category.getNumRecipes() - 1);
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
	
	private ResultSet getCategoriesTable() throws SQLException {
		Statement stmt = conn.createStatement();
		return stmt.executeQuery("SELECT recipe_categories.category_name, " +
				"parent_categories.category_name AS category_parent, COUNT(*) AS recipe_count " + 
				"FROM (recipes " + 
				"RIGHT OUTER JOIN recipe_categories ON recipes.category_id = recipe_categories.category_id) " + 
				"LEFT OUTER JOIN recipe_categories parent_categories ON recipe_categories.category_parent_id = parent_categories.category_id " + 
				"GROUP BY recipe_categories.category_name, parent_categories.category_name;");
//		return stmt.executeQuery("SELECT recipe_categories.category_name, " +
//				"parent_categories.category_name AS category_parent " + 
//				"FROM recipe_categories " + 
//				"LEFT OUTER JOIN recipe_categories parent_categories " + 
//				"ON recipe_categories.category_parent_id = parent_categories.category_id;");
	}
	
	private ResultSet getRecipesTable() throws SQLException {
		Statement stmt = conn.createStatement();
		return stmt.executeQuery("SELECT * FROM (((recipes " + 
				 "JOIN creators ON recipes.creator_id = creators.creator_id) " +
				 "JOIN recipe_categories ON recipes.category_id = recipe_categories.category_id) " +
				 "LEFT OUTER JOIN recipe_images ON recipes.recipe_id = recipe_images.recipe_id);");
	}
	
	private ResultSet getIngredientsTable() throws SQLException {
		Statement stmt = conn.createStatement();
		return stmt.executeQuery("SELECT recipe_ingredients.recipe_id, measurement_qty.measurement_qty_amount, " +
				"measurement_units.measurement_unit_name, ingredients.ingredient_name, ingredient_states.ingredient_state_name, " +
				"ingredient_categories.category_name " + 
				"FROM (((((recipe_ingredients " + 
				"JOIN measurement_qty ON recipe_ingredients.measurement_qty_id = measurement_qty.measurement_qty_id) " + 
				"JOIN measurement_units ON recipe_ingredients.measurement_unit_id = measurement_units.measurement_unit_id) " + 
				"JOIN ingredients ON recipe_ingredients.ingredient_id = ingredients.ingredient_id) " + 
				"JOIN ingredient_states ON recipe_ingredients.ingredient_state_id = ingredient_states.ingredient_state_id) " + 
				"JOIN ingredient_categories ON recipe_ingredients.category_id = ingredient_categories.category_id);");
	}
	
	private ResultSet getRecipeTable(String recipeName) throws SQLException {
		Statement stmt = conn.createStatement();
		return stmt.executeQuery("SELECT * FROM (((recipes " + 
				 "JOIN creators ON recipes.creator_id = creators.creator_id) " +
				 "JOIN recipe_categories ON recipes.category_id = recipe_categories.category_id) " +
				 "LEFT OUTER JOIN recipe_images ON recipes.recipe_id = recipe_images.recipe_id) " +
				 "WHERE recipes.recipe_name = '" + recipeName.replace("'", "''") + "';");
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
	
//	private ResultSet getRecipeHeadersTable() throws SQLException {
//		Statement stmt = conn.createStatement();
//		return stmt.executeQuery("SELECT recipes.recipe_id, recipe_name, creator_name, " +
//				 "recipe_description, category_name, image_hash, prep_time, cook_time, servings " +
//				 "FROM (((recipes " + 
//				 "JOIN creators ON recipes.creator_id = creators.creator_id) " +
//				 "JOIN recipe_categories ON recipes.category_id = recipe_categories.category_id) " +
//				 "LEFT OUTER JOIN recipe_images ON recipes.recipe_id = recipe_images.recipe_id);");
//	}
	
	private ResultSet getRecipeHeadersTable(String categoryName) throws SQLException {
		Statement stmt = conn.createStatement();
		return stmt.executeQuery("SELECT recipes.recipe_id, recipe_name, creator_name, " +
				 "recipe_description, category_name, image_hash, prep_time, cook_time, servings " +
				 "FROM (((recipes " + 
				 "JOIN creators ON recipes.creator_id = creators.creator_id) " +
				 "JOIN recipe_categories ON recipes.category_id = recipe_categories.category_id) " +
				 "LEFT OUTER JOIN recipe_images ON recipes.recipe_id = recipe_images.recipe_id) " +
				 "WHERE category_name = '" + categoryName + "';");
	}
	
	private static Connection getConnection() throws SQLException {
		String dbUrl = System.getenv("JDBC_DATABASE_URL");
	    return DriverManager.getConnection(dbUrl);
	}
}
