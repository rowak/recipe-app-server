package io.github.rowak.recipesappserver.net;

import java.io.IOException;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

import io.github.rowak.recipesappserver.models.Category;
import io.github.rowak.recipesappserver.models.Recipe;
import io.github.rowak.recipesappserver.models.RecipeHeader;
import io.github.rowak.recipesappserver.sql.RecipesDB;

public class RequestHandler {
	private final String SRC_URL = "./src/main/";
	private final String CSS_SRC_URL = SRC_URL + "css/";
	private final String JS_SRC_URL = SRC_URL + "js/";
	private final String WEBSTYLE_CSS_URL = CSS_SRC_URL + "webstyle.css";
	private final String LOAD_CATEGORIES_JS_URL = JS_SRC_URL + "load_categories.js";
	private final String LOAD_RECIPE_HEADERS_JS_URL = JS_SRC_URL + "load_recipe_headers.js";
	
	private RecipesDB db = new RecipesDB();
	
	/*
	 * Process a request from the client into a response action.
	 */
	public Response getResponse(Request request) {
		switch (request.getType()) {
			case VERSION:
				JSONObject obj = new JSONObject();
				obj.put("data", Server.VERSION);
				// No client verification required
				return new Response(ResponseType.VERSION, obj);
			case CATEGORIES:
				return getCategoriesResponse(request);
			case RECIPE_HEADERS:
				return getRecipeHeadersResponse(request);
			case RECIPE:
				return getRecipeResponse(request);
			case JS_LOAD_CATEGORIES:
				return getFileResponse(LOAD_CATEGORIES_JS_URL);
			case JS_LOAD_RECIPE_HEADERS:
				return getFileResponse(LOAD_RECIPE_HEADERS_JS_URL);
			case CSS_WEBSTYLE:
				return getFileResponse(WEBSTYLE_CSS_URL);
			default:
				return Response.INVALID_REQUEST;
		}
	}
	
	private Response getCategoriesResponse(Request request) {
		try {
			db.connect();
			Category[] categories = db.getCategories();
			db.disconnect();
			if (categories != null) {
				JSONArray categoriesJson = new JSONArray();
				for (Category category : categories) {
					categoriesJson.put(category.toJSON());
				}
				return new Response(ResponseType.CATEGORIES,
						getDataObject(categoriesJson));
			}
			return Response.RESOURCE_NOT_FOUND;
		} catch (SQLException | IOException e) {
			e.printStackTrace();
			return Response.DATABASE_ERROR;
		}
	}
	
	private Response getRecipeHeadersResponse(Request request) {
		JSONObject data = request.getData();
		String categoryName = data.has("categoryName") ?
				data.getString("categoryName") : null;
		if (categoryName == null) {
			return Response.INVALID_REQUEST;
		}
		
		try {
			db.connect();
			RecipeHeader[] recipeHeaders =
					db.getRecipeHeadersFromCategory(categoryName);
			db.disconnect();
			if (recipeHeaders != null) {
				JSONArray recipeHeadersJson = new JSONArray();
				for (RecipeHeader header : recipeHeaders) {
					recipeHeadersJson.put(header.toJSON());
				}
				return new Response(ResponseType.RECIPE_HEADERS,
						getDataObject(recipeHeadersJson));
			}
			return Response.RESOURCE_NOT_FOUND;
		} catch (SQLException | IOException e) {
			e.printStackTrace();
			return Response.DATABASE_ERROR;
		}
	}
	
	private Response getRecipeResponse(Request request) {
		JSONObject data = request.getData();
		String recipeName = data.has("recipeName") ?
				data.getString("recipeName") : null;
		if (recipeName == null) {
			return Response.INVALID_REQUEST;
		}
		
		try {
			db.connect();
			Recipe recipe = db.getRecipe(recipeName);
			db.disconnect();
			if (recipe != null) {
				return new Response(ResponseType.RECIPE,
						getDataObject(recipe.toJSON()));
			}
			return Response.RESOURCE_NOT_FOUND;
		} catch (SQLException | IOException e) {
			e.printStackTrace();
			return Response.DATABASE_ERROR;
		}
	}
	
	private Response getFileResponse(String url) {
		JSONObject data = new JSONObject();
		data.put("fileUrl", url);
		return new Response(ResponseType.FILE, data);
	}
	
	/*
	 * Wraps a json array object in a json object under the name "data".
	 * Placed directly into the json of a response object.
	 */
	private JSONObject getDataObject(JSONArray arr) {
		JSONObject obj = new JSONObject();
		obj.put("data", arr);
		return obj;
	}
	
	/*
	 * Wraps a json object in another json object under the name "data".
	 * Placed directly into the json of a response object.
	 */
	private JSONObject getDataObject(JSONObject obj) {
		JSONObject objData = new JSONObject();
		objData.put("data", obj);
		return objData;
	}
	
	/*
	 * Wraps a integer value in a json object under the name "data".
	 * Placed directly into the json of a response object.
	 */
	private JSONObject getDataObject(int val) {
		JSONObject obj = new JSONObject();
		obj.put("data", val);
		return obj;
	}
	
	/*
	 * Wraps a boolean value in a json object under the name "data".
	 * Placed directly into the json of a response object.
	 */
	private JSONObject getDataObject(boolean val) {
		JSONObject obj = new JSONObject();
		obj.put("data", val);
		return obj;
	}
}

