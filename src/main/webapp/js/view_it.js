let ACTUAL_USER_ID =0;

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
					listOfFilms[i][3].querySelector("input").name = "suppfavori";
					listOfFilms[i][3].querySelector("svg").style.color = "red";
					listOfFilms[i][3].querySelector("svg").style.opacity = 0.8;
				} else {
					listOfFilms[i][3].querySelector("input").name = "addfavori";
				}
				if (listOfFilms[i][4] != null && listOfFilms[i][5] != null) {
					if (jsonFile[j].avis == "like") {
						listOfFilms[i][4].querySelector("input").name = "remove";
						listOfFilms[i][4].querySelector("svg").style.color = "green";
						listOfFilms[i][5].querySelector("input").name = "adddislike";
						listOfFilms[i][5].querySelector("svg").style.color = "grey";
					} else if (jsonFile[j].avis == "dislike") {
						listOfFilms[i][4].querySelector("input").name = "addlike";
						listOfFilms[i][4].querySelector("svg").style.color = "grey";
						listOfFilms[i][5].querySelector("input").name = "remove";
						listOfFilms[i][5].querySelector("svg").style.color = "red";
					} else {
						listOfFilms[i][4].querySelector("input").name = "addlike";
						listOfFilms[i][4].querySelector("svg").style.color = "grey";
						listOfFilms[i][5].querySelector("input").name = "adddislike";
						listOfFilms[i][5].querySelector("svg").style.color = "grey";
					}
				}
				listOfFilms[i][2].innerText = jsonFile[j].pourcentage+"%";
			}
		}
	}
}

function ListOfFilms () {
	let doc = document.getElementsByTagName("article");
	let list = [];
	for (i=0;i<doc.length;i++) {
		list.push([doc[i]["title"],doc[i]["id"],doc[i].querySelector("p"),doc[i].getElementsByTagName("form")[0],null,null]);
	}
	if (list.length == 0) {
		doc = document.getElementsByClassName("filmDetail");
		for (i=0;i<doc.length;i++) {
			list.push([doc[i].title,doc[i].id,doc[i].querySelector("div#pourcentage"),doc[i].getElementsByClassName("favori")[0],doc[i].getElementsByClassName("like")[0],doc[i].getElementsByClassName("dislike")[0]]);
		}
	}
	return list;
};

function ListOfFilms_search () {
	let doc = document.getElementsByTagName("article");
	let list = [];
	for (i=0;i<doc.length;i++) {
		list.push([doc[i]["title"],doc[i]["id"],doc[i],false]);
	}
	return list;
};

function ListOfFilms_Recherche (listOfFilms, recherche) {
	let new_list = [];
	let nb = 0;
	for (i=0;i<listOfFilms.length;i++){
		if (listOfFilms[i][0].toLowerCase().search(recherche.toLowerCase()) >= 0) {
			listOfFilms[i][3] = false;
			nb = nb + 1;
		} else {
			listOfFilms[i][3] = true;
		}
		new_list.push(listOfFilms[i]);
	}
	if (nb >= 1) {
		for (i=0;i<listOfFilms.length;i++){
			listOfFilms[i][2].hidden = listOfFilms[i][3];
		}
		return new_list;
	} else {
		return listOfFilms;
	}
};

function ListOfFilms_Trier (listOfFilms, trier) {
	let new_list = [];
	if(trier == "Genre") {
		new_list.push(listOfFilms.sort());
	}
	return new_list;
};

function Rechercher () {
	let input = document.getElementById("recherche").value.toLowerCase();
	let listOfFilms = ListOfFilms_search();
	ListOfFilms_Recherche(listOfFilms, input);
}

function Trier() {
	let listOfFilms = ListOfFilms();
	let select = document.getElementById("trier");
	let selectOption = select.options[select.selectedIndex].value;
	ListOfFilms_Trier(listOfFilms, selectOption);
}

function addFilmForm(){
	document.querySelectorAll("div.infoAddBlock");
}

window.onload = function() {
	ACTUAL_USER_ID=document.querySelector(".conteneur_deco > form > label").getAttribute("id").substring(7);


	if(window.location.pathname==="/accueil" || window.location.pathname==="/" || window.location.pathname==="/user/favoris"){
		listFilmJSON();
		ListOfFilms();
		addFilmForm();
	}

	if(window.location.pathname==="/film"){
		let filmId = parseInt(document.getElementsByClassName('filmDetail')[0].getAttribute('id'))
		if(ACTUAL_USER_ID !== "0"){
			getFilmInfo(filmId);
		}
	}
	if(window.location.pathname==="/admin/gestionuser"){
		listUsers();
	}
};

