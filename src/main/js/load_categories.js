function getColor(hue) {
    /*let hue = 360*Math.random();*/
    hue %= 360;
    let sat = 1;
    let bri = 1;
    return hsbToHex(hue, sat, bri);
}

function hsbToHex(h, s, b) {
    var rgb = hsbToRgb(h, s, b);
    console.log(rgb[0] + "  " + rgb[1] + "  " + rgb[2] + "     " + "#" + decToHex(rgb[0]) + "  " + decToHex(rgb[1]) + "  " + decToHex(rgb[2]));
    console.log(decToHex(255));
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

/* Adapted from https://en.wikipedia.org/wiki/HSL_and_HSV#HSV_to_RGB_alternative */
function hsbToRgb(h, s, b) {
    function f(n) {
        let k = (n + h / 60) % 6;
        return b - b*s*Math.max(0, Math.min(k, 4 - k, 1));
    }
    return [f(5)*255, f(3)*255, f(1)*255];
}

var parentCategory = null;
var xmlHttp = new XMLHttpRequest();
xmlHttp.onload = function() {
    var response = JSON.parse(xmlHttp.responseText);
    var categories = response.data;
    var list = document.createElement("ul");
    list.className = "categoryList";
    let hue = 0;
    for (i = 0; i < categories.length; i++) {
        let category = categories[i].name;
        var item = document.createElement("li");
        item.className = "categoryItem";
        var link = document.createElement("a");
        link.innerHTML = category;
        link.href = "file?fileName=categoryView.html&category=" + category;
        link.style = "color: " + getColor(hue);
        link.className = "categoryLink";
        item.appendChild(link);
        list.appendChild(item);
        hue += 15;
    }
    var categoriesHeader = document.createElement("h1");
    categoriesHeader.innerHTML = "Categories";
    categoriesHeader.className = "header";
    document.body.appendChild(categoriesHeader);
    document.body.appendChild(list);
    document.getElementById("loadingText").remove();
};
xmlHttp.open("GET", "categories");
xmlHttp.setRequestHeader("Content-Type", "application/json");
xmlHttp.send(null);
