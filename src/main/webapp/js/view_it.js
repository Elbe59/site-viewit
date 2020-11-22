window.onload = function () {
    /*document.getElementById("header_gestion").hidden = true;
    document.getElementById("header_favoris").hidden = true;
    document.getElementById("header_ajoutfilm").hidden = true;*/

};
var fd = new FormData();
fd.append('file', document.getElementById("image-addFilm").files[0]);
var req;
if (window.ActiveXObject) {
    req=new ActiveXObject();
} else {
    req=new XMLHttpRequest();
}
req.open("post", "../user/ajoutfilm", true);
req.send(fd);