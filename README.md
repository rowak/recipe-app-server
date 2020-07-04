# RecipeApp Server
This is the backend and web frontend for RecipeApp, which is a simple app for viewing recipes from a database.

The server can be run using your own database by setting the <code>JDBC\_DATABASE\_URL</code> environment variable to the JDBC url of your database on the desired server host machine. For a MYSQL database for example, the url format is <code>jdbc:mysql://&lt;hostname&gt;:&lt;port&gt;/&lt;dbname&gt;</code>. The default port for the RecipeApp server is 1967, but it can be changed using the <code>-p PORT</code> argument.

## Database Structure
The following code creates the necessary tables for a *postgres* database.
```sql
CREATE TABLE recipes (recipe_id SERIAL PRIMARY KEY, recipe_name TEXT, creator_id INTEGER, recipe_description TEXT, recipe_directions TEXT, prep_time INTEGER, cook_time INTEGER, servings INTEGER, category_id INTEGER, recipe_url TEXT);
CREATE TABLE creators (creator_id SERIAL PRIMARY KEY, creator_name TEXT);
CREATE TABLE measurement_qty (measurement_qty_id SERIAL PRIMARY KEY, measurement_qty_name TEXT);
CREATE TABLE measurement_units (measurement_unit_id SERIAL PRIMARY KEY, measurement_unit_name TEXT);
CREATE TABLE ingredients (ingredient_id SERIAL PRIMARY KEY, ingredient_name TEXT);
CREATE TABLE ingredient_states (ingredient_state_id SERIAL PRIMARY KEY, ingredient_state_name TEXT);
CREATE TABLE recipe_ingredients (recipe_id INTEGER, measurement_qty_id INTEGER, measurement_unit_id INGEGER, ingredient_id INTEGER, ingredient_state_id INTEGER, category_id INTEGER);
CREATE TABLE recipe_images (image_id SERIAL PRIMARY KEY, recipe_id INTEGER, image_hash TEXT);
CREATE TABLE recipe_categories (category_id SERIAL PRIMARY KEY, category_parent_id INTEGER, category_name TEXT);
CREATE TABLE ingredient_categories (category_id SERIAL PRIMARY KEY, category_name TEXT);
```
