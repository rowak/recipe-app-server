package io.github.rowak.recipesappserver.models;

import org.json.JSONObject;

public class RecipeHeader {
	private String name;
	private String creator;
	private String category;
	private int prepTime;
	private int cookTime;
	private int servings;
	
	public static class Builder {
		private String name;
		private String creator;
		private String category;
		private int prepTime;
		private int cookTime;
		private int servings;
		
		public Builder setName(String name) {
			this.name = name;
			return this;
		}
		
		public Builder setCreator(String creator) {
			this.creator = creator;
			return this;
		}
		
		public Builder setCategory(String category) {
			this.category = category;
			return this;
		}
		
		public Builder setPrepTime(int prepTime) {
			this.prepTime = prepTime;
			return this;
		}
		
		public Builder setCookTime(int cookTime) {
			this.cookTime = cookTime;
			return this;
		}
		
		public Builder setServings(int servings) {
			this.servings = servings;
			return this;
		}
		
		public RecipeHeader build() {
			RecipeHeader header = new RecipeHeader();
			header.name = name;
			header.creator = creator;
			header.category = category;
			header.prepTime = prepTime;
			header.cookTime = cookTime;
			header.servings = servings;
			return header;
		}
	}
	
	public static RecipeHeader fromJSON(JSONObject json) {
		RecipeHeader.Builder headerBuilder = new RecipeHeader.Builder();
		if (json.has("name")) {
			headerBuilder.setName(json.getString("name"));
		}
		if (json.has("creator")) {
			headerBuilder.setCreator(json.getString("creator"));
		}
		if (json.has("category")) {
			headerBuilder.setCategory(json.getString("category"));
		}
		if (json.has("prepTime")) {
			headerBuilder.setPrepTime(json.getInt("prepTime"));
		}
		if (json.has("cookTime")) {
			headerBuilder.setCookTime(json.getInt("cookTime"));
		}
		if (json.has("servings")) {
			headerBuilder.setServings(json.getInt("servings"));
		}
		return headerBuilder.build();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getCreator() {
		return creator;
	}
	
	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public int getPrepTime() {
		return prepTime;
	}
	
	public void setPrepTime(int prepTime) {
		this.prepTime = prepTime;
	}
	
	public int getCookTime() {
		return cookTime;
	}
	
	public void setCookTime(int cookTime) {
		this.cookTime = cookTime;
	}
	
	public int getTotalTime() {
		return prepTime + cookTime;
	}
	
	public int getServings() {
		return servings;
	}
	
	public void setServings(int servings) {
		this.servings = servings;
	}
	
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		obj.put("name", name);
		obj.put("creator", creator);
		obj.put("category", category);
		obj.put("prepTime", prepTime);
		obj.put("cookTime", cookTime);
		obj.put("servings", servings);
		return obj;
	}
	
	@Override
	public String toString() {
		return toJSON().toString();
	}
}
