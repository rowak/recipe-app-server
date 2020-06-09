package io.github.rowak.recipesappserver.models;

import org.json.JSONObject;

public class Category {
	private String name;
	private String parent;
	
	public Category(String name, String parent) {
		this.name = name;
		this.parent = parent;
	}
	
	public static Category fromJSON(JSONObject json) {
		Category category = new Category(null, null);
		if (json.has("name")) {
			category.name = json.getString("name");
		}
		if (json.has("parent")) {
			category.parent = json.getString("parent");
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
	
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		if (name != null) {
			obj.put("name", name);
		}
		if (parent != null) {
			obj.put("parent", parent);
		}
		return obj;
	}
	
	@Override
	public String toString() {
		return toJSON().toString();
	}
}
