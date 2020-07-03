function updateRecipeHeaders(recipeHeaders) {
    var oldList = document.getElementById("recipeHeaderList");
    if (oldList != null) {
        oldList.remove();
    }
    var list = document.createElement("ul");
    list.id = "recipeHeaderList";
    for (i = 0; i < recipeHeaders.length; i++) {
        var recipeHeaderLink = document.createElement("a");
        recipeHeaderLink.href = "#" + recipeHeaders[i].name;
        recipeHeaderLink.className = "noStyleLink";
        var recipeHeader = document.createElement("div");
        recipeHeader.className = "recipeHeader";
        var img = document.createElement("img");
        img.src = recipeHeaders[i].imageUrl;
        img.className = "recipeHeaderImage";
        var pRecipeName = document.createElement("h2");
        pRecipeName.className = "recipeHeaderTitle";
        pRecipeName.innerHTML = recipeHeaders[i].name;
        let desc = recipeHeaders[i].description;
        var pRecipeDesc = document.createElement("p");
        pRecipeDesc.className = "recipeHeaderDesc";
        pRecipeDesc.innerHTML = desc != null ? desc : "No description";
        if (desc == null) {
            pRecipeDesc.style = "font-style: italic;";
        }
        recipeHeader.appendChild(img);
        recipeHeader.appendChild(pRecipeName);
        recipeHeader.appendChild(pRecipeDesc);
        recipeHeaderLink.appendChild(recipeHeader);
        list.appendChild(recipeHeaderLink);
    }
    showHeader();
    var headerText = parentCategory != null ? parentCategory + " Recipes" : "Unknown";
    document.getElementById("header").innerHTML = decodeURI(headerText);
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
