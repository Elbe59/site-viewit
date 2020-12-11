#--- Create TABLE viewIt

CREATE DATABASE IF NOT EXISTS `viewit` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `viewit`;

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

	CONSTRAINT FILM_GENRE_FK FOREIGN KEY (idGenre) REFERENCES genre(idGenre)
);

#--- liker = 0 : défaut, liker = 1 : j'aime, liker = -1 : j'aime pas
#--- favoris = 0 : non défaut; favoris = 1 : oui
CREATE TABLE preferer (
    idFilm        Int(5) NOT NULL ,
    idUtilisateur Int(5) NOT NULL,
	liker		  Int(1) NOT NULL,
	favoris		  Int(1) NOT NULL,
	CONSTRAINT Preferer_PK PRIMARY KEY (idFilm,idUtilisateur),

	CONSTRAINT Preferer_FILM_FK FOREIGN KEY (idFilm) REFERENCES film(idFilm) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT Preferer_UTILISATEUR_FK FOREIGN KEY (idUtilisateur) REFERENCES utilisateur(idUtilisateur) ON DELETE CASCADE ON UPDATE CASCADE
);
#--- Création des insert

INSERT INTO utilisateur (idUtilisateur, prenomUtilisateur, nomUtilisateur, email, mdpHash, admin) VALUES
(1,"Defaut", "Defaut","defaut.defaut@gmail.com","$argon2i$v=19$m=65536,t=5,p=1$zFnaINNvYeCrC75OYuuZEl9al5weOMnSXcOUoIWhUdIMRbNvXF1ipU5aMaU0HVXtsotzpepy/LxIHtd7SJMgFpk7T4T6eE24y3CxyiuG1woN5vMrPCnl4ldjtAmWQ/iEsL0JRXuthPrbFO1GkA+k4D2s7E9SNF9JA8sJaSHURU8$U5xfj0Qz7+T3sr05PxuUhEgAKU2+WxhcrFMUUS2yVGi2egf4rSsxZ9FSXYliBnx03aXgNEvtPrZ7zWq2TQdw9LA+gWS4+IOrKkR56EkVZDgD59jkDVE5P59jmqSIVpQq51jx8SkCITxgOaS8xMGuDaUtpBIce1UfxGGEgGi8pRg",0),
(2,"Jack", "Barzone","jack.barzone@gmail.com","$argon2i$v=19$m=65536,t=5,p=1$dTSkKiptldBfurDMM/Ez+1xdd8kAbGOk6INnM4YxG8bBVkywlRCjWB7+qrQjj9Ifb1axUqWswQHEX8zm8jKcg1SFi+WCb1ZHTFea6TtkNnsTva+sOT6SshKfiCY7HOLAvUUmDsX+W8mxUtDhOdvHfYU96dkgy6H2TM5CkP73Im0$q0xJDLk0mJcq+JodHjncyxtqOk+2KPr9AQ1womwdmYajdeoVuRaS/IrDoKVlwxYBL6M5BzNAUk7sDudKtEzSwKcsjeBcJEkKWoIZIsS+yo59tJ7vwd0PsgWLqWt1M49UG0sT1dS/jVAUIp6ANclZkTtgq5kJrNncR6SCSPyjLk8",1),
(3,"Denis", "Cauchois","denis.cauchois@yncrea.fr","$argon2i$v=19$m=65536,t=5,p=1$WPUiir9tiipIBdnm7yTVpZu11Z14DGoBPgQ/8kVJpO+FozRh+LcwA5UchCcpPOxHjzPxRs5FOSSS+1OTrGPyiYA65tq8W5sOldJMVLvCCDIGEqnHuz1vhoAZHzbcfrH3uK9W3SHFiWZAONF9+v/N2zrxLgtd7DBllOK3N5viuEU$Y6/QAaEt/u9cO7uSd7wL25hCTRG8VVLnk+SrTTszEteHh5Soa9t4cz0Bs2zl3DhTNNDfAq18uheiLNu0hZlORuPmEnbbDRo1vF+2pHiD+OhVBaHeouhEY/IypHqa3oOKXdbtxRqHq4bwPIp4+QhM0jcU+xSXX88T9R9kO11bYJA",1),
(4,"Yann", "Riquier","yann.riquier@externe.junia.com","$argon2i$v=19$m=65536,t=5,p=1$+Q2ncq4NWBznggWM4VNPDfc0+AB5qT2RXBtkcauZ0OhkdtTCHJ/H4ASooZtoOxSKj0p32oXZcBHkpEYt8e2FNU343WZY4TfnTi5Dy6LtZ2MSPCevEEeCb1WseVQ7NYs+u1H2hf54HRCBSJ0Jg7Zz1dJn7o2nuI0UJwPbJ6mmkHg$KVtSpZ2ceqrJ13Kig6MvyMTpf/Hzvc/fo4cSGYATmG42H2ukYU8Tw5u9Ro7WQEOwFguW3Nb8mi6W2qzZIieU8pNaM8kIC3G19ckCIHlcwoBLoDoF6mSEeiO9yZMAp73AZvLnVXvpGTgCJtZuEpcMVBG5QQXQ1r5ak9d3c5W/TqQ",1),
(5,"Fabien", "Turpin","fab.turpin@gmail.com","$argon2i$v=19$m=65536,t=5,p=1$jWTIoFAnUmy7rl4vW96liLXkGT8Y/CqS9OcFNUP7fBMeYQLhgvR3nM101cCtlnlzlm7036NPPkzHBzGrwAIapzyGTQu+JsIqdpvAZGKEApfMplP2Kk5BlM37DoYpZ7lGi/dYI41QPDoHT19ptO1eKP7JJdRWLDmqL2fNr1kjwZ4$ykaOBReDqgQoNpsdvlc2zxQGfHykaSC6Jkamm4Tr6oanUIQdevqHXcXKwnAYvqaW8n3h+H8A0H+lLzC9PJYSTFuP6urw6oVO3HoW/oX3pNwfI00P1NzeCMWr+eH9wFO0joGEIOOaF3c2c4fFiHY3pTerXROeDdRuALN43hBUqWE",0),
(6,"Jean", "Dupont","jean.dupont@gmail.com","$argon2i$v=19$m=65536,t=5,p=1$oH8H9ga/FfvciTjmvJIXHA5bfOg4Gkdd/oCX9rmeKodLflqrIVlCpZQnD07AKvw0/0f/yv2NGvhICutwSJgnEewmxTyvpO/KsZZblWC1H69XSoqra5n9sgbxTN7T2VhYsrg8KDtJBGvJqvwDSW1nuMGHebSzrVIsSSdk9H7XRcA$JVNigVCKVNAN/Lj0uX5V+bK5wlE5VJ6S4EN1v/RJEjJtvvbn30qOYmUnKLrZ3kpVTatF6aTGTQBCv3kMjQffoP/Fts0RFCTWLDXBTiM5a+0JVwXfHiaeVg9H4mOPLbx9NLy7JDlp14u1mFcy6TBDtf/vd2zI3rEGT5H4qYk8dVM",0),
(7, "Emile", "Mongenet", "emile.mongenet@student.yncrea.fr", "$argon2i$v=19$m=65536,t=5,p=1$lDevnBIpBMZxpdTROvfBzeZ5F6t3D/Skq5pdaLCP2bZU6ik/wCe8QQsIv15khRy84r5WBGOI9Nvu++0zcd3Q5rFptfBlt2rGqITYJQ2VIGqhA0cwpfONa9TE7iCBUB+EQRUnUgjzAfrmR4K0EmDahc5+sjNg6Tn3qfSOld4fQN4$HeJBjzvm09d0OWboW8zUC0rJLvLhKrQIcdZ10ayowI90wpivlxClMiRrMH/A1KvJUHP8w886nwU8Dg3q2gfm2+3RatTf0UBlX5jaFSc4u1p6n8v+pobpspn1xFCNJpKX1FvszR3PnsXWxoen9rgWb5wqCcoCnz8PL0ok+2B9tII", 0),
(8, "Kylian", "Barthelemot", "kylian.barthelemot@student.yncrea.fr", "$argon2i$v=19$m=65536,t=5,p=1$LngexZ3eBmaD5wZAvNCG7wqNHn+XeDB4VELb5tcrYPGvRBGypKq/UynOt1jEVDfWRKbmC+x0b2FoetJ1JHTZzxrbNFFcdqtxVFmeTw2C+jCWSx82RVr63avizQyMLBopoduLK/rcuSoLvWM0NI214KDKz9pIqKmFGsjEChGVO1k$NWJnN6by04xoZ5ssy2f0xIVPcHJsL2FWa7DKroOP9ZQAuqyHNdMbsIXraBUSCsj5xcQOxAlkp5AceNn73CzRqSp8t3dL+DH2HH0C/MEWeGMQ6c1kf6wqdtcNairf91HPOzKkx7d+Fx50cVI2Zy/9/7CYpAhJP6dNrorUxyhMIxU", 0),
(9, "Quentin", "Callens", "quentin.callens@hei.yncrea.fr", "$argon2i$v=19$m=65536,t=5,p=1$Hc+8BKsRZWhPWiRvUsM5uit+tu1zrSYB8x9kkFVKSmW0vWD1aEuAwpI5MVEv18bdLG1ENHPl0J53d+r+jau3Tp89jsGCaP9L0E+ypE3E01TcX7eRknltdz/ig5m5Pfvy9oxZSOzP1NjiC76pyfiKHN0NLHwvL6kqfxlA5i/KH+Q$zaRhsOJzwL7l+O4Nc8r9k+9Th9hxFezoUIFeshnXCwMNCs2WQIJzY89JlI/7FhjG4QB+KrsqyAGg5KcE3uBHphPBWumoCTK7ScnuWnpTLF+dY2y8hOCVkvVTlqiyTJpmz89J2kbZOW5XkMMLRY64emgAo9RIZWwXX9i6XIOO4yo", 0),
(10, "Pierre", "Sanchez", "pierre.sanchez@hei.yncrea.fr", "$argon2i$v=19$m=65536,t=5,p=1$NHllxEJI1OfNZS2gsRW71Kc2+9WzDy7Rg0EIpnJ9zfPE+7rpTbHEaD392fUij5gIlMtAWrauuDk4MYfscp2Ui5tVXbE2PyS1nsaMUe7odPk/f8PCuohTKg4AHPML2uN1ox9balp8/y17kheYQI3kpUdfTEIoAYLsY6q0Ao7MNMw$kfLvueW31KqLWge+H1w1YwB25mFLsAuSMNdk57WVY+2UXNh0rPitJLKjvNdKeYnLQt7DXioZ9cBGFI4MacdfWME1dN3BgXu4JEc+BtDCwfOiFmE8jCxWBg7MNU4A4VJdaKzROTZaTd2sHI26ggUP8kWITtNmK+M4MV8oHhwLepA", 0),
(11, "John", "Smith", "john.smith@junia.fr", "$argon2i$v=19$m=65536,t=5,p=1$+2JExSlulkCM1Gp3PrUppPDpo8c5F9CzzJSDwtEIPFTyVgSa8/PMD2Ntjqoc+MjxK22IV4SLYUjSBkciXQxaQxveqey+k+81bSH12wBw4shOiEwxnUe4tzQJ14jSU2XMW5qDK8N2HHxyRxIdS70FiguSDGnU+NAJIr+WDeFaOiE$X43mnI4d3YnVMhEVWcf1vDn2/j0LJqe+0c4RMFGMDQWzaWxYaaVa2HacRR52tYUGhYfjKLaua1+jPmN/UugxHp6YwSbDpFrtxIiZ9ejjrcVMf1Wv/ET4F3PlB4FqUQGf2x2b4yuK1Xj1mJWPE17KOjqE68rMBwvdSNxOiMrLdeE", 0),
(12, "Aaron", "Mundo", "aaron.mundo@junia.fr", "$argon2i$v=19$m=65536,t=5,p=1$HZZRmVsueVo08hwO3KyAPjMp9OYpStSaEZXGQmb4g5Z9vjgfHEk1XI1bZjCJJN8EXQzJGskRfuyRKGfMSTd8TEeabc9O58nooZdMZGwuourXCFxCyUu1LlpWZEzsMuodJaavVkDmlWr8f/ZoqN1DRB5CDXHOPkaayAHhW84kKFU$o5VTjJjKrNtjhqIPn4YsjTCPOuw5qyOP9aOzA1Igo8JskLRS72ZzuOI6YhEQNTF/lKwC38dhw5Y2ZY7pvhcIzoAzX0rCwtr1Hi328ryIncJ9Xp12bCr8mDKXTI94Jksm8xPVeAFzFxlosUDyBHPAlMcARoqNLevG8kZpMB3x6Ns", 1);

