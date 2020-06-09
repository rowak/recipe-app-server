package io.github.rowak.recipesappserver.net;

public enum RequestType {
	VERSION,			// required: *none*
	CATEGORIES,			// required: *none*
	RECIPE_HEADERS,		// required: *none*
	RECIPE				// required: recipeName
}
