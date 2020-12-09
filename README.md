# Site-Viewit

## Step 0 : Avant utilisation

### Objectif du site

Ce site internet permet à un utilisateur de rechercher des films, d'en ajouter en favoris et de les noter.

## Step 1 : Initialisation

### 1.1 Lancer le projet à partir de Eclipse ou IntellJ

#### 1. Télécharger le projet

Cloner ce projet : https://gitlab.com/hei-projet/hei-projet-2020/site-viewit.git.

#### 2. Importer le projet dans Eclipse ou IntellJ (Projet Maven)

Ouvrir le projet avec un des logiciels.
Vérifier que le 'context root' est bien "/" et non "/viewit".

#### 3. Initialisation de la base de donnée.

#### 4. Initialiser le fichier "images.properties"

##### 4.1. Pour le lancement du projet

Se rendre dans le fichier "src/main/ressources/images.properties".
Remplacer le chemin d'accés par votre propre chemin d'accés : "{your way}/data/filmImages".

Faire de mêmes pour le fichier "src/test/ressources/images.properties".
Remplacer le chemin d'accés par votre propre chemin d'accés : "{your way}/test/dataTest".

Faire de même dans le fichier "src/main/test/ressources/images.properties".
Le chemin : "{your way}/src/test/resources/dataTest".

### 1.2 Lancer le projet avec Docker

Nous avons mis en place Docker pour lancer notre site sur n'importe quel poste avec plus de facilitée.
Notre fichier Docker-compose permet de lancer notre projet à partir d'une Image Tomcat et d'une image MariaDB.
Pour lancer le projet sur Docker sans utiliser l'image crée à partir de gitlab-ci,
il faut remplacer la ligne 5 du fichier docker-compose.yml (image: registry.gitlab.com/hei-projet/hei-projet-2020/site-viewit  -- par --  build: .    )
Pour finir, sur GitBash ou autre console de commande, se placer à la racine du projet puis lancer la commande "docker-compose up".

### 1.3 Lancer le projet avec Docker à partir de l'image GitLab-ci

Nous avons mis en place gitLab-CI pour notre projet afin de faire une vérification des tests après chaque push.
La mise en place de cette CI nous a également permis de packager notre projet et de produire une image que l'on pourra ensuite utiliser avec Docker.
il faut remplacer la ligne 5 du fichier docker-compose.yml (build: .    -- par -- image: registry.gitlab.com/hei-projet/hei-projet-2020/site-viewit)
Pour finir, sur GitBash ou autre console de commande, se placer à la racine du projet puis lancer la commande "docker-compose up".


## Step 2 : Utilisation en tant que personne non connecté

### 2.1 Rechercher un film à l'aide de son titre

En tant qu'administrateur vous avez la possibilité de rechercher un film à partir de son titre.

Taper dans la barre de recherche un film que vous voulez trouver (exemple : "the revenant").
La liste des films se réduit en gardant les films qui contiennent la lettre ou la suite de lettres dans son titre.

Si votre film est valide dans le site, celui-ci apparaitra.
Si votre suite de lettre ne correspond à aucun film, la recherche s'arrêtera à la dernière requête qui correspond à au moins un film.

### 2.2 Trier les films

Vous avez également la possibilté de trier les films par ordre alphabétique, par popularité (du meilleur au moins bon pourcentage), 
du film le plus récent au plus ancien ou l'inverse.

Sélectionner le tri que vous souhaité (exemple : par popularité) puis cliquer sur "go".
Vous devriez maintenant voir les films avec le plus de pourcentage en haut puis les autres par décroissance.

### 2.3 Sélectionner un film

Pour finir, un utilisateur non connecté à également la possibilité de voir les détails du film qui l'intéresse.

Cliquer sur l'image du film dont vous voulez trouver les informations.
Vous avez alors accès au synopsis, quelques informations sur la fiche technique du film, ainsi que la bande-annonce du film.

## Step 3 : Connection en tant qu'utilisateur

### 3.0 Connection

Dans l'onglet connection en haut à droite de l'écran, remplissez avec les informations suivantes :

Email : john.smith@junia.fr
Mot de Passe : utilisateur

### 3.1 Gestion de ses films favoris

Une fois connecté, vous avez alors la possibilité de gérer votre liste de favoris (grâce au coeur apparu en  haut à droite de chaque image).

Vous avez maintenant la possibilité d'ajouter un film à vos favoris et ainsi le retrouver dans votre liste des favoris.
Pour cela, cliquer sur "favoris" en haut de votre écran.

### 3.2 Liker ou disliker un film