INSERT INTO genre (idGenre, nomGenre) VALUES
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

INSERT INTO film (idFilm, titreFilm, resumeFilm, dateSortie, dureeFilm, realisateur, acteur, imgFilm, urlBA, idGenre, valide) VALUES
(1, "Captain America: First Avenger", "Captain America: First Avenger nous plonge dans les premières années de l\'univers Mavel. Steve Rogers,  frêle et timide participe à un programme expérimental le transformant en Super Soldat, plus connu sous le nom de Captain America. Il sera confronté à la diabolique organisation HYDRA dirigée par le redoutable Red Skull.", "2011-08-17", 124, "Joe Johnston", "Chris Evans", "CaptainAmericaFirstAvenger.jpg", "IsiV9IJieMk", 14, 1),
(2, "Iron Man", "Tony Stark, playboy, milliardaire, n\'est pas seulement l\'héritier des usines d\'armement de son père, c\'est également un inventeur de génie. Alors qu\'il est en déplacement en Afghanistan pour présenter sa dernière création, il est enlevé par des terroristes. Traumatisé par cette expérience, il décida de créer une armure de combat unique pour combattre le mal.", "2008-04-30", 126, "Jon Favreau", "Robert Downey Jr.", "IronMan.jpg", "Q2lEqf_F6Pk", 14, 1),
(3, "Iron Man 2", "Le monde sait désormais que l\'inventeur milliardaire Tony Stark et le super-héros Iron Man ne font qu\'un. Cependant, malgré les pressions, Tony n\'est pas disposé à divulguer les secrets de son armure, redoutant que l\'information atterrisse dans de mauvaises mains. Avec Pepper Potts et James Rhodey Rhodes à ses côtés, Tony va forger de nouvelles alliances et affronter de nouvelles forces toutes-puissantes.", "2010-04-28", 126, "Jon Favreau", "Robert Downey Jr.", "IronMan2.jpg", "VdZj2QYTAic", 14, 1),
(4, "Thor", "Au royaume d\'Asgard, Thor est un guerrier aussi puissant qu\'arrogant. Alors que ses actes téméraires, qui ont déclenché une guerre ancestrale, l\'ont fait bannir sur Terre par Odin, son père, il est condamné à vivre parmi les hommes. Cependant, lorsque les forces du mal, en provenance d\'Asgard, s\'apprêtent à se déchaîner sur la Terre, Thor va devoir se comporter en véritable héros.", "2011-04-27", 114, "Kenneth Branagh", "Chris Hemsworth", "Thor.jpg", "MeN3Gwf2sDg", 14, 1),
(5, "L\'Incroyable Hulk", "Le scientifique Bruce Banner cherche désespérément un antidote aux radiations qui ont créé Hulk. Il vit dans l\'ombre et parcourt la planète à la recherche d\'un remède. La force destructrice de Hulk attire le Général Thaddeus E. Ross et son bras droit Blonsky qui rêvent de l\'utiliser à des fins militaires. Ils tentent de développer un sérum pour créer des soldats surpuissants.", "2008-07-23", 114, "Louis Leterrier", "Edward Norton", "LIncroyableHulk.jpg", "LCjTxK1B5Pg", 14, 1),
(6, "Le Seigneur des anneaux : La Communauté de l\'anneau", "Un jeune et timide Hobbit, Frodon Sacquet, hérite d\'un anneau magique. Bien loin d\'être une simple babiole, il s\'agit d\'un instrument de pouvoir absolu qui permettrait à Sauron, le Seigneur des ténèbres, de régner sur la Terre du Milieu et de réduire en esclavage ses peuples. Frodon doit parvenir jusqu\'à la Crevasse du Destin pour détruire l\'anneau.", "2001-12-19", 228, "Peter Jackson", "Elijah Wood", "LeSeigneurdesanneauxLaCommunautédelanneau.jpg", "nalLU8i4zgs", 7, 1),
(7, "Avengers : Infinity War", "Père adoptif de Gamora et Nébula, Thanos a commencé à recueillir les six Pierres d\'Infinité : la Pierre du Pouvoir, la Pierre de l\'Espace, la Pierre de Réalité, la Pierre de l\'Âme, la Pierre du Temps et la Pierre de l\'Esprit. Son objectif est de réunir ces six gemmes sur un gantelet doré, forgé par le nain Eitri sur Nidavellir, afin d\'utiliser leur immense puissance pour détruire la moitié de la population de l\'Univers et rétablir ainsi un certain équilibre. Dans sa quête le menant sur diverses planètes, la Terre, Knowhere et Vormir, Thanos est aidé par ses enfants adoptifs : Ebony Maw, Cull Obsidian, Corvus Glaive et Proxima Midnight. Face à cette nouvelle menace qui concerne l\'Univers entier, le groupe de super-héros des Avengers, divisé depuis 2 ans, doit se reformer, et s\'associer au Docteur Strange, aux Gardiens de la Galaxie et au peuple du Wakanda.", "2018-04-25", 149, "Anthony et Joe Russo", "Robert Downey Jr.", "AvengersInfinityWar.jpg", "eIWs2IUr3Vs", 14, 1),
(8, "Rambo", "Un ancien militaire du Vietnam doit se battre pour rester en vie.", "1982-10-22", 90, "Ted Kotchev", "Sylvester Stallone", "Rambo.jpg", "lXtZmFf0OKo", 2, 1),
(9, "Joker", "Le film, qui relate une histoire originale inédite sur grand écran, se focalise sur la figure emblématique de lennemi juré de Batman. Il brosse le portrait dArthur Fleck, un homme sans concession méprisé par la société.", "2019-10-09", 122, "Todd Phillips", "Joaquin Phoenix", "Joker.jpg", "OoTx1cYC5u8", 2, 1),
(10, "Ça : Chapitre 2", "Tous les 27 ans, une créature maléfique revient hanter les rues de Derry, dans le Maine. Près de trente ans après les événements du premier opus, les membres du Club des Ratés, désormais adultes, se retrouvent.", "2019-11-26", 169, "Andrés Muschietti", "Bill Skarsgård", "ÇaChapitre2.jpg", "G8fR1vvrLmI", 12, 1),
(11, "Ça", "À Derry, dans le Maine, sept enfants ayant du mal à s\'intégrer se sont regroupés au sein du « Club des Ratés ». Rejetés par leurs camarades, ils sont les cibles favorites des gros durs de l\'école. Ils ont aussi en commun le fait d\'avoir éprouvé leur plus grande terreur face à un terrible prédateur métamorphe qu\'ils appellent « Ça ». Car depuis toujours, Derry est en proie à une créature qui émerge des égouts tous les 27 ans pour se nourrir des terreurs de ses victimes de choix : les enfants. Bien décidés à rester soudés, les Ratés tentent de surmonter leurs peurs pour enrayer un nouveau cycle meurtrier. Un cycle qui a commencé un jour de pluie lorsqu\'un petit garçon poursuivant son bateau en papier s\'est retrouvé face-à-face avec un clown répondant au nom de Grippe-Sou...", "2018-01-28", 135, "Andrés Muschietti", "Bill Skarsgård", "Ça.jpg", "Bk9F2lt0xn4", 12, 1),
(12, "Interstellar", "Dans un futur proche, la Terre est de moins en moins accueillante pour l\'humanité qui connaît une grave crise alimentaire. Le film raconte les aventures d\'un groupe d\'explorateurs qui utilise une faille récemment découverte dans l\'espace-temps afin de repousser les limites humaines et partir à la conquête des distances astronomiques dans un voyage interstellaire.", "2018-11-18", 169, "Christopher Nolan", "Matthew McConaughey", "Interstellar.jpg", "VaOijhK3CRU", 11, 1),
(13, "The Revenant", "Durant une expédition dans une Amérique profondément sauvage, le légendaire trappeur Hugh Glass est brutalement attaqué par un ours et laissé pour mort par les membres de sa propre équipe. Dans sa quête de survie, Glass endure une souffrance inimaginable ainsi que la trahison de son homme de confiance, John Fitzgerald. Guidé par la volonté et l\'amour de sa famille, Glass doit affronter un hiver brutal dans une inexorable lutte pour survivre et trouver la rédemption.", "2016-02-24", 159, "Alejandro González Iñárritu", "Leonardo DiCaprio", "TheRevenant.jpg", "E1jOiqbNTpo", 6, 1),
(14, "Wonder Woman 1984", "Suite des aventures de Diana Prince, alias Wonder Woman, Amazone devenue une super-héroïne dans notre monde. Après la Première guerre mondiale, direction les années 80 ! Cette fois, Wonder Woman doit affronter deux nouveaux ennemis, particulièrement redoutables : Max Lord et Cheetah.", "2020-12-16", 151, "Patty Jenkins", "Gal Gadot", "WonderWoman1984.jpg", "XW2E2Fnh52w", 14, 1),
(15, "Batman : The Dark Knight", "Batman aborde une phase décisive de sa guerre au crime. Avec l\'aide du lieutenant de police Jim Gordon et du nouveau procureur Harvey Dent, il entreprend de démanteler les dernières organisations criminelles qui infestent les rues de la ville. L\'association s\'avère efficace, mais le trio se heurte bientôt à un nouveau génie du crime qui répand la terreur et le chaos dans Gotham : le Joker. On ne sait pas d\'où il vient ni qui il est. Ce criminel possède une intelligence redoutable doublé d\'un humour sordide et n\'hésite pas à s\'attaquer à la pègre locale dans le seul but de semer le chaos.", "2008-07-18", 153, "Christopher Nolan", "Christopher Nolan", "BatmanTheDarkKnight.jpg", "wrcaivEjWCo", 14, 1),
(16, "Les Evades", "En 1947, Andy Dufresne, un jeune banquier, est condamné à la prison à vie pour le meurtre de sa femme et de son amant. Ayant beau clamer son innocence, il est emprisonné à Shawshank, le pénitencier le plus sévère de l\'Etat du Maine. Il y fait la rencontre de Red, un Noir désabusé, détenu depuis vingt ans. Commence alors une grande histoire d\'amitié entre les deux hommes...", "1995-03-01", 140, "Frank Darabont", "Tim Robbins", "LesEvades.jpg", "2e8Otbbcowc", 4, 1),
(17, "Le Seigneur des Anneaux : Le Retour du Roi", "Les armées de Sauron ont attaqué Minas Tirith, la capitale de Gondor. Jamais ce royaume autrefois puissant n\'a eu autant besoin de son roi. Mais Aragorn trouvera-t-il en lui la volonté d\'accomplir sa destinée ?\r\nTandis que Gandalf s\'efforce de soutenir les forces brisées de Gondor, Théoden exhorte les guerriers de Rohan à se joindre au combat. Mais malgré leur courage et leur loyauté, les forces des Hommes ne sont pas de taille à lutter contre les innombrables légions d\'ennemis qui s\'abattent sur le royaume...\r\nChaque victoire se paye d\'immenses sacrifices. Malgré ses pertes, la Communauté se jette dans la bataille pour la vie, ses membres faisant tout pour détourner l\'attention de Sauron afin de donner à Frodon une chance d\'accomplir sa quête.\r\nVoyageant à travers les terres ennemies, ce dernier doit se reposer sur Sam et Gollum, tandis que l\'Anneau continue de le tenter...\r", "2003-12-17", 201, "Peter Jackson", "Sean Astin", "LeSeigneurdesAnneauxLeRetourduRoi.jpg", "kT7CWHxfjgU", 15, 1),
(18, "Le Seigneur des Anneaux : Les Deux Tours", "Après la mort de Boromir et la disparition de Gandalf, la Communauté s\'est scindée en trois. Perdus dans les collines d\'Emyn Muil, Frodon et Sam découvrent qu\'ils sont suivis par Gollum, une créature versatile corrompue par l\'Anneau. Celui-ci promet de conduire les Hobbits jusqu\'à la Porte Noire du Mordor. A travers la Terre du Milieu, Aragorn, Legolas et Gimli font route vers le Rohan, le royaume assiégé de Theoden. Cet ancien grand roi, manipulé par l\'espion de Saroumane, le sinistre Langue de Serpent, est désormais tombé sous la coupe du malfaisant Magicien. Eowyn, la nièce du Roi, reconnaît en Aragorn un meneur d\'hommes. Entretemps, les Hobbits Merry et Pippin, prisonniers des Uruk-hai, se sont échappés et ont découvert dans la mystérieuse Forêt de Fangorn un allié inattendu : Sylvebarbe, gardien des arbres, représentant d\'un ancien peuple végétal dont Saroumane a décimé la forêt...", "2002-12-18", 179, "Peter Jackson", "Elijah Wood", "LeSeigneurdesAnneauxLesdeuxTours.jpg", "c9blKqmyeV4", 15, 1),
(19, "Forest Gump", "Quelques décennies d\'histoire américaine, des années 1940 à la fin du XXème siècle, à travers le regard et l\'étrange odyssée d\'un homme simple et pur, Forrest Gump.", "1994-10-05", 140, "Robert Zemeckis", "Tom Hanks", "ForestGump.jpg", "vhbOdIJyalo", 3, 1),
(20, "La Liste de Schindler", "Evocation des années de guerre d\'Oskar Schindler, fils d\'industriel d\'origine autrichienne rentré à Cracovie en 1939 avec les troupes allemandes. Il va, tout au long de la guerre, protéger des juifs en les faisant travailler dans sa fabrique et en 1944 sauver huit cents hommes et trois cents femmes du camp d\'extermination de Auschwitz-Birkenau.", "1994-03-02", 195, "Steven Spielberg", "Liam Neeson", "LaListedeSchindler.jpg", "ONWtyxzl-GE", 2, 1),
(21, "Le Parrain", "En 1945, à New York, les Corleone sont une des cinq familles de la mafia. Don Vito Corleone, \"parrain\" de cette famille, marie sa fille à un bookmaker. Sollozzo, \" parrain \" de la famille Tattaglia, propose à Don Vito une association dans le trafic de drogue, mais celui-ci refuse. Sonny, un de ses fils, y est quant à lui favorable.\r\nAfin de traiter avec Sonny, Sollozzo tente de faire tuer Don Vito, mais celui-ci en réchappe. Michael, le frère cadet de Sonny, recherche alors les commanditaires de l\'attentat et tue Sollozzo et le chef de la police, en représailles.\r\nMichael part alors en Sicile, où il épouse Apollonia, mais celle-ci est assassinée à sa place. De retour à New York, Michael épouse Kay Adams et se prépare à devenir le successeur de son père...", "1972-10-18", 175, "Francis Ford Coppola", "Marlon Brando", "LeParrain.jpg", "fF6Bc75HVBI", 4, 1),
(22, "Your Name", "Mitsuha, adolescente coincée dans une famille traditionnelle, rêve de quitter ses montagnes natales pour découvrir la vie trépidante de Tokyo. Elle est loin dimaginer pouvoir vivre laventure urbaine dans la peau de Taki, un jeune lycéen vivant à Tokyo, occupé entre son petit boulot dans un restaurant italien et ses nombreux amis. À travers ses rêves, Mitsuha se voit littéralement propulsée dans la vie du jeune garçon au point quelle croit vivre la réalité... Tout bascule lorsquelle réalise que Taki rêve également dune vie dans les montagnes, entouré dune famille traditionnelle dans la peau dune jeune fille ! Une étrange relation sinstalle entre leurs deux corps quils accaparent mutuellement. Quel mystère se cache derrière ces rêves étranges qui unissent deux destinées que tout oppose et qui ne se sont jamais rencontrées ?", "2016-12-28", 110, "Makoto Shinkai", "Yoann Borg", "YourName.jpg", "AROOK45LXXg", 10, 1);