//---- PAGE DETAIL FILM AJAX + WS ----//

let getFilmInfo = function (filmId){
	let infoFilmRequest = new XMLHttpRequest();
	let url = "../ws/"+ACTUAL_USER_ID+"/filmdetail/"+filmId;
	infoFilmRequest.open("GET", url, true);
	infoFilmRequest.responseType = "json";

	infoFilmRequest.onload = function () {
		let filmDetail = this.response;
		remplirFilmIcone(filmDetail);
	};
	infoFilmRequest.send();
}

let remplirFilmIcone = function (filmDetail){
	let iconesBloc=document.getElementById("bloc_icones");
	let pourcentageValue=iconesBloc.querySelector("#pourcentage_film");
	let favori=iconesBloc.querySelector("#favori_film");
	let like=iconesBloc.querySelector("#like_film");
	let dislike=iconesBloc.querySelector("#dislike_film");
	if (filmDetail.favori === true) {
		favori.style.color="red";
		favori.style.opacity=0.8;
		favori.onclick = function (){
			removeAvis(filmDetail.id,"favori");
		}
	} else {
		favori.style.color="grey";
		favori.onclick = function (){
			addAvis(filmDetail.id,"favori");
		}
	}
	if (filmDetail.avis === "like") {
		like.style.color = "green";
		dislike.style.color = "grey";
		like.onclick = function (){
			removeAvis(filmDetail.id,"like");
		}
		dislike.onclick = function (){
			addAvis(filmDetail.id,"dislike");
		}
	} else if (filmDetail.avis === "dislike") {
		like.style.color = "grey";
		dislike.style.color = "red";
		like.onclick = function (){
			addAvis(filmDetail.id,"like");
		}
		dislike.onclick = function(){
			removeAvis(filmDetail.id,"dislike");
		}
	} else {
		like.style.color = "grey";
		dislike.style.color = "grey";
		like.onclick = function (){
			addAvis(filmDetail.id,"like");
		}
		dislike.onclick = function (){
			addAvis(filmDetail.id,"dislike");
		}
	}
	pourcentageValue.innerText = filmDetail.pourcentage + " %";
}

let removeAvis = function (filmId,action){
	let removeAvisRequest = new XMLHttpRequest();
	let url = "../ws/"+ACTUAL_USER_ID+"/filmdetail/"+filmId+"/remove/"+action;
	removeAvisRequest.open("PATCH", url, true);
	removeAvisRequest.onload = function () {
		if(this.status===409){
			alert("Vous \351ssayez d'effectuer une action in\350xistante");
		}
		else{
			getFilmInfo(filmId);
		}
	};
	removeAvisRequest.send();
}
let addAvis = function (filmId,action){
	let addAvisRequest = new XMLHttpRequest();
	let url = "../ws/"+ACTUAL_USER_ID+"/filmdetail/"+filmId+"/add/"+action;
	addAvisRequest.open("PATCH", url, true);
	addAvisRequest.onload = function () {
		if(this.status===409){
			alert("Vous essayez d'effectuer une action in\350xistante");
		}
		else{
			getFilmInfo(filmId);
		}
	};
	addAvisRequest.send();
}

//----  GESTION DES UTILISATEURS EN UTILISANT LES WEB SERVICES A PARTIR DE REQUETES AJAX----//
let listUsers = function () {
	let usersRequest = new XMLHttpRequest();
	let url = "../ws/admin/gestionuser/listuser";
	usersRequest.open("GET", url, true);
	usersRequest.responseType = "json";

	usersRequest.onload = function () {
		let users = this.response;
		refreshTable(users);
	};
	usersRequest.send();
};

let changeRole = function (user){
	let changeRoleRequest = new XMLHttpRequest();
	let url = "../ws/admin/gestionuser/"+user.id+"/change";
	changeRoleRequest.open("PATCH", url, true);
	changeRoleRequest.onload = function () {
		if(this.status===409){
			alert("Vous essayez de modifier le r\364le d'un utilisateur in\351xistant");
		}
		else{
			listUsers();
		}
	};
	changeRoleRequest.send();
};

let modifUser = function (user){
	let modifUserElement= document.getElementById("bloc_modif_utilisateur");
	modifUserElement.querySelector("h3").innerText = "Modifier l'utilisateur "+user.prenom +" "+ user.nom;
	let modif_form=modifUserElement.querySelectorAll("#modif_utilisateur > input");
	modif_form[0].value = user.email;
	modif_form[1].value = user.nom;
	modif_form[2].value = user.prenom;
	modif_form[3].value = "";
	modifUserElement.hidden =false;
	document.getElementById("modif_send").onclick = function () {
		validModifUser(user);
	};
}

