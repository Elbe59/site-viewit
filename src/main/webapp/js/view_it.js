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

function ListOfFilms () {
	let doc = document.getElementsByTagName("article");
	let list = [];
	for (i=0;i<doc.length;i++) {
		list.push([doc[i]["title"],doc[i]["id"],doc[i],doc[i]["genre"]]);
	}
	return list;
};

function ListOfFilms_Recherche (listOfFilms, recherche) {
	let new_list = [];
	for (i=0;i<listOfFilms.length;i++){
		if (listOfFilms[i][0].toLowerCase().search(recherche.toLowerCase()) >= 0) {
			new_list.push(listOfFilms[i]);
			listOfFilms[i][2].hidden = false;
		} else {
			listOfFilms[i][2].hidden = true;
		}
	}
	return new_list;
};

function ListOfFilms_Trier (listOfFilms, trier) {
	console.log(trier);
	let new_list = [];
	if(trier == "Genre") {
		new_list.push(listOfFilms.sort());
	}
	return new_list;
};

function Rechercher () {
	let input = document.getElementById("recherche").value.toLowerCase();
	let listOfFilms = ListOfFilms();
	let newList = ListOfFilms_Recherche(listOfFilms, input);
	console.log(newList);
}

function Trier() {
	let listOfFilms = ListOfFilms();
	let select = document.getElementById("trier");
	let selectOption = select.options[select.selectedIndex].value;
	let newList = ListOfFilms_Trier(listOfFilms, selectOption);
	console.log(newList);
}

window.onload = function() {
	let listOfFilms = ListOfFilms();
	console.log(listOfFilms);
};