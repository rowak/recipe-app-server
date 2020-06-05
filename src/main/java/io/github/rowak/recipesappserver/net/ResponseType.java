package io.github.rowak.recipesappserver.net;

public enum ResponseType
{
	VERSION,  // return server version
	CATEGORIES,  // return recipe category names
	RECIPE_HEADERS,  // return minimal recipe data (name, creator, description) for all recipes
	RECIPE,  // return all recipe data for a specific recipe
	INVALID_REQUEST,
	DATABASE_ERROR,
	RESOURCE_NOT_FOUND
}