let validModifUser = function (user){
	let new_email=document.getElementById("modif_mail").value.toLowerCase();
	let new_name=document.getElementById("modif_prenom").value.toLowerCase();
	new_name = new_name.charAt(0).toLocaleUpperCase() + new_name.substring(1);
	let new_surname=document.getElementById("modif_nom").value.toLowerCase();
	new_surname = new_surname.charAt(0).toLocaleUpperCase() + new_surname.substring(1);
	let new_password=document.getElementById("modif_mdp").value;
	let previous_password=document.getElementById("previous_mdp").value;
	if (verifEntry(new_email,new_name,new_surname,new_password,previous_password)){
		if(confirm('Etes vous sur de vouloir modifier cet utilisateur ?')) {
			let modifUserRequest = new XMLHttpRequest();
			let url = "../ws/admin/gestionuser/"+user.id+"/modif";
			modifUserRequest.open("PATCH", url, true);
			modifUserRequest.onload = function () {
				if(this.status===409){
					alert("Vous ne pouvez pas modifier un utilisateur in\351xistant");
				}
				else if(this.status===405){
					alert("Le Mot de Passe actuel saisi n'est pas correct");
				} else if (this.status===404) {
					alert("Vous ne pouvez pas modifier un utilisateur par une adresse mail d\351j\340 \351xistante");
				} else{
					document.getElementById("bloc_modif_utilisateur").hidden=true;
					listUsers();
				}
			};
			modifUserRequest.setRequestHeader("content-type", "application/x-www-form-urlencoded");
			modifUserRequest.send("new_email="+new_email+"&new_name="+new_name+"&new_surname="+new_surname+"&new_password="+new_password+"&previous_password="+previous_password);
		}
		else{
			document.getElementById("bloc_modif_utilisateur").hidden = true;
			return false;}
	}
}

let deleteUser = function (user){
	let deleteUserRequest= new XMLHttpRequest();
	let url = "../ws/admin/gestionuser/"+user.id;
	deleteUserRequest.open("DELETE",url,true);

	deleteUserRequest.onload = function (){
		if(this.status===409){
			alert("Vous essayez de supprimer un utilisateur in\350xistant");
		}
		else {
			listUsers()
		}
	}
	deleteUserRequest.send();
}

let verifyAjoutForm = function () {
	let email = document.getElementById("ajout_mail").value.toLowerCase();
	let name = document.getElementById("ajout_prenom").value.toLowerCase();
	name = name.charAt(0).toLocaleUpperCase() + name.substring(1);
	let surname = document.getElementById("ajout_nom").value.toLowerCase();
	surname = surname = surname.charAt(0).toLocaleUpperCase() + surname.substring(1);
	let password = document.getElementById("ajout_mdp").value;

	if (verifEntry(email,name,surname,password)) {
		if (confirm('Etes vous sur de vouloir ajouter cet utilisateur ?')) {
			let ajoutUserRequest = new XMLHttpRequest();
			let url = "../ws/admin/gestionuser";
			ajoutUserRequest.open("POST", url, true);
			ajoutUserRequest.onload = function () {
				if(this.status== 409){
					alert("Vous ne pouvez pas ajouter deux utilisateurs avec la m\352me adresse e-mail.")
				}
				else{
					listUsers();
				}
			};
			ajoutUserRequest.setRequestHeader("content-type", "application/x-www-form-urlencoded");
			ajoutUserRequest.send("email=" + email + "&name=" + name + "&surname=" + surname + "&password=" + password);
		} else {
			return false;
		}

	}
}

let verifEntry = function (email,name,surname,password){
	let booleanVerif = true;
	// A compl??ter pour v??rifier les champs du formulaire.
	if (/^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/.test(email)) {
	} else {
		booleanVerif = false;
		alert("Vous avez rentr\351 une mauvaise adresse email.");
	}
	if (password.length >0 && password.length < 7) {
		booleanVerif = false;
		alert("Votre mot de passe doit contenir au minimum 7 caract\350res.")
	}
	if (name.length ===0) {
		booleanVerif = false;
		alert("Vous n'avez pas rentr\351 de pr\351nom.")
	}
	if (surname.length ===0) {
		booleanVerif = false;
		alert("Vous n'avez pas rentr\351 de nom de famille.")
	}
	return booleanVerif;
}