INSERT INTO preferer (idFilm, idUtilisateur, liker, favoris) VALUES
(1, 2, 1, 0),
(1, 4, 1, 1),
(1, 6, 1, 1),
(1, 7, 1, 1),
(1, 8, 1, 0),
(1, 9, -1, 0),
(1, 10, -1, 0),
(2, 3, -1, 0),
(2, 6, 0, 1),
(2, 7, 1, 1),
(2, 8, 1, 0),
(2, 9, -1, 0),
(2, 10, 1, 0),
(3, 3, -1, 0),
(3, 6, 0, 1),
(3, 7, 1, 1),
(3, 8, 1, 0),
(3, 9, 1, 0),
(3, 10, -1, 0),
(4, 2, 1, 0),
(4, 6, 0, 1),
(4, 7, 1, 1),
(4, 9, -1, 0),
(4, 10, -1, 0),
(5, 2, 0, 1),
(5, 6, -1, 1),
(5, 7, 1, 0),
(5, 8, 1, 0),
(5, 9, 1, 0),
(5, 10, -1, 0),
(6, 2, 0, 1),
(6, 3, 1, 1),
(6, 6, -1, 0),
(6, 7, 1, 1),
(6, 8, 1, 0),
(6, 9, -1, 0),
(6, 10, 1, 0),
(7, 3, 1, 1),
(7, 6, 1, 1),
(7, 7, 1, 1),
(7, 8, 0, 1),
(7, 9, 0, 1),
(7, 10, 1, 1),
(7, 12, 0, 1),
(8, 2, 0, 1),
(8, 3, 0, 1),
(8, 7, 1, 1),
(8, 8, -1, 0),
(8, 9, 1, 0),
(8, 10, -1, 0),
(9, 3, 1, 0),
(9, 6, 1, 1),
(9, 8, 1, 1),
(9, 9, 1, 0),
(9, 10, 1, 0),
(10, 2, 1, 1),
(10, 3, 1, 1),
(10, 8, 0, 1),
(10, 9, 1, 0),
(10, 10, 1, 0),
(11, 2, 1, 1),
(11, 3, 1, 1),
(11, 8, 1, 1),
(11, 9, -1, 0),
(11, 10, 1, 0),
(12, 2, 1, 1),
(12, 3, 1, 1),
(12, 8, 1, 1),
(12, 10, 1, 0),
(13, 8, 1, 1),
(13, 9, 1, 0),
(13, 10, 1, 0),
(14, 6, 0, 1),
(14, 7, 1, 0),
(14, 9, 1, 0),
(14, 10, 1, 0),
(15, 6, 1, 1),
(17, 2, 1, 1),
(17, 6, -1, 0),
(18, 2, -1, 1),
(18, 6, -1, 0),
(19, 2, 1, 1),
(20, 2, 0, 1),
(21, 2, 1, 1);