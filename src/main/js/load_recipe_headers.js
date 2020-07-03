function updateRecipeHeaders(recipeHeaders) {
    var oldList = document.getElementById("recipeHeaderList");
    if (oldList != null) {
        oldList.remove();
    }
    var list = document.createElement("ul");
    list.id = "recipeHeaderList";
    for (i = 0; i < recipeHeaders.length; i++) {
        var recipeHeader = document.createElement("div");
        recipeHeader.className = "recipeHeader";
        var img = document.createElement("img");
        img.src = recipeHeaders[i].imageUrl;
        img.className = "recipeHeaderImage";
        var pRecipeName = document.createElement("h2");
        pRecipeName.className = "recipeHeaderTitle";
        pRecipeName.innerHTML = recipeHeaders[i].name;
        var pRecipeDesc = document.createElement("p");
        pRecipeDesc.className = "recipeHeaderDesc";
        pRecipeDesc.innerHTML = recipeHeaders[i].description;
        recipeHeader.appendChild(img);
        recipeHeader.appendChild(pRecipeName);
        recipeHeader.appendChild(pRecipeDesc);
        list.appendChild(recipeHeader);
    }
    showHeader();
    var headerText = parentCategory != null ? parentCategory + " Recipes" : "Unknown";
    document.getElementById("header").innerHTML = headerText.replace("%20", " ");
    document.body.appendChild(list);
}

function loadRecipeHeaders(category) {
    showLoadingText();
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onload = function() {
        var response = JSON.parse(xmlHttp.responseText);
        var recipeHeaders = response.data;
        updateRecipeHeaders(recipeHeaders);
    };
    xmlHttp.open("GET", "recipe_headers?categoryName=" + category);
    xmlHttp.setRequestHeader("Content-Type", "application/json");
    xmlHttp.send(null);
}
