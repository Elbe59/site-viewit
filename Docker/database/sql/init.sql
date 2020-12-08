#--- Drop Table si elles existent

DROP TABLE IF EXISTS Preferer;
DROP TABLE IF EXISTS FILM;
DROP TABLE IF EXISTS GENRE;
DROP TABLE IF EXISTS UTILISATEUR;

#--- Création des tables

#--- admin = 0 : Utilisateur, admin = 1 : Administrateur
CREATE TABLE UTILISATEUR(
	idUtilisateur     Int(5)  Auto_increment  NOT NULL ,
	prenomUtilisateur Varchar (50) NOT NULL ,
    nomUtilisateur    Varchar (50) NOT NULL ,
    email             Varchar (266) NOT NULL ,
    mdpHash           Varchar (1000) NOT NULL ,
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
    titreFilm   Varchar (100) NOT NULL ,
    resumeFilm  Varchar (1000) NOT NULL ,
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

#--- liker = 0 : défaut, liker = 1 : j'aime, liker = 2 : j'aime pas
#--- favoris = 0 : non défaut; favoris = 1 : oui
CREATE TABLE PREFERER (
    idFilm        Int(5) NOT NULL ,
    idUtilisateur Int(5) NOT NULL,
	liker		  Int(1) NOT NULL,
	favoris		  Int(1) NOT NULL,
	CONSTRAINT Preferer_PK PRIMARY KEY (idFilm,idUtilisateur),

	CONSTRAINT Preferer_FILM_FK FOREIGN KEY (idFilm) REFERENCES FILM(idFilm) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT Preferer_UTILISATEUR_FK FOREIGN KEY (idUtilisateur) REFERENCES UTILISATEUR(idUtilisateur) ON DELETE CASCADE ON UPDATE CASCADE
);

#--- Création des insert

INSERT INTO UTILISATEUR (idUtilisateur, prenomUtilisateur, nomUtilisateur, email, mdpHash, admin) VALUES
(1,"Defaut", "Defaut","defaut.defaut@gmail.com","$argon2i$v=19$m=65536,t=5,p=1$zFnaINNvYeCrC75OYuuZEl9al5weOMnSXcOUoIWhUdIMRbNvXF1ipU5aMaU0HVXtsotzpepy/LxIHtd7SJMgFpk7T4T6eE24y3CxyiuG1woN5vMrPCnl4ldjtAmWQ/iEsL0JRXuthPrbFO1GkA+k4D2s7E9SNF9JA8sJaSHURU8$U5xfj0Qz7+T3sr05PxuUhEgAKU2+WxhcrFMUUS2yVGi2egf4rSsxZ9FSXYliBnx03aXgNEvtPrZ7zWq2TQdw9LA+gWS4+IOrKkR56EkVZDgD59jkDVE5P59jmqSIVpQq51jx8SkCITxgOaS8xMGuDaUtpBIce1UfxGGEgGi8pRg",0),
(2,"Jack", "Barzone","jack.barzone@gmail.com","$argon2i$v=19$m=65536,t=5,p=1$dTSkKiptldBfurDMM/Ez+1xdd8kAbGOk6INnM4YxG8bBVkywlRCjWB7+qrQjj9Ifb1axUqWswQHEX8zm8jKcg1SFi+WCb1ZHTFea6TtkNnsTva+sOT6SshKfiCY7HOLAvUUmDsX+W8mxUtDhOdvHfYU96dkgy6H2TM5CkP73Im0$q0xJDLk0mJcq+JodHjncyxtqOk+2KPr9AQ1womwdmYajdeoVuRaS/IrDoKVlwxYBL6M5BzNAUk7sDudKtEzSwKcsjeBcJEkKWoIZIsS+yo59tJ7vwd0PsgWLqWt1M49UG0sT1dS/jVAUIp6ANclZkTtgq5kJrNncR6SCSPyjLk8",1),
(3,"Denis", "Cauchois","denis.cauchois@yncrea.fr","$argon2i$v=19$m=65536,t=5,p=1$WPUiir9tiipIBdnm7yTVpZu11Z14DGoBPgQ/8kVJpO+FozRh+LcwA5UchCcpPOxHjzPxRs5FOSSS+1OTrGPyiYA65tq8W5sOldJMVLvCCDIGEqnHuz1vhoAZHzbcfrH3uK9W3SHFiWZAONF9+v/N2zrxLgtd7DBllOK3N5viuEU$Y6/QAaEt/u9cO7uSd7wL25hCTRG8VVLnk+SrTTszEteHh5Soa9t4cz0Bs2zl3DhTNNDfAq18uheiLNu0hZlORuPmEnbbDRo1vF+2pHiD+OhVBaHeouhEY/IypHqa3oOKXdbtxRqHq4bwPIp4+QhM0jcU+xSXX88T9R9kO11bYJA",1),
(4,"Yann", "Riquier","yann.riquier@externe.junia.com","$argon2i$v=19$m=65536,t=5,p=1$+Q2ncq4NWBznggWM4VNPDfc0+AB5qT2RXBtkcauZ0OhkdtTCHJ/H4ASooZtoOxSKj0p32oXZcBHkpEYt8e2FNU343WZY4TfnTi5Dy6LtZ2MSPCevEEeCb1WseVQ7NYs+u1H2hf54HRCBSJ0Jg7Zz1dJn7o2nuI0UJwPbJ6mmkHg$KVtSpZ2ceqrJ13Kig6MvyMTpf/Hzvc/fo4cSGYATmG42H2ukYU8Tw5u9Ro7WQEOwFguW3Nb8mi6W2qzZIieU8pNaM8kIC3G19ckCIHlcwoBLoDoF6mSEeiO9yZMAp73AZvLnVXvpGTgCJtZuEpcMVBG5QQXQ1r5ak9d3c5W/TqQ",0),
(5,"Fabien", "Turpin","fab.turpin@gmail.com","$argon2i$v=19$m=65536,t=5,p=1$jWTIoFAnUmy7rl4vW96liLXkGT8Y/CqS9OcFNUP7fBMeYQLhgvR3nM101cCtlnlzlm7036NPPkzHBzGrwAIapzyGTQu+JsIqdpvAZGKEApfMplP2Kk5BlM37DoYpZ7lGi/dYI41QPDoHT19ptO1eKP7JJdRWLDmqL2fNr1kjwZ4$ykaOBReDqgQoNpsdvlc2zxQGfHykaSC6Jkamm4Tr6oanUIQdevqHXcXKwnAYvqaW8n3h+H8A0H+lLzC9PJYSTFuP6urw6oVO3HoW/oX3pNwfI00P1NzeCMWr+eH9wFO0joGEIOOaF3c2c4fFiHY3pTerXROeDdRuALN43hBUqWE",0),
(6,"Jean", "Dupont","jean.dupont@gmail.com","$argon2i$v=19$m=65536,t=5,p=1$oH8H9ga/FfvciTjmvJIXHA5bfOg4Gkdd/oCX9rmeKodLflqrIVlCpZQnD07AKvw0/0f/yv2NGvhICutwSJgnEewmxTyvpO/KsZZblWC1H69XSoqra5n9sgbxTN7T2VhYsrg8KDtJBGvJqvwDSW1nuMGHebSzrVIsSSdk9H7XRcA$JVNigVCKVNAN/Lj0uX5V+bK5wlE5VJ6S4EN1v/RJEjJtvvbn30qOYmUnKLrZ3kpVTatF6aTGTQBCv3kMjQffoP/Fts0RFCTWLDXBTiM5a+0JVwXfHiaeVg9H4mOPLbx9NLy7JDlp14u1mFcy6TBDtf/vd2zI3rEGT5H4qYk8dVM",0);

INSERT INTO GENRE (idGenre, nomGenre) VALUES
(1, "Comédie"),
(2, "Drame"),
(3, "Romance"),
(4, "Action"),
(5, "Historique"),
(6, "Western"),
(7, "Aventure"),
(8, "Thriller"),
(9, "Policier"),
(10, "Fantastique"),
(11, "Science-fiction"),
(12, "Horreur"),
(13, "Zombie"),
(14, "Super-Héros"),
(15, "Fantasy"),
(16, "Documentaire"),
(17, "Espionnage"),
(18, "Guerre");

INSERT INTO FILM (idFilm, titreFilm, resumeFilm, dateSortie, dureeFilm, realisateur, acteur, imgFilm, urlBA, idGenre, valide) VALUES
(1, "Captain America: First Avenger", "Captain America: First Avenger nous plonge dans les premières années de l\'univers Mavel. Steve Rogers,  frêle et timide participe à un programme expérimental le transformant en Super Soldat, plus connu sous le nom de Captain America. Il sera confronté à la diabolique organisation HYDRA dirigée par le redoutable Red Skull.", "2011-08-17", 124, "Joe Johnston", "Chris Evans", "CaptainAmericaFirstAvenger.jpg", "IsiV9IJieMk", 14, 1),
(2, "Iron Man", "Tony Stark, playboy, milliardaire, n\'est pas seulement l\'héritier des usines d\'armement de son père, c\'est également un inventeur de génie. Alors qu\'il est en déplacement en Afghanistan pour présenter sa dernière création, il est enlevé par des terroristes. Traumatisé par cette expérience, il décida de créer une armure de combat unique pour combattre le mal.", "2008-04-30", 126, "Jon Favreau", "Robert Downey Jr.", "IronMan.jpg", "Q2lEqf_F6Pk", 14, 1),
(3, "Iron Man 2", "Le monde sait désormais que l\'inventeur milliardaire Tony Stark et le super-héros Iron Man ne font qu\'un. Cependant, malgré les pressions, Tony n\'est pas disposé à divulguer les secrets de son armure, redoutant que l\'information atterrisse dans de mauvaises mains. Avec Pepper Potts et James Rhodey Rhodes à ses côtés, Tony va forger de nouvelles alliances et affronter de nouvelles forces toutes-puissantes.", "2010-04-28", 126, "Jon Favreau", "Robert Downey Jr.", "IronMan2.jpg", "VdZj2QYTAic", 14, 1),
(4, "Thor", "Au royaume d\'Asgard, Thor est un guerrier aussi puissant qu\'arrogant. Alors que ses actes téméraires, qui ont déclenché une guerre ancestrale, l\'ont fait bannir sur Terre par Odin, son père, il est condamné à vivre parmi les hommes. Cependant, lorsque les forces du mal, en provenance d\'Asgard, s\'apprêtent à se déchaîner sur la Terre, Thor va devoir se comporter en véritable héros.", "2011-04-27", 114, "Kenneth Branagh", "Chris Hemsworth", "Thor.jpg", "MeN3Gwf2sDg", 14, 0),
(5, "L\'Incroyable Hulk", "Le scientifique Bruce Banner cherche désespérément un antidote aux radiations qui ont créé Hulk. Il vit dans l\'ombre et parcourt la planète à la recherche d\'un remède. La force destructrice de Hulk attire le Général Thaddeus E. Ross et son bras droit Blonsky qui rêvent de l\'utiliser à des fins militaires. Ils tentent de développer un sérum pour créer des soldats surpuissants.", "2008-07-23", 114, "Louis Leterrier", "Edward Norton", "LIncroyableHulk.jpg", "LCjTxK1B5Pg", 14, 1),
(6, "Le Seigneur des anneaux : La Communauté de l\'anneau", "Un jeune et timide Hobbit, Frodon Sacquet, hérite d\'un anneau magique. Bien loin d\'être une simple babiole, il s\'agit d\'un instrument de pouvoir absolu qui permettrait à Sauron, le Seigneur des ténèbres, de régner sur la Terre du Milieu et de réduire en esclavage ses peuples. Frodon doit parvenir jusqu\'à la Crevasse du Destin pour détruire l\'anneau.", "2020-11-26", 228, "Peter Jackson", "Elijah Wood", "LeSeigneurdesanneauxLaCommunautédelanneau.jpg", "nalLU8i4zgs", 7, 1),
(7, "Avengers : Infinity War", "Père adoptif de Gamora et Nébula, Thanos a commencé à recueillir les six Pierres d\'Infinité : la Pierre du Pouvoir, la Pierre de l\'Espace, la Pierre de Réalité, la Pierre de l\'Âme, la Pierre du Temps et la Pierre de l\'Esprit. Son objectif est de réunir ces six gemmes sur un gantelet doré, forgé par le nain Eitri sur Nidavellir, afin d\'utiliser leur immense puissance pour détruire la moitié de la population de l\'Univers et rétablir ainsi un certain équilibre. Dans sa quête le menant sur diverses planètes, la Terre, Knowhere et Vormir, Thanos est aidé par ses enfants adoptifs : Ebony Maw, Cull Obsidian, Corvus Glaive et Proxima Midnight. Face à cette nouvelle menace qui concerne l\'Univers entier, le groupe de super-héros des Avengers, divisé depuis 2 ans, doit se reformer, et s\'associer au Docteur Strange, aux Gardiens de la Galaxie et au peuple du Wakanda.", "2018-04-25", 149, "Anthony et Joe Russo", "Robert Downey Jr.", "AvengersInfinityWar.jpg", "eIWs2IUr3Vs", 14, 1),
(8, "Rambo", "Un ancien militaire du Vietnam doit se battre pour rester en vie.", "1982-10-22", 90, "Ted Kotchev", "Sylvester Stallone", "Rambo.jpg", "lXtZmFf0OKo", 2, 0),
(9, "Joker", "Le film, qui relate une histoire originale inédite sur grand écran, se focalise sur la figure emblématique de lennemi juré de Batman. Il brosse le portrait dArthur Fleck, un homme sans concession méprisé par la société.", "2019-10-09", 122, "Todd Phillips", "Joaquin Phoenix", "Joker.jpg", "OoTx1cYC5u8", 2, 0);

INSERT INTO Preferer (idFilm, idUtilisateur, liker, favoris) VALUES
(5, 6, 2, 0),
(4, 2, 1, 1),
(1, 4, 1, 1),
(1, 2, 1, 1),
(1, 6, 1, 1);