//-----------Cr??ation du tableau des utilisateurs--------------
let refreshTable = function (users) {
	document.getElementById("bloc_modif_utilisateur").hidden=true;
	document.getElementById("ajout_utilisateur").hidden=true;
	document.getElementById("ajout_mail").value = "";
	document.getElementById("ajout_nom").value = "";
	document.getElementById("ajout_prenom").value = "";
	document.getElementById("ajout_mdp").value = "";
	document.getElementById("ajout_user_send").onclick = function (){
		verifyAjoutForm()
	}
	document.querySelector(".conteneur-genre")
	let tableElement = document.querySelector(".tablelist tbody");
	var newTableElement = tableElement.cloneNode(false);
	let compteur = 0;
	for (const user of users) {
		compteur ++;
		newTableElement.appendChild(buildUserTableLine(user,compteur));
	}
	tableElement.parentNode.replaceChild(newTableElement, tableElement);
};

let buildUserTableLine = function (user,compteur) {
	let lineElement = document.createElement("tr");
	lineElement.appendChild(createTableCell(compteur));
	lineElement.appendChild(createTableCell(user.prenom));
	lineElement.appendChild(createTableCell(user.nom, true));
	lineElement.appendChild(createTableCell(user.email));
	if(user.admin===true) {
		lineElement.appendChild(createTableCell("Oui"));
	}
	else{
		lineElement.appendChild(createTableCell("Non"));
	}

	let actionCell = document.createElement("td");
	actionCell.classList.add("small_column");
	let buttonGroupElement = document.createElement("div");
	buttonGroupElement.classList.add("column-gestion");
	actionCell.appendChild(buttonGroupElement);
	if(user.id.toString() !== ACTUAL_USER_ID){
		let imageValue;
		let new_role;
		if(user.admin===true) {
			imageValue = "down_user";
			new_role = "R\351trograder cet utilisateur";
		}
		else {
			imageValue = "up";
			new_role = "Promouvoir Admin";
		}
		let setRoleButton = document.createElement("button");
		let image=document.createElement("img");
		image.setAttribute("src","../images/icones/"+imageValue+".png");
		image.setAttribute("class","img_liste");
		setRoleButton.appendChild(image);
		setRoleButton.classList.add("btn-delete");
		setRoleButton.title = new_role;
		setRoleButton.onclick = function () {
			if(confirm('Etes vous sur de vouloir changer le r\364le de cet utilisateur ?')) {
				changeRole(user);
			}
			else{ return false;}
		};
		buttonGroupElement.appendChild(setRoleButton);

		let updateButton = document.createElement("button");
		let imageUpdate=document.createElement("img");
		imageUpdate.setAttribute("src","../images/icones/modification.png");
		imageUpdate.setAttribute("class","img_liste");
		updateButton.appendChild(imageUpdate);
		updateButton.classList.add("btn-delete");
		updateButton.title = "Modifier cet utilisateur";
		updateButton.onclick = function () {
			modifUser(user);
		};
		buttonGroupElement.appendChild(updateButton);

		let deleteButton = document.createElement("button");
		let imageDelete=document.createElement("img");
		imageDelete.setAttribute("src","../images/icones/supprimer.png");
		imageDelete.setAttribute("class","img_liste");
		deleteButton.appendChild(imageDelete);
		deleteButton.classList.add("btn-delete");
		deleteButton.title = "Supprimer cet utilisateur";
		deleteButton.onclick = function () {
			if(confirm('Etes vous sur de vouloir supprimer '+user.prenom +' '+user.nom+' ?')) {
				deleteUser(user);
			}
			else{ return false;}
		};
		buttonGroupElement.appendChild(deleteButton);
	}
	else{
		let updateButton = document.createElement("button");
		let imageUpdate=document.createElement("img");
		imageUpdate.setAttribute("src","../images/icones/modification.png");
		imageUpdate.setAttribute("class","img_liste");
		updateButton.appendChild(imageUpdate);
		updateButton.classList.add("btn-delete");
		updateButton.title = "Modifier cet utilisateur";
		updateButton.onclick = function () {
			modifUser(user);
		};
		buttonGroupElement.appendChild(updateButton);
		lineElement.style.backgroundColor = "yellow";
		lineElement.style.fontWeight = "bold";
	}
	lineElement.appendChild(actionCell);
	if (user.id == ACTUAL_USER_ID) {
		let nomUser = document.getElementById("userCo="+ACTUAL_USER_ID);
		nomUser.innerText = user.prenom + " " + user.nom;
	}
	return lineElement;
};

let createTableCell = function (text) {
	let cellElement;
	cellElement = document.createElement("td");
	cellElement.innerText = text;
	return cellElement;
};