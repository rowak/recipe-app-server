function getColor(hue) {
    /*let hue = 360*Math.random();*/
    hue %= 360;
    let sat = 1;
    let bri = 1;
    return hsbToHex(hue, sat, bri);
}

function hsbToHex(h, s, b) {
    var rgb = hsbToRgb(h, s, b);
    return "#" + decToHex(rgb[0]) + decToHex(rgb[1]) + decToHex(rgb[2]);
}

function decToHex(n) {
    n = Math.round(n);
    var hex = "";
    let c;
    while (n > 0) {
        c = n % 16 + 48;
        if (c > 57) {
            c += 7;
        }
        hex = String.fromCharCode(c) + hex;
        n = Math.floor(n / 16);
    }
    while (hex.length < 2) {
        hex = "0" + hex;
    }
    return hex;
}

/* From https://en.wikipedia.org/wiki/HSL_and_HSV#HSV_to_RGB_alternative */
function hsbToRgb(h, s, b) {
    function f(n) {
        let k = (n + h / 60) % 6;
        return b - b*s*Math.max(0, Math.min(k, 4 - k, 1));
    }
    return [f(5)*255, f(3)*255, f(1)*255];
}

function getNumChildren(parent, categories) {
    let children = 0;
    for (j = 0; j < categories.length; j++) {
        if (categories[j].parent == parent) {
            children++;
        }
    }
    return children;
}

function getCategoryFromHash() {
    let c = window.location.hash.substring(1);
    if (!c) {
        c = null;
    }
    return c;
}

function showLoadingText() {
    var header = document.getElementById("header");
    if (header != null) {
        header.remove();
    }
    var categoryList = document.getElementById("categoryList");
    if (categoryList != null) {
        categoryList.remove();
    }
    var recipeHeaderList = document.getElementById("recipeHeaderList");
    if (recipeHeaderList != null) {
        recipeHeaderList.remove();
    }
    var loadingText = document.createElement("p");
    loadingText.id = "loadingText";
    loadingText.innerHTML = "Loading...";
    document.body.appendChild(loadingText);
}

function showHeader() {
    var header = document.getElementById("header");
    if (header != null) {
        header.remove();
    }
    var loadingText = document.getElementById("loadingText");
    if (loadingText != null) {
        loadingText.remove();
    }
    var categoryList = document.getElementById("categoryList");
    if (categoryList != null) {
        categoryList.remove();
    }
    var recipeHeaderList = document.getElementById("recipeHeaderList");
    if (recipeHeaderList != null) {
        recipeHeaderList.remove();
    }
    var categoriesHeader = document.createElement("h1");
    categoriesHeader.innerHTML = "Categories";
    categoriesHeader.id = "header";
    document.body.appendChild(categoriesHeader);
}

function updateCategories(category, categories) {
    parentCategory = category;
    showHeader();
    var oldList = document.getElementById("categoryList");
    if (oldList != null) {
        oldList.remove();
    }
    var headerText = parentCategory != null ? parentCategory : "Categories";
    document.getElementById("header").innerHTML = headerText.replace("%20", " ");
    var list = document.createElement("ul");
    list.id = "categoryList";
    let hue = 0;
    for (i = 0; i < categories.length; i++) {
        let category = categories[i].name;
        let parent = categories[i].parent;
        if (parent == parentCategory) {
            var item = document.createElement("li");
            item.className = "categoryItem";
            var link = document.createElement("a");
            link.innerHTML = category;
            link.href = "#" + category;
            if (getNumChildren(category, categories) == 0) {
                /*link.href = "categoryView?category=" + category;*/
                link.addEventListener("click", function() {loadRecipeHeaders(category)});
            }
            else {
                /*link.href = "#" + category;*/
                link.addEventListener("click", function() {updateCategories(category, categories)});
            }
            link.style = "color: " + getColor(hue);
            link.className = "categoryLink";
            item.appendChild(link);
            list.appendChild(item);
            hue += 20;
        }
        else if (category == parentCategory && getNumChildren(category, categories) == 0) {
            loadRecipeHeaders(category)
        }
    }
    document.body.appendChild(list);
}

function loadCategories(categories) {
    /*
    var categoriesHeader = document.createElement("h1");
    categoriesHeader.innerHTML = "Categories";
    categoriesHeader.id = "header";
    document.body.appendChild(categoriesHeader);
    */


    /*showHeader();*/

    updateCategories(parentCategory, categories);

    /*document.getElementById("loadingText").remove();*/
}

var parentCategory = getCategoryFromHash();
var xmlHttp = new XMLHttpRequest();
xmlHttp.onload = function() {
    var response = JSON.parse(xmlHttp.responseText);
    var categories = response.data;
    loadCategories(categories);

    window.onhashchange = function() {
        updateCategories(getCategoryFromHash(), categories);
    }
};
xmlHttp.open("GET", "categories");
xmlHttp.setRequestHeader("Content-Type", "application/json");
xmlHttp.send(null);
