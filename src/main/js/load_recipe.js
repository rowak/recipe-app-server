var NON_PLURAL_UNITS = [
    "oz", "dr", "gt", "gtt", "smdg", "smi",
	"pn", "ds", "ssp", "csp", "fl.dr", "tsp",
	"dsp", "dssp", "dstspn", "tbsp", "Tbsp",
	"oz", "fl.oz", "wgf", "tcf", "pt", "qt",
	"pot", "gal"
];

var CATEGORY_SORT = 1;
var NAME_SORT = 2;

class IngredientSort {
	static sortByCategory(ingredients) {
		this.quickSort(ingredients, 0, ingredients.length-1,
				new SortMethod(CATEGORY_SORT));
	}

	static sortByName(ingredients) {
		this.quickSort(ingredients, 0, ingredients.length-1,
				new SortMethod(NAME_SORT));
	}

	static quickSort(list, l, r, sortMethod) {
		if (l < r) {
			let p = this.partition(list, l, r, sortMethod);
			this.quickSort(list, l, p, sortMethod);
			this.quickSort(list, p+1, r, sortMethod);
		}
	}

	static partition(list, l, r, sortMethod) {
		let pivot = list[Math.floor((l+r)/2)];
		let i = l-1, j = r+1;
		while (true) {
			do {
				i++;
			} while (sortMethod.compare(list[i], pivot) < 0);
			do {
				j--;
			} while (sortMethod.compare(list[j], pivot) > 0);
			if (i >= j) {
				return j;
			}
			this.swap(list, i, j);
		}
	}

	static swap(list, i, j) {
		let temp = list[i];
		list[i] = list[j];
		list[j] = temp;
	}
}

class SortMethod {
    constructor(type) {
        this.type = type;
    }

    compare(ingredient, pivot) {
        switch (this.type) {
            case CATEGORY_SORT:
                return this.compareByCategory(ingredient, pivot);
            case NAME_SORT:
                return this.compareByName(ingredient, pivot);
            default:
                return 0;
        }
    }

    compareByCategory(ingredient, pivot) {
        let ingredientCategory = ingredient.category != null ?
                ingredient.category : "";
        return ingredientCategory.localeCompare(pivot.category != null ? pivot.category : "");
    }

    compareByName(ingredient, pivot) {
        return ingredient.name.localeCompare(pivot.name);
    }
}

class Fraction {
    constructor(num, denom) {
        if (denom < 0) {
            num *= -1;
            denom *= -1;
        }
        this.num = num;
        this.denom = denom;
    }

    static fromString(str) {
        let data = str.split("/");
		if (data.length == 0 || data.length > 2) {
			return null;
		}
        let denom = data.length > 1 ? parseInt(data[1]) : 1;
        return new Fraction(parseInt(data[0]), denom);
    }

    multiply(frac) {
        this.num *= frac.num;
		this.denom *= frac.denom;
    }

    toDecimal() {
		return this.num / this.denom;
	}

    toMixedStr() {
		let simple = this.simplify();
		let whole = Math.floor(simple.num / simple.denom);
		let leftover = simple.num - (whole * simple.denom);
		let str = "";
		if (whole != 0) {
			str += whole;
			if (leftover != 0) {
				str += " ";
			}
		}
		if (leftover != 0) {
			str += leftover + "/" + simple.denom;
		}
		else if (simple.num == 0) {
			str += simple.denom;
		}
        return str;
	}

    simplify() {
		let simple = new Fraction(this.num, this.denom);
		let gcdVal = this.gcd(simple.num, simple.denom);
		simple.num /= gcdVal;
		simple.denom /= gcdVal;
		return simple;
	}

	lcd(a, b) {
		return Math.floor((a * b)/this.gcd(a, b));
	}

	gcd(a, b) {
		return a % b == 0 ? b : this.gcd(b, a % b);
	}
}

