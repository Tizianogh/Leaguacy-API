<p align="center"><img width=50% src="./assets/leaguacy-logo.svg"></p>



<p align="center"><img src="https://img.shields.io/badge/Java-1.11-fdff00?style=for-the-badge&logo=Java">&nbsp;
<img src="https://img.shields.io/badge/Spring-2.6.4-fdff00?style=for-the-badge&logo=Spring">&nbsp;
<img src="https://img.shields.io/badge/MySQL-8-fdff00?style=for-the-badge&logo=Java"></p>

**Leaguacy est une plateforme offrant la possibilité de créer une équipe sportive dans l'objectif d'affronter des adversaires de niveau équivalent. Lorsque vous gagnez, votre équipe monte dans le classement. À l'inverse, quand vous perdez, vous descendez. À la fin d'une saison, les meilleures équipes gagnent des prix.**


## ⚡️ Quick start


### Prémices


Avant de commencer, il est important d'installer les outils nécessaires au bon fonctionnement du projet :
- [Java](https://www.oracle.com/fr/java/technologies/javase/jdk11-archive-downloads.html) Nous aurons besoin de **Java 11** pour faire fonctionner Spring
- [Docker](https://www.docker.com/) Nous utiliserons docker pour conteneuriser notre base de données - MySQL


### ✅ Premier lancement


Suivez les instructions ci-dessous pour mettre en place l'environnement adéquat au bon lancement du projet :

```sh
mkdir Leaguacy
cd Leaguacy
git clone https://github.com/Tizianogh/Leaguacy-API.git
```

Ouvrez le projet avec votre IDE préféré. Configurer la version de [Java](https://www.oracle.com/fr/java/technologies/javase/jdk11-archive-downloads.html) du projet si ce n'est pas fait automatiquement. Si le projet ne compile pas, exécutez la commande suivante à la racine : ``./gradlew build
``.


### 🐳 Dockerisation de la base de données


Dirigez-vous dans le répertoire docker ``` cd docker```.

Exécutez la commande ```docker-compose up -d```, puis, 
```docker exec -it mysql_leaguacy bash```.

Par défaut, le mot de passe pour entrer dans MySQL est ```root```. Il est modifiable directement dans le fichier ```docker/docker-compose.yml``` dans la section ``MYSQL_ROOT_PASSWORD``.
Pour entrer dans l'instance MySQL, exécutez la commande ```mysql -uroot -proot```. La section ``-proot`` changera en fonction du mot de passe que vous auriez déterminé.

En cas d'arrêt du container, pour pouvoir le relancer, exécutez la commande ``docker start mysql_leaguacy``, puis, ``docker exec -it mysql_leaguacy bash``.


### ⚙ Configuration application.properties️


Un fichier ```src/main/ressources/application.template.properties``` est mis à disposition. Ce fichier sert à configurer les accès à la base de données de votre environnement local.
Pour que spring interprète le fichier, il est important de retirer le  ``.template.`` de l'extension du fichier. Pour cela, dupliquez le en retirant l'extension template. Après cette modification, le nom du fichier doit ressembler à cela : ``application.properties``.

Dans le fichier, il est important de mettre vos valeurs dans les champs ``spring.datasource.username=[USERNAME]`` et ``spring.datasource.password=[PASSWORD]``. Les valeurs correspondent à l'utilisateur que vous avez créé pour votre de base de données ainsi que le mot de passe si vous l'avez changé. Par défaut, les deux valeurs sont ``root``.


### 🏁 Lancement du projet


Pour lancer le projet, exécutez la commande ```./gradlew bootRun```.
