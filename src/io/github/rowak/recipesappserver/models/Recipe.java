package io.github.rowak.recipesappserver.models;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Recipe {
	private RecipeHeader header;
	private Ingredient[] ingredients;
	private String description;
	private String directions;  // HTML encoded
	
	public static class Builder {
		private RecipeHeader.Builder headerBuilder;
		private List<Ingredient> ingredients;
		private String description;
		private String directions;
		
		public Builder() {
			headerBuilder = new RecipeHeader.Builder();
			ingredients = new ArrayList<Ingredient>();
		}
		
		public Builder setName(String name) {
			headerBuilder.setName(name);
			return this;
		}
		
		public Builder setCreator(String creator) {
			headerBuilder.setCreator(creator);
			return this;
		}
		
		public Builder setCategory(String category) {
			headerBuilder.setCategory(category);
			return this;
		}
		
		public Builder setPrepTime(int prepTime) {
			headerBuilder.setPrepTime(prepTime);
			return this;
		}
		
		public Builder setCookTime(int cookTime) {
			headerBuilder.setCookTime(cookTime);
			return this;
		}
		
		public Builder setServings(int servings) {
			headerBuilder.setServings(servings);
			return this;
		}
		
		public Builder addIngredient(Ingredient ingredient) {
			ingredients.add(ingredient);
			return this;
		}
		
		public Builder setIngredients(List<Ingredient> ingredients) {
			this.ingredients = ingredients;
			return this;
		}
		
		public Builder setDescription(String description) {
			this.description = description;
			return this;
		}
		
		public Builder setDirections(String directions) {
			this.directions = directions;
			return this;
		}
		
		public Recipe build() {
			Recipe recipe = new Recipe();
			recipe.header = headerBuilder.build();
			recipe.ingredients = ingredients.toArray(new Ingredient[0]);
			recipe.description = description;
			recipe.directions = directions;
			return recipe;
		}
	}
	
	public static Recipe fromJSON(JSONObject json) {
		Recipe.Builder recipeBuilder = new Recipe.Builder();
		if (json.has("header")) {
			RecipeHeader header = RecipeHeader.fromJSON(json.getJSONObject("header"));
			recipeBuilder.setName(header.getName());
			recipeBuilder.setCreator(header.getCreator());
			recipeBuilder.setCategory(header.getCategory());
			recipeBuilder.setPrepTime(header.getPrepTime());
			recipeBuilder.setCookTime(header.getCookTime());
			recipeBuilder.setServings(header.getServings());
		}
		if (json.has("ingredients")) {
			JSONArray ingredientsJson = json.getJSONArray("ingredients");
			for (int i = 0; i < ingredientsJson.length(); i++) {
				recipeBuilder.addIngredient(Ingredient.fromJSON(ingredientsJson.getJSONObject(i)));
			}
		}
		if (json.has("description")) {
			recipeBuilder.setDescription(json.getString("description"));
		}
		if (json.has("directions")) {
			recipeBuilder.setDirections(json.getString("directions"));
		}
		return recipeBuilder.build();
	}
	
	public RecipeHeader getHeader() {
		return header;
	}
	
	public void setHeader(RecipeHeader header) {
		this.header = header;
	}
	
	public Ingredient[] getIngredients() {
		return ingredients;
	}
	
	public void setIngredients(Ingredient[] ingredients) {
		this.ingredients = ingredients;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getDirections() {
		return directions;
	}
	
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		obj.put("header", header.toJSON());
		JSONArray arr = new JSONArray();
		for (Ingredient ingredient : ingredients) {
			arr.put(ingredient.toJSON());
		}
		obj.put("ingredients", arr);
		obj.put("description", description);
		obj.put("directions", directions);
		return obj;
	}
	
	@Override
	public String toString() {
		return header.getName();
	}
}