function unitIsPlural(ingredient) {
    if (ingredient.measurementQty != null) {
        for (j = 0; j < NON_PLURAL_UNITS.length; j++) {
            if (NON_PLURAL_UNITS[j] == ingredient.measurementUnit) {
                return false;
            }
        }
        let frac = Fraction.fromString(ingredient.measurementQty);
        return frac.toDecimal() > 1;
    }
    return false;
}

function ingredientNameIsPlural(ingredient) {
    if (ingredient.measurementQty != null && ingredient.measurementUnit == null) {
        let frac = Fraction.fromString(ingredient.measurementQty);
        return frac.toDecimal() > 1 && ingredient.name.charAt(ingredient.name.length-1) != 's';
    }
    return false;
}

function capitalizeFirstChar(str) {
    return str.substring(0, 1).toUpperCase() + str.substring(1);
}

function getIngredientString(ingredient, servings, recipeServings) {
    str = "";
    if (ingredient.measurementQty != null) {
        let frac = Fraction.fromString(ingredient.measurementQty);
        frac.multiply(new Fraction(servings, recipeServings));
        str += frac.toMixedStr() + " ";
    }
    if (ingredient.measurementUnit != null) {
        let qty = ingredient.measurementQty == null ?
                capitalizeFirstChar(ingredient.measurementUnit) :
                ingredient.measurementUnit;
        str += qty;
        if (unitIsPlural(ingredient)) {
            str += "s";
        }
        str += " ";
    }
    if (ingredient.name != null) {
        let ingredientName = ingredient.measurementQty == null &&
                ingredient.measurementUnit == null ?
                capitalizeFirstChar(ingredient.name) : ingredient.name;
        str += ingredientName;
        if (ingredientNameIsPlural(ingredient)) {
            str += "s";
        }
    }
    if (ingredient.state != null) {
        str += ", " + ingredient.state;
    }
    return capitalizeFirstChar(str);
}

function categoriesEqual(category1, category2) {
    return (category1 != null && category2 != null &&
            category1 == category2) ||
            (category1 == null && category2 == null);
}

function getIngredientsElement(recipe, servings) {
    var div = document.createElement("div");

    var ingredientsHeader = document.createElement("h2");
    ingredientsHeader.className = "recipeHeaderText";
    ingredientsHeader.innerHTML = "Ingredients";
    div.appendChild(ingredientsHeader);

    var ingredients = recipe.ingredients;
    IngredientSort.sortByName(ingredients);
    IngredientSort.sortByCategory(ingredients);
    var list = document.createElement("ul");
    list.className = "ingredientList";
    if (ingredients[0].category != null) {
        var h4 = document.createElement("h4");
        h4.className = "recipeEmphasisText";
        h4.innerHTML = ingredients[0].category;
        div.appendChild(h4);
    }
    for (i = 0; i < ingredients.length; i++) {
        if (i > 0 && !categoriesEqual(ingredients[i].category,
            ingredients[i-1].category)) {
            div.appendChild(list);
            list = document.createElement("ul");
            list.className = "ingredientList";
            var h4 = document.createElement("h4");
            h4.className = "recipeEmphasisText";
            h4.innerHTML = ingredients[i].category;
            div.appendChild(h4);
        }
        var item = document.createElement("li");
        item.innerHTML = getIngredientString(ingredients[i], servings, recipe.header.servings);
        list.appendChild(item);
    }
    div.appendChild(list);
    return div;
}

function formatTime(minutes) {
    let time = "";
    let days = Math.floor(minutes / (60*24));
    let hours = Math.floor(minutes / 60);
    minutes %= 60;
    if (days > 0) {
        time += days + "d";
        if (hours > 0 || minutes > 0) {
            time += " ";
        }
    }
    if (hours > 0) {
        time += hours + "h";
        if (minutes > 0) {
            time += " ";
        }
    }
    if (minutes > 0 || (days == 0 && hours == 0)) {
        time += minutes + "m";
    }
    return time;
}

