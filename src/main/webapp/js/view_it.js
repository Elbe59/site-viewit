function listFilmJSON () {
	let filmRequest = new XMLHttpRequest();
	filmRequest.open("GET", "../list", true);
	filmRequest.responseType = "json";

	filmRequest.onload = function() {
		let list = this.response;
		Lecture(list);
	};
	filmRequest.send();
};

function Lecture(jsonFile) {
	let listOfFilms = ListOfFilms();
	for (i=0;i<listOfFilms.length;i++) {
		for (j=0;j<jsonFile.length;j++) {
			if (listOfFilms[i][1] == jsonFile[j].id) {
				if (jsonFile[j].favori == true) {
					listOfFilms[i][2].querySelector("input").name = "suppfavori";
					listOfFilms[i][2].querySelector("svg").style.color = "red";
					listOfFilms[i][2].querySelector("svg").style.opacity = 1;
				} else {
					listOfFilms[i][2].querySelector("input").name = "addfavori";
				}
				console.log(listOfFilms[i][0],listOfFilms[i][2].querySelector("input").name)
			}
		}
	}
}

function ListOfFilms () {
	let doc = document.getElementsByTagName("article");
	let list = [];
	for (i=0;i<doc.length;i++) {
		list.push([doc[i]["title"],doc[i]["id"],doc[i]]);
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
}

function Rechercher () {
	let input = document.getElementById("recherche").value.toLowerCase();
	let listOfFilms = ListOfFilms();
	let newList = ListOfFilms_Recherche(listOfFilms, input);
	console.log(newList);
};

function addFilmForm(){
	let form=document.querySelectorAll("div.infoAddBlock");
	console.log(form);

}
function addUserForm(){
	let form=document.querySelectorAll("form#ajout_utilisateur > input");
	console.log(form);
}
window.onload = function() {
	let listOfFilms = ListOfFilms();
	addFilmForm();
	listFilmJSON();
	console.log(listOfFilms);
};