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
				if (jsonFile[j].favori === true) {
					listOfFilms[i][2].querySelector("input").name = "suppfavori";
					listOfFilms[i][2].querySelector("svg").style.color = "red";
					listOfFilms[i][2].querySelector("svg").style.opacity = 1;
				} else {
					listOfFilms[i][2].querySelector("input").name = "addfavori";
				}
				if (listOfFilms[i][3] != null && listOfFilms[i][4] != null) {
					if (jsonFile[j].avis === "like") {
						listOfFilms[i][3].querySelector("input").name = "remove";
						listOfFilms[i][3].querySelector("svg").style.color = "green";
						listOfFilms[i][4].querySelector("input").name = "adddislike";
						listOfFilms[i][4].querySelector("svg").style.color = "grey";
					} else if (jsonFile[j].avis === "dislike") {
						listOfFilms[i][3].querySelector("input").name = "addlike";
						listOfFilms[i][3].querySelector("svg").style.color = "grey";
						listOfFilms[i][4].querySelector("input").name = "remove";
						listOfFilms[i][4].querySelector("svg").style.color = "red";
					} else {
						listOfFilms[i][3].querySelector("input").name = "addlike";
						listOfFilms[i][3].querySelector("svg").style.color = "grey";
						listOfFilms[i][4].querySelector("input").name = "adddislike";
						listOfFilms[i][4].querySelector("svg").style.color = "grey";
					}
				}
			}
		}
	}
}

