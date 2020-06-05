package io.github.rowak.recipesappserver.models;

import org.json.JSONObject;

public class Ingredient {
	private String measurementQty;
	private String measurementUnit;
	private String name;
	private String state;
	private String category;
	
	public static class Builder {
		private String measurementQty;
		private String measurementUnit;
		private String name;
		private String state;
		private String category;
		
		public Builder setMeasurementQty(String measurementQty) {
			this.measurementQty = measurementQty;
			return this;
		}
		
		public Builder setMeasurementUnit(String measurementUnit) {
			this.measurementUnit = measurementUnit;
			return this;
		}
		
		public Builder setName(String name) {
			this.name = name;
			return this;
		}
		
		public Builder setState(String state) {
			this.state = state;
			return this;
		}
		
		public Builder setCategory(String category) {
			this.category = category;
			return this;
		}
		
		public Ingredient build() {
			Ingredient ingredient = new Ingredient();
			ingredient.measurementQty = measurementQty;
			ingredient.measurementUnit = measurementUnit;
			ingredient.name = name;
			ingredient.state = state;
			ingredient.category = category;
			return ingredient;
		}
	}
	
	public static Ingredient fromJSON(JSONObject json) {
		Ingredient.Builder ingredientBuilder = new Ingredient.Builder();
		if (json.has("measurementQty")) {
			ingredientBuilder.setMeasurementQty(json.getString("measurementQty"));
		}
		if (json.has("measurementUnit")) {
			ingredientBuilder.setMeasurementUnit(json.getString("measurementUnit"));
		}
		if (json.has("name")) {
			ingredientBuilder.setName(json.getString("name"));
		}
		if (json.has("state")) {
			ingredientBuilder.setState(json.getString("state"));
		}
		if (json.has("category")) {
			ingredientBuilder.setCategory(json.getString("category"));
		}
		return ingredientBuilder.build();
	}
	
	public String getMeasurementQty() {
		return measurementQty;
	}
	
	public void setMeasurementQty(String measurementQty) {
		this.measurementQty = measurementQty;
	}
	
	public String getMeasurementUnit() {
		return measurementUnit;
	}
	
	public void setMeasurementUnit(String measurementUnit) {
		this.measurementUnit = measurementUnit;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		obj.put("measurementQty", measurementQty);
		obj.put("measurementUnit", measurementUnit);
		obj.put("name", name);
		obj.put("state", state);
		obj.put("category", category);
		return obj;
	}
	
	public toReadableString(int servings) {
		
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (measurementQty != null) {
			sb.append(measurementQty + " ");
		}
		if (measurementUnit != null) {
			sb.append(measurementUnit + " ");
		}
		if (name != null) {
			sb.append(name);
		}
		if (state != null) {
			sb.append(", " + state);
		}
		return capitalizeFirstChar(sb.toString());
	}
	
	private String capitalizeFirstChar(String str) {
		return str.substring(0, 1) + str.substring(1);
	}
}