function updateRecipe(recipe) {
    var oldList = document.getElementById("recipeHeaderList");
    if (oldList != null) {
        oldList.remove();
    }
    var recipeElmt = document.createElement("div");
    recipeElmt.id = "recipe";
    recipeElmt.className = "recipeHeader";
    var divRecipeHeader = document.createElement("div");
    divRecipeHeader.className = "divGroup";
    var img = document.createElement("img");
    img.src = recipe.header.imageUrl;
    img.className = "recipeHeaderImage";
    var pRecipeName = document.createElement("h1");
    pRecipeName.className = "recipeHeaderTitle";
    pRecipeName.innerHTML = recipe.header.name;
    var pRecipeDesc = document.createElement("p");
    pRecipeDesc.className = "recipeHeaderDesc";
    let desc = recipe.header.description;
    pRecipeDesc.innerHTML = desc != null ? desc : "No description";
    if (desc == null) {
        pRecipeDesc.style = "font-style: italic;";
    }
    var divTimes = document.createElement("div");
    divTimes.className = "divGroup";
    var pPrepTime = document.createElement("p");
    pPrepTime.className = "recipeEmphasisText";
    pPrepTime.innerHTML = "Prep time: " + formatTime(recipe.header.prepTime);
    var pCookTime = document.createElement("p");
    pCookTime.className = "recipeEmphasisText";
    pCookTime.innerHTML = "Cook time: " + formatTime(recipe.header.cookTime);
    var pTotalTime = document.createElement("p");
    pTotalTime.className = "recipeEmphasisText";
    pTotalTime.innerHTML = "Total time: " + formatTime(recipe.header.prepTime + recipe.header.cookTime);
    divTimes.appendChild(pPrepTime);
    divTimes.appendChild(pCookTime);
    divTimes.appendChild(pTotalTime);
    divRecipeHeader.appendChild(img);
    divRecipeHeader.appendChild(pRecipeName);
    divRecipeHeader.appendChild(pRecipeDesc);
    divRecipeHeader.appendChild(divTimes);
    /*var divIngredients = document.createElement("div");*/
    var divIngredients = getIngredientsElement(recipe, recipe.header.servings);
    /*divIngredients.className = "divGroup";*/
    /*
    var ingredientsHeader = document.createElement("h2");
    ingredientsHeader.className = "recipeHeaderText";
    ingredientsHeader.innerHTML = "Ingredients";
    */
    /*
    var ingredientsList = document.createElement("ul");
    ingredientsList.id = "ingredientList";
    IngredientSort.sortByName(recipe.ingredients);
    IngredientSort.sortByCategory(recipe.ingredients);
    for (i = 0; i < recipe.ingredients.length; i++) {
        var ingredientItem = document.createElement("li");
        ingredientItem.innerHTML = getIngredientString(recipe.ingredients[i], recipe.header.servings, recipe.header.servings);
        ingredientsList.appendChild(ingredientItem);
    }
    */
    /*divIngredients.appendChild(ingredientsHeader);*/
    /*divIngredients.appendChild(ingredientsList);*/
    var directionsHeader = document.createElement("h2");
    directionsHeader.className = "recipeHeaderText";
    directionsHeader.innerHTML = "Directions";
    var pDirections = document.createElement("p");
    pDirections.className = "recipeMediumText";
    pDirections.innerHTML = recipe.directions;
    recipeElmt.appendChild(divRecipeHeader);
    recipeElmt.appendChild(divIngredients);
    recipeElmt.appendChild(directionsHeader);
    recipeElmt.appendChild(pDirections);
    showHeader();
    /*var headerText = "Recipe";*/
    document.getElementById("header").innerHTML = decodeURI(recipe.header.name);
    document.body.appendChild(recipeElmt);
}

function loadRecipe(recipeName) {
    showLoadingText();
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onload = function() {
        var response = JSON.parse(xmlHttp.responseText);
        var recipe = response.data;
        updateRecipe(recipe);
    };
    xmlHttp.open("GET", "recipe?recipeName=" + recipeName);
    xmlHttp.setRequestHeader("Content-Type", "application/json");
    xmlHttp.send(null);
}