Lorsque vous vous rendez sur la page détail d'un film, vous avez toujours la possibilité d'ajouter ou d'enlever un film de vos favoris.
Mais vous pouvez également liker ou disliker un film.
Le pourcentage étant calculé à partir du nombre de like et de dislike, devrait se mettre à jour.

### 3.3 Demander l'ajout d'un film

En tant qu'utilisateur vous avez égalment accès à une 3ème page permettant de demander l'ajout d'un film.
Vous pouvez alors remplir les champs du film puis enregistrer.
Votre film ne sera pas visible tout de suite car celui-ci doit d'abord être vérifié puis validé par un administrateur.

Exemple :

Titre : Avengers : Endgame

Synopsis : Le Titan Thanos ayant réussi à s'approprier les six Pierres d'Infinité et à les réunir sur le Gantelet doré, a pu réaliser son objectif de pulvériser la moitié de la population de l'Univers d'un claquement de doigts. Les quelques Avengers et Gardiens de la Galaxie ayant survécu, Steve Rogers, Thor, Natasha Romanoff, Tony Stark, Carol Danvers, Clint Barton, Bruce Banner, James Rhodes, Nébula et Rocket espèrent réparer le méfait de Thanos. Ils le retrouvent mais il s'avère que ce dernier a détruit les pierres et Thor le décapite. Cinq ans plus tard, alors que chacun essaie de continuer sa vie et d'oublier les nombreuses pertes dramatiques, Scott Lang, alias Ant-Man, parvient à s'échapper de la Dimension subatomique où il était coincé depuis la disparition du Docteur Hank Pym, de sa femme Janet Van Dyne et de sa fille Hope Van Dyne. Lang propose aux Avengers une solution pour faire revenir à la vie tous les êtres disparus, dont leurs alliés et coéquipiers : récupérer les Pierres d'Infinité dans le passé grâce à l'univers quantique. Pour ce faire, à l'aide des connaissances scientifiques de Bruce Banner et de Tony Stark, ils vont se scinder en plusieurs groupes pour partir chercher les gemmes dans diverses époques passées…

Réalisateur : Anthony et Joe Russo
Acteur : Robert Downey Jr.
Durée : 181
Date de sortie : 2019-04-24
Genre : Super-Héros

## Step 4 : Gestion du site en tant qu'administrateur

### 4.0 Connection

Email : aaron.mundo@junia.fr
Mot de Passe : administrateur

### 4.1 Un administrateur est avant tout un utilisateur

En tant qu'administrateur vous avez accès à tout ce dont l'utilisateur à accès.
La seule différence est que la page de demande d'ajout est devenu une page d'ajout de film.
L'administrateur a lui, la possibilité d'ajouter l'image du film ainsi que le lien la bande-annonce.

### 4.2 Gestion des genres

Un film est bien évidemment défini par un genre.
L'administrateur à donc la possibilité d'ajouter un genre si il le juge nécessaire.
Il peut également supprimer ceux qu'il considère inutile (les genres liés à des films ne peuvent pas être supprimé).

### 4.3 Gestion des utilisateurs

Pour finir, l'administrateur a la possibilité de gérer les informations des utilisateurs du site.
Il est le seul à pouvoir modifier les informations concernant le nom, le prénom, l'adresse-mail et le mot de passe.
De plus, seul l'administrateur à la possibilité de créer un nouvel utilisateur.

Il a également la possibilité de promouvoir un utilisateur en tant qu'administrateur ou de rétrograder un administrateur en tant qu'utilisateur.

### 4.4 Gestion des films

L'administrateur à accès à la liste des films qu'ils soient valides ou non.
A partir de cette page, l'administrateur a la possibilté de valider un film (une fois validé, le film sera visible dans la page accueil).
Il peut également modifier ou supprimer un film.

Lorsque l'administrateur souhaite modifier un film, celui-ci est alors conduit dans une nouvelle page pré-remplie avec les informations du film, dans laquelle il peut les modifier.

Exemple :

Vous pouvez alors modifier le film que vous avez créer plus tôt,

Image de couverture : (Sélectionner l'image du film dans le dossier "/data/filmImages")
Lien vers la bande-annonce : https://www.youtube.com/watch?v=wV-Q0o2OQjQ

## Auteurs
- [Kylian Barthelemot](https://gitlab.com/KBarth)
- [Quentin Callens](https://gitlab.com/quentincallens)
- [Emile Mongenet](https://gitlab.com/emilemongenet)
- [Pierre Sanchez](https://gitlab.com/mado_io)