function ListOfFilms () {
	let doc = document.getElementsByTagName("article");
	let list = [];
	for (i=0;i<doc.length;i++) {
		list.push([doc[i]["title"],doc[i]["id"],doc[i],null,null]);
	}
	if (list.length == 0) {
		doc = document.getElementsByClassName("filmDetail")[0];
		list.push([doc.title,doc.id,doc.getElementsByClassName("favori")[0],doc.getElementsByClassName("like")[0],doc.getElementsByClassName("dislike")[0]]);
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

function addFilmForm(){
	let form=document.querySelectorAll("div.infoAddBlock");
	console.log(form);

}

window.onload = function() {
	ACTUAL_USER_ID=document.querySelector(".conteneur_deco > form > label").getAttribute("id").substring(7);


	if(window.location.pathname==="/accueil" || window.location.pathname==="/" || window.location.pathname==="/user/favoris" || window.location.pathname==="/film"){
		listFilmJSON();
		let listOfFilms = ListOfFilms();
		addFilmForm();
		console.log(listOfFilms);
	}

	/*let element=document.getElementById("header_gestion_user");
	element.addEventListener("click",function(){
		console.log("cc");
		listUsers();
	});*/
	if(window.location.pathname==="/admin/gestionuser"){
		listUsers();
	}
};

let listUsers = function () {

	let usersRequest = new XMLHttpRequest();
	let url = "../ws/admin/gestionuser/listuser";
	usersRequest.open("GET", url, true);
	usersRequest.responseType = "json";

	usersRequest.onload = function () {
		let users = this.response;
		console.log(users)
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
			alert("Vous essayez de modifier le role d'un utilisateur inexistant");
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
	let new_email=document.getElementById("modif_mail").value;
	let new_name=document.getElementById("modif_prenom").value;
	new_name = new_name.charAt(0).toLocaleUpperCase() + new_name.substring(1);
	let new_surname=document.getElementById("modif_nom").value;
	new_surname = new_surname.charAt(0).toLocaleUpperCase() + new_surname.substring(1);
	let new_password=document.getElementById("modif_mdp").value;
	// A compléter pour vérifier les champs du formulaire.
	let booleanVerif = true;
	// A compléter pour vérifier les champs du formulaire.
	if (/^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/.test(new_email)) {

		/*let error = document.createElement("p");
		error.value = "Vous avez rentré une mauvaise adresse email."
		error.style.color = "red";
		email.appendChild(error)*/
	} else {
		booleanVerif = false;
		alert("Vous avez rentré une mauvaise adresse email.");
	}
	if (new_password.length < 7) {
		booleanVerif = false;
		alert("Votre mot de passe doit contenir au minimum 7 caractères.")
	}
	if (new_name.length ===0) {
		booleanVerif = false;
		alert("Vous n'avez pas rentré de prénom.")
	}
	if (new_surname.length ===0) {
		booleanVerif = false;
		alert("Vous n'avez pas rentré de nom de famille.")
	}
	if (booleanVerif) {
		if(confirm('Etes vous sur de vouloir modifier cet utilisateur ?')) {
			let modifUserRequest = new XMLHttpRequest();
			let url = "../ws/admin/gestionuser/"+user.id+"/modif";
			modifUserRequest.open("PATCH", url, true);
			modifUserRequest.onload = function () {
				if(this.status===409){
					alert("Vous essayez de modifier un utilisateur inexistant");
				}
				else{
					document.getElementById("bloc_modif_utilisateur").hidden=true;
					listUsers();
				}
			};
			modifUserRequest.setRequestHeader("content-type", "application/x-www-form-urlencoded");
			modifUserRequest.send("new_email="+new_email+"&new_name="+new_name+"&new_surname="+new_surname+"&new_password="+new_password);
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
			alert("Vous essayez de supprimer un utilisateur inexistant");
		}
		else {
			listUsers()
		}
	}
	deleteUserRequest.send();
}

let verifyAjoutForm = function (user) {
	let email = document.getElementById("ajout_mail").value.toLowerCase();
	let name = document.getElementById("ajout_prenom").value.toLowerCase();
	name = name.charAt(0).toLocaleUpperCase() + name.substring(1);
	let surname = document.getElementById("ajout_nom").value.toLowerCase();
	surname = surname = surname.charAt(0).toLocaleUpperCase() + surname.substring(1);
	let password = document.getElementById("ajout_mdp").value;

	let booleanVerif = true;
	// A compléter pour vérifier les champs du formulaire.
	if (/^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/.test(email)) {

		/*let error = document.createElement("p");
		error.value = "Vous avez rentré une mauvaise adresse email."
		error.style.color = "red";
		email.appendChild(error)*/
	} else {
		booleanVerif = false;
		alert("Vous avez rentré une mauvaise adresse email.");
	}
	if (password.length < 7) {
		booleanVerif = false;
		alert("Votre mot de passe doit contenir au minimum 7 caractères.")
	}
	if (name.length ===0) {
		booleanVerif = false;
		alert("Vous n'avez pas rentré de prénom.")
	}
	if (surname.length ===0) {
		booleanVerif = false;
		alert("Vous n'avez pas rentré de nom de famille.")
	}
	if (booleanVerif) {
		if (confirm('Etes vous sur de vouloir ajouter cet utilisateur ?')) {
			let ajoutUserRequest = new XMLHttpRequest();
			let url = "../ws/admin/gestionuser";
			ajoutUserRequest.open("POST", url, true);
			ajoutUserRequest.onload = function () {
				if(this.status== 409){
					alert("Vous ne pouvez pas ajouter deux utilisateur avec la même adresse e-mail.")
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
//-----------Création du tableau des utilisateurs--------------
let refreshTable = function (users) {
	document.getElementById("ajout_mail").value = "";
	document.getElementById("ajout_nom").value = "";
	document.getElementById("ajout_prenom").value = "";
	document.getElementById("ajout_mdp").value = "";
	document.getElementById("ajout_user_send").onclick = function (){
		verifyAjoutForm()
	}
	let modifUserElement= document.querySelector(".conteneur-genre")
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
			new_role = "Rétrograder utilisateur";
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
			if(confirm('Etes vous sur de vouloir changer le rôle de cet utilisateur ?')) {
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
	return lineElement;
};

let createTableCell = function (text) {
	let cellElement;
	cellElement = document.createElement("td");
	cellElement.innerText = text;
	return cellElement;
};