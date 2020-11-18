
#--- Drop Table si elles existent

DROP TABLE IF EXISTS Preferer;
DROP TABLE IF EXISTS COMMENTAIRE;
DROP TABLE IF EXISTS FILM;
DROP TABLE IF EXISTS GENRE;
DROP TABLE IF EXISTS UTILISATEUR;

#--- Création des tables

#--- admin = 0 : Utilisateur, admin = 1 : Administrateur
CREATE TABLE UTILISATEUR(
	idUtilisateur     Int(5)  Auto_increment  NOT NULL ,
	prenomUtilisateur Varchar (50) NOT NULL ,
    nomUtilisateur    Varchar (50) NOT NULL ,
    email             Varchar (50) NOT NULL ,
    mdp               Varchar (50) NOT NULL ,
    admin             Int(1) NOT NULL,
	CONSTRAINT UTILISATEUR_PK PRIMARY KEY (idUtilisateur)
);

CREATE TABLE GENRE(
    idGenre  Int(2)  Auto_increment  NOT NULL ,
    nomGenre Varchar (50) NOT NULL,
	CONSTRAINT GENRE_PK PRIMARY KEY (idGenre)
);

#--- valide = 0 : Non affiché, valide = 1 : Affiche
CREATE TABLE FILM(
    idFilm      Int(5)  Auto_increment  NOT NULL ,
    titreFilm   Varchar (50) NOT NULL ,
    resumeFilm  Varchar (500) NOT NULL ,
    dateSortie  Date NOT NULL ,
	dureeFilm   Int(3) NOT NULL,
	realisateur Varchar(100) NOT NULL,
	acteur      Varchar(100),
    imgFilm     Varchar(100) NOT NULL,
    urlBA       Varchar (100) NOT NULL,
	idGenre     Int(2) NOT NULL,
	valide		Int(1) NOT NULL,
	CONSTRAINT FILM_PK PRIMARY KEY (idFilm),
	
	CONSTRAINT FILM_GENRE_FK FOREIGN KEY (idGenre) REFERENCES GENRE(idGenre)
);

CREATE TABLE COMMENTAIRE(
	idCommentaire      Int(5) Auto_increment NOT NULL,
    dateCommentaire    Date NOT NULL ,
    contenuCommentaire Varchar (300) NOT NULL,
	idFilm             Int(5) NOT NULL ,
    idUtilisateur      Int(5) NOT NULL ,
	CONSTRAINT COMMENTAIRE_PK PRIMARY KEY (idCommentaire),
	
	CONSTRAINT COMMENTAIRE_FILM_FK FOREIGN KEY (idFilm) REFERENCES FILM(idFilm) DELETE ON CASCADE,
	CONSTRAINT COMMENTAIRE_UTILISATEUR_FK FOREIGN KEY (idUtilisateur) REFERENCES UTILISATEUR(idUtilisateur)
);

#--- liker = 0 : défaut, liker = 1 : j'aime, liker = 2 : j'aime pas
#--- favoris = 0 : non défaut; favoris = 1 : oui
CREATE TABLE Preferer(
    idFilm        Int(5) NOT NULL ,
    idUtilisateur Int(5) NOT NULL,
	liker		  Int(1) NOT NULL,
	favoris		  Int(1) NOT NULL,
	CONSTRAINT Preferer_PK PRIMARY KEY (idFilm,idUtilisateur),

	CONSTRAINT Preferer_FILM_FK FOREIGN KEY (idFilm) REFERENCES FILM(idFilm),
	CONSTRAINT Preferer_UTILISATEUR_FK FOREIGN KEY (idUtilisateur) REFERENCES UTILISATEUR(idUtilisateur)
);

#--- Création des insert

INSERT INTO UTILISATEUR (idUtilisateur, prenomUtilisateur, nomUtilisateur, email, mdp, admin) VALUES
(1,'Defaut', 'Defaut','defaut.defaut@gmail.com','123',0),
(2,'Jack', 'Barzone','jack.barzone@gmail.com','1234',1),
(3,'Denis', 'Cauchois','denis.cauchois@yncrea.fr','DenisDu59',1),
(4,'Yann', 'Riquier','yann.riquier@externe.junia.com','Yann1234',0),
(5,'Fabien', 'Turpin','fab.turpin@gmail.com','motdepasse',0),
(6,'Jean', 'Dupont','jean.dupont@gmail.com','JPdu59',0);

