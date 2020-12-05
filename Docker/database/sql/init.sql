--
-- Base de données : `viewit`
--
CREATE DATABASE IF NOT EXISTS `viewit` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `viewit`;

-- --------------------------------------------------------

--
-- Structure de la table `film`
--

DROP TABLE IF EXISTS `film`;
CREATE TABLE `film` (
  `idFilm` int(5) NOT NULL,
  `titreFilm` varchar(100) NOT NULL,
  `resumeFilm` varchar(500) NOT NULL,
  `dateSortie` date NOT NULL,
  `dureeFilm` int(3) NOT NULL,
  `realisateur` varchar(100) NOT NULL,
  `acteur` varchar(100) DEFAULT NULL,
  `imgFilm` varchar(100) DEFAULT NULL,
  `urlBA` varchar(100) DEFAULT NULL,
  `idGenre` int(2) NOT NULL,
  `valide` int(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `film`
--

INSERT INTO `film` (`idFilm`, `titreFilm`, `resumeFilm`, `dateSortie`, `dureeFilm`, `realisateur`, `acteur`, `imgFilm`, `urlBA`, `idGenre`, `valide`) VALUES
(1, 'Captain America: First Avenger', 'Captain America: First Avenger nous plonge dans les premières années de l\'univers Mavel. Steve Rogers,  frêle et timide participe à un programme expérimental le transformant en Super Soldat, plus connu sous le nom de Captain America. Il sera confronté à la diabolique organisation HYDRA dirigée par le redoutable Red Skull.', '2011-08-17', 124, 'Joe Johnston', 'Chris Evans', 'captainAmericaFirstAvenger.jpg', 'IsiV9IJieMk', 14, 1),
(2, 'Iron Man', 'Tony Stark, playboy, milliardaire, n\'est pas seulement l\'héritier des usines d\'armement de son père, c\'est également un inventeur de génie. Alors qu\'il est en déplacement en Afghanistan pour présenter sa dernière création, il est enlevé par des terroristes. Traumatisé par cette expérience, il décida de créer une armure de combat unique pour combattre le mal.', '2008-04-30', 126, 'Jon Favreau', 'Robert Downey Jr.', 'ironMan.jpg', 'Q2lEqf_F6Pk', 14, 1),
(3, 'Iron Man 2', 'Le monde sait désormais que l\'inventeur milliardaire Tony Stark et le super-héros Iron Man ne font qu\'un. Cependant, malgré les pressions, Tony n\'est pas disposé à divulguer les secrets de son armure, redoutant que l\'information atterrisse dans de mauvaises mains. Avec Pepper Potts et James Rhodey Rhodes à ses côtés, Tony va forger de nouvelles alliances et affronter de nouvelles forces toutes-puissantes.', '2010-04-28', 126, 'Jon Favreau', 'Robert Downey Jr.', 'ironMan2.jpg', 'VdZj2QYTAic', 14, 1),
(4, 'Thor', 'Au royaume d\'Asgard, Thor est un guerrier aussi puissant qu\'arrogant. Alors que ses actes téméraires, qui ont déclenché une guerre ancestrale, l\'ont fait bannir sur Terre par Odin, son père, il est condamné à vivre parmi les hommes. Cependant, lorsque les forces du mal, en provenance d\'Asgard, s\'apprêtent à se déchaîner sur la Terre, Thor va devoir se comporter en véritable héros.', '2011-04-27', 114, 'Kenneth Branagh', 'Chris Hemsworth', 'thor.jpg', 'MeN3Gwf2sDg', 14, 0),
(5, 'L\'Incroyable Hulk', 'Le scientifique Bruce Banner cherche désespérément un antidote aux radiations qui ont créé Hulk. Il vit dans l\'ombre et parcourt la planète à la recherche d\'un remède. La force destructrice de Hulk attire le Général Thaddeus E. Ross et son bras droit Blonsky qui rêvent de l\'utiliser à des fins militaires. Ils tentent de développer un sérum pour créer des soldats surpuissants.', '2008-07-23', 114, 'Louis Leterrier', 'Edward Norton', 'theIncredibleHulk.jpg', 'LCjTxK1B5Pg', 14, 1),
(6, 'Ça : Chapitre 2', 'Tous les 27 ans, une créature maléfique revient hanter les rues de Derry, dans le Maine. Près de trente ans après les événements du premier opus, les membres du Club des Ratés, désormais adultes, se retrouvent.', '2019-08-26', 169, 'Andrés Muschietti', 'Bill Hader', 'ca.jpg', 'G8fR1vvrLmI', 12, 1),
(7, 'Le Seigneur des anneaux : La Communauté de l\'anneau', 'Un jeune et timide Hobbit, Frodon Sacquet, hérite d\'un anneau magique. Bien loin d\'être une simple babiole, il s\'agit d\'un instrument de pouvoir absolu qui permettrait à Sauron, le Seigneur des ténèbres, de régner sur la Terre du Milieu et de réduire en esclavage ses peuples. Frodon doit parvenir jusqu\'à la Crevasse du Destin pour détruire l\'anneau.', '2020-11-26', 228, 'Peter Jackson', 'Elijah Wood', 'fichier', 'nalLU8i4zgs', 7, 1),
(8, 'dsf', 'ersq', '2020-12-31', 1233, 'srtd', '12', 'dsf.png', '', 1, 1);

-- --------------------------------------------------------

--
-- Structure de la table `genre`
--

DROP TABLE IF EXISTS `genre`;
CREATE TABLE `genre` (
  `idGenre` int(2) NOT NULL,
  `nomGenre` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `genre`
--

INSERT INTO `genre` (`idGenre`, `nomGenre`) VALUES
(1, 'Comédie'),
(2, 'Drame'),
(3, 'Romance'),
(4, 'Action'),
(5, 'Historique'),
(6, 'Western'),
(7, 'Aventure'),
(8, 'Thriller'),
(9, 'Policier'),
(10, 'Fantastique'),
(11, 'Science-fiction'),
(12, 'Horreur'),
(13, 'Zombie'),
(14, 'Super-Héros'),
(15, 'Fantasy'),
(16, 'Documentaire'),
(17, 'Espionnage'),
(18, 'Guerre');

-- --------------------------------------------------------

--
-- Structure de la table `preferer`
--

DROP TABLE IF EXISTS `preferer`;
CREATE TABLE `preferer` (
  `idFilm` int(5) NOT NULL,
  `idUtilisateur` int(5) NOT NULL,
  `liker` int(1) NOT NULL,
  `favoris` int(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `preferer`
--

INSERT INTO `preferer` (`idFilm`, `idUtilisateur`, `liker`, `favoris`) VALUES
(1, 2, -1, 0),
(1, 4, 1, 1),
(1, 6, 1, 1),
(4, 2, 1, 1),
(5, 6, 2, 0),
(6, 2, 0, 0),
(7, 2, 0, 0);

-- --------------------------------------------------------

--
-- Structure de la table `utilisateur`
--

DROP TABLE IF EXISTS `utilisateur`;
CREATE TABLE `utilisateur` (
  `idUtilisateur` int(5) NOT NULL,
  `prenomUtilisateur` varchar(50) NOT NULL,
  `nomUtilisateur` varchar(50) NOT NULL,
  `email` varchar(266) NOT NULL,
  `mdp` varchar(50) NOT NULL,
  `mdpHash` varchar(1000) NOT NULL,
  `admin` int(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `utilisateur`
--

INSERT INTO `utilisateur` (`idUtilisateur`, `prenomUtilisateur`, `nomUtilisateur`, `email`, `mdp`, `mdpHash`, `admin`) VALUES
(1, 'Defaut', 'Defaut', 'defaut.defaut@gmail.com', '123', '$argon2i$v=19$m=65536,t=5,p=1$zFnaINNvYeCrC75OYuuZEl9al5weOMnSXcOUoIWhUdIMRbNvXF1ipU5aMaU0HVXtsotzpepy/LxIHtd7SJMgFpk7T4T6eE24y3CxyiuG1woN5vMrPCnl4ldjtAmWQ/iEsL0JRXuthPrbFO1GkA+k4D2s7E9SNF9JA8sJaSHURU8$U5xfj0Qz7+T3sr05PxuUhEgAKU2+WxhcrFMUUS2yVGi2egf4rSsxZ9FSXYliBnx03aXgNEvtPrZ7zWq2TQdw9LA+gWS4+IOrKkR56EkVZDgD59jkDVE5P59jmqSIVpQq51jx8SkCITxgOaS8xMGuDaUtpBIce1UfxGGEgGi8pRg', 0),
(2, 'Jack', 'Barzone', 'jack.barzone@gmail.com', '1234', '$argon2i$v=19$m=65536,t=5,p=1$dTSkKiptldBfurDMM/Ez+1xdd8kAbGOk6INnM4YxG8bBVkywlRCjWB7+qrQjj9Ifb1axUqWswQHEX8zm8jKcg1SFi+WCb1ZHTFea6TtkNnsTva+sOT6SshKfiCY7HOLAvUUmDsX+W8mxUtDhOdvHfYU96dkgy6H2TM5CkP73Im0$q0xJDLk0mJcq+JodHjncyxtqOk+2KPr9AQ1womwdmYajdeoVuRaS/IrDoKVlwxYBL6M5BzNAUk7sDudKtEzSwKcsjeBcJEkKWoIZIsS+yo59tJ7vwd0PsgWLqWt1M49UG0sT1dS/jVAUIp6ANclZkTtgq5kJrNncR6SCSPyjLk8', 1),
(3, 'Denis', 'Cauchois', 'denis.cauchois@yncrea.fr', 'DenisDu59', '$argon2i$v=19$m=65536,t=5,p=1$WPUiir9tiipIBdnm7yTVpZu11Z14DGoBPgQ/8kVJpO+FozRh+LcwA5UchCcpPOxHjzPxRs5FOSSS+1OTrGPyiYA65tq8W5sOldJMVLvCCDIGEqnHuz1vhoAZHzbcfrH3uK9W3SHFiWZAONF9+v/N2zrxLgtd7DBllOK3N5viuEU$Y6/QAaEt/u9cO7uSd7wL25hCTRG8VVLnk+SrTTszEteHh5Soa9t4cz0Bs2zl3DhTNNDfAq18uheiLNu0hZlORuPmEnbbDRo1vF+2pHiD+OhVBaHeouhEY/IypHqa3oOKXdbtxRqHq4bwPIp4+QhM0jcU+xSXX88T9R9kO11bYJA', 0),
(4, 'Yann', 'Riquier', 'yann.riquier@externe.junia.com', 'Yann1234', '$argon2i$v=19$m=65536,t=5,p=1$+Q2ncq4NWBznggWM4VNPDfc0+AB5qT2RXBtkcauZ0OhkdtTCHJ/H4ASooZtoOxSKj0p32oXZcBHkpEYt8e2FNU343WZY4TfnTi5Dy6LtZ2MSPCevEEeCb1WseVQ7NYs+u1H2hf54HRCBSJ0Jg7Zz1dJn7o2nuI0UJwPbJ6mmkHg$KVtSpZ2ceqrJ13Kig6MvyMTpf/Hzvc/fo4cSGYATmG42H2ukYU8Tw5u9Ro7WQEOwFguW3Nb8mi6W2qzZIieU8pNaM8kIC3G19ckCIHlcwoBLoDoF6mSEeiO9yZMAp73AZvLnVXvpGTgCJtZuEpcMVBG5QQXQ1r5ak9d3c5W/TqQ', 0),
(5, 'Fabien', 'Turpin', 'fab.turpin@gmail.com', 'motdepasse', '$argon2i$v=19$m=65536,t=5,p=1$jWTIoFAnUmy7rl4vW96liLXkGT8Y/CqS9OcFNUP7fBMeYQLhgvR3nM101cCtlnlzlm7036NPPkzHBzGrwAIapzyGTQu+JsIqdpvAZGKEApfMplP2Kk5BlM37DoYpZ7lGi/dYI41QPDoHT19ptO1eKP7JJdRWLDmqL2fNr1kjwZ4$ykaOBReDqgQoNpsdvlc2zxQGfHykaSC6Jkamm4Tr6oanUIQdevqHXcXKwnAYvqaW8n3h+H8A0H+lLzC9PJYSTFuP6urw6oVO3HoW/oX3pNwfI00P1NzeCMWr+eH9wFO0joGEIOOaF3c2c4fFiHY3pTerXROeDdRuALN43hBUqWE', 0),
(6, 'Jean', 'Dupont', 'jean.dupont@gmail.com', 'JPdu59', '$argon2i$v=19$m=65536,t=5,p=1$oH8H9ga/FfvciTjmvJIXHA5bfOg4Gkdd/oCX9rmeKodLflqrIVlCpZQnD07AKvw0/0f/yv2NGvhICutwSJgnEewmxTyvpO/KsZZblWC1H69XSoqra5n9sgbxTN7T2VhYsrg8KDtJBGvJqvwDSW1nuMGHebSzrVIsSSdk9H7XRcA$JVNigVCKVNAN/Lj0uX5V+bK5wlE5VJ6S4EN1v/RJEjJtvvbn30qOYmUnKLrZ3kpVTatF6aTGTQBCv3kMjQffoP/Fts0RFCTWLDXBTiM5a+0JVwXfHiaeVg9H4mOPLbx9NLy7JDlp14u1mFcy6TBDtf/vd2zI3rEGT5H4qYk8dVM', 0);

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `film`
--
ALTER TABLE `film`
  ADD PRIMARY KEY (`idFilm`),
  ADD KEY `FILM_GENRE_FK` (`idGenre`);

--
-- Index pour la table `genre`
--
ALTER TABLE `genre`
  ADD PRIMARY KEY (`idGenre`);

--
-- Index pour la table `preferer`
--
ALTER TABLE `preferer`
  ADD PRIMARY KEY (`idFilm`,`idUtilisateur`),
  ADD KEY `Preferer_UTILISATEUR_FK` (`idUtilisateur`);

--
-- Index pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  ADD PRIMARY KEY (`idUtilisateur`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `film`
--
ALTER TABLE `film`
  MODIFY `idFilm` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT pour la table `genre`
--
ALTER TABLE `genre`
  MODIFY `idGenre` int(2) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  MODIFY `idUtilisateur` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `film`
--
ALTER TABLE `film`
  ADD CONSTRAINT `FILM_GENRE_FK` FOREIGN KEY (`idGenre`) REFERENCES `genre` (`idGenre`);

--
-- Contraintes pour la table `preferer`
--
ALTER TABLE `preferer`
  ADD CONSTRAINT `Preferer_FILM_FK` FOREIGN KEY (`idFilm`) REFERENCES `film` (`idFilm`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Preferer_UTILISATEUR_FK` FOREIGN KEY (`idUtilisateur`) REFERENCES `utilisateur` (`idUtilisateur`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;
