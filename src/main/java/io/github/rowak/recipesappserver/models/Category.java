package io.github.rowak.recipesappserver.models;

import org.json.JSONObject;

public class Category {
	private String name;
	private String parent;
	private int numRecipes;
	
	public Category(String name, String parent, int numRecipes) {
		this.name = name;
		this.parent = parent;
		this.numRecipes = numRecipes;
	}
	
	public static Category fromJSON(JSONObject json) {
		Category category = new Category(null, null, -1);
		if (json.has("name")) {
			category.name = json.getString("name");
		}
		if (json.has("parent")) {
			category.parent = json.getString("parent");
		}
		if (json.has("numRecipes")) {
			category.numRecipes = json.getInt("numRecipes");
		}
		return category;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getParent() {
		return parent;
	}
	
	public void setParent(String parent) {
		this.parent = parent;
	}
	
	public int getNumRecipes() {
		return numRecipes;
	}
	
	public void setNumRecipes(int numRecipes) {
		this.numRecipes = numRecipes;
	}
	
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		if (name != null) {
			obj.put("name", name);
		}
		if (parent != null) {
			obj.put("parent", parent);
		}
		if (numRecipes != -1) {
			obj.put("numRecipes", numRecipes);
		}
		return obj;
	}
	
	@Override
	public String toString() {
		return toJSON().toString();
	}
}