INSERT INTO GENRE (idGenre, nomGenre) VALUES
(1, "Roman"),
(2, "Bande dessinée"),
(3, "Comics"),
(4, "Manga"),
(5, "Nouvelles"),
(6, "Science-Fiction"),
(7, "Fantastique"),
(8, "Fantasy"),
(9, "Aventure"),
(10, "Policier"),
(11, "Théâtre"),
(12, "Humour"),
(13, "Philosophie"),
(14, "Super-Héros"),
(15, "Horreur"),
(16, "Historique"),
(17, "Super-Héros");

INSERT INTO FILM (idFilm, titreFilm, resumeFilm, dateSortie, dureeFilm, realisateur, acteur, imgFilm, urlBA, idGenre, valide) VALUES
(1, "Captain America: First Avenger", "Captain America: First Avenger nous plonge dans les premières années de l'univers Mavel. Steve Rogers,  frête et timide participe à un programme expérimental le transformant en Super Soldat, plus connu sous le nom de Captain America. Il sera confronté à la diabolique organisation HYDRA dirigée par le redoutable Red Skull.", "2011-08-17", 124, "Joe Johnston", "Chris Evans", "captainAmericaFirstAvenger.jpg", "IsiV9IJieMk", 17, 1),
(2, "Iron Man", "Tony Stark, playboy, milliardaire, n'est pas seulement l'héritier des usines d'armement de son père, c'est également un inventeur de génie. Alors qu'il est en déplacement en Afghanistan pour présenter sa dernière création, il est enlevé par des terroristes. Traumatisé par cette expérience, il décida de créer une armure de combat unique pour combattre le mal.", "2008-04-30", 126, "Jon Favreau", "Robert Downey Jr.", "ironMan.jpg", "Q2lEqf_F6Pk", 17, 1),
(3, "Iron Man 2", "Le monde sait désormais que l'inventeur milliardaire Tony Stark et le super-héros Iron Man ne font qu'un. Cependant, malgré les pressions, Tony n'est pas disposé à divulguer les secrets de son armure, redoutant que l'information atterrisse dans de mauvaises mains. Avec Pepper Potts et James Rhodey Rhodes à ses côtés, Tony va forger de nouvelles alliances et affronter de nouvelles forces toutes-puissantes.", "2010-04-28", 126, "Jon Favreau", "Robert Downey Jr.", "ironMan2.jpg", "VdZj2QYTAic", 17, 1),
(4, "Thor", "Au royaume d'Asgard, Thor est un guerrier aussi puissant qu'arrogant. Alors que ses actes téméraires, qui ont déclenché une guerre ancestrale, l'ont fait bannir sur Terre par Odin, son père, il est condamné à vivre parmi les hommes. Cependant, lorsque les forces du mal, en provenance d'Asgard, s'apprêtent à se déchaîner sur la Terre, Thor va devoir se comporter en véritable héros.", "2011-04-27", 114, "Kenneth Branagh", "Chris Hemsworth", "thor.jpg", "MeN3Gwf2sDg", 17, 0),
(5, "L'Incroyable Hulk", "Le scientifique Bruce Banner cherche désespérément un antidote aux radiations qui ont créé Hulk. Il vit dans l'ombre et parcourt la planète à la recherche d'un remède. La force destructrice de Hulk attire le Général Thaddeus E. Ross et son bras droit Blonsky qui rêvent de l'utiliser à des fins militaires. Ils tentent de développer un sérum pour créer des soldats surpuissants.", "2008-07-23", 114, "Louis Leterrier", "Edward Norton", "theIncredibleHulk.jpg", "LCjTxK1B5Pg", 6, 1),
(6, "Ça : Chapitre 2", "Tous les 27 ans, une créature maléfique revient hanter les rues de Derry, dans le Maine. Près de trente ans après les événements du premier opus, les membres du Club des Ratés, désormais adultes, se retrouvent.", "2019-08-26", 169, "Andrés Muschietti", "Bill Hader", "ca.jpg", "G8fR1vvrLmI", 15, 1);

INSERT INTO COMMENTAIRE (idCommentaire, dateCommentaire, contenuCommentaire, idFilm, idUtilisateur) VALUES
(1, "2020-11-10", "Je ne comprend pas à quel moment cela peut exister, un géant vert en mousse.", 5, 6),
(2, "2020-11-10", "C'est peut-être le principe même d'un film de science-fiction/super-héro non ?", 5, 5),
(3, "2020-11-12", "Moi aussi j'aimerai participer à cette expérience :/", 1, 2);

INSERT INTO Preferer (idFilm, idUtilisateur, liker, favoris) VALUES
(5, 6, 2, 0),
(1, 2, 1, 1);