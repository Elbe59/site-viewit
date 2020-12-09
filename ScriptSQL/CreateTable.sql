#--- Drop Table si elles existent

DROP TABLE IF EXISTS preferer;
DROP TABLE IF EXISTS film;
DROP TABLE IF EXISTS genre;
DROP TABLE IF EXISTS utilisateur;

#--- Création des tables

#--- admin = 0 : Utilisateur, admin = 1 : Administrateur
CREATE TABLE utilisateur(
	idUtilisateur     Int(5)  Auto_increment  NOT NULL ,
	prenomUtilisateur Varchar (50) NOT NULL ,
    nomUtilisateur    Varchar (50) NOT NULL ,
    email             Varchar (266) NOT NULL ,
    mdpHash           Varchar (1000) NOT NULL ,
    admin             Int(1) NOT NULL,
	CONSTRAINT UTILISATEUR_PK PRIMARY KEY (idUtilisateur)
);

CREATE TABLE genre(
    idGenre  Int(2)  Auto_increment  NOT NULL ,
    nomGenre Varchar (50) NOT NULL,
	CONSTRAINT GENRE_PK PRIMARY KEY (idGenre)
);

#--- valide = 0 : Non affiché, valide = 1 : Affiche
CREATE TABLE film(
    idFilm      Int(5)  Auto_increment  NOT NULL ,
    titreFilm   Varchar (100) NOT NULL ,
    resumeFilm  Varchar (1500) NOT NULL ,
    dateSortie  Date NOT NULL ,
	dureeFilm   Int(3) NOT NULL,
	realisateur Varchar(100) NOT NULL,
	acteur      Varchar(100),
    imgFilm     Varchar(100),
    urlBA       Varchar (100),
	idGenre     Int(2) NOT NULL,
	valide		Int(1) NOT NULL,
	CONSTRAINT FILM_PK PRIMARY KEY (idFilm),
	
	CONSTRAINT FILM_GENRE_FK FOREIGN KEY (idGenre) REFERENCES GENRE(idGenre)
);

#--- liker = 0 : défaut, liker = 1 : j'aime, liker = -1 : j'aime pas
#--- favoris = 0 : non défaut; favoris = 1 : oui
CREATE TABLE preferer (
    idFilm        Int(5) NOT NULL ,
    idUtilisateur Int(5) NOT NULL,
	liker		  Int(1) NOT NULL,
	favoris		  Int(1) NOT NULL,
	CONSTRAINT Preferer_PK PRIMARY KEY (idFilm,idUtilisateur),

	CONSTRAINT Preferer_FILM_FK FOREIGN KEY (idFilm) REFERENCES FILM(idFilm) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT Preferer_UTILISATEUR_FK FOREIGN KEY (idUtilisateur) REFERENCES UTILISATEUR(idUtilisateur) ON DELETE CASCADE ON UPDATE CASCADE
);
