package io.github.rowak.recipesappserver.net;

public enum RequestType {
	/* Android client requests */
	VERSION,			     // required: *none*          returns: server version
	CATEGORIES,			     // required: *none*          returns: categories
	RECIPE_HEADERS,		     // required: categoryName    returns: recipe headers for requested category
	RECIPE,				     // required: recipeName      returns: requested recipe
	
	/* Web client requests */
	JS_LOAD_CATEGORIES,      // required: *none*		  returns: load_categories.js
	JS_LOAD_RECIPE_HEADERS,  // required: *none*		  returns: load_recipe_headers.js
	JS_LOAD_RECIPE,			 // required: *none*		  returns: load_recipe.js
	CSS_WEBSTYLE			 // required: *none*		  returns: webstyle.css
}
