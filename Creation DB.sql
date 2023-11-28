DROP TABLE IF EXISTS Tache_Effectuee;
DROP TABLE IF EXISTS Employe;
DROP TABLE IF EXISTS  Role;
DROP TABLE IF EXISTS Liste_Tache;
DROP TABLE IF EXISTS Dossier;
DROP TABLE IF EXISTS Tache_Dossier;
DROP TABLE IF EXISTS PersonneContact;
DROP TABLE IF EXISTS ClientIndividu;
DROP TABLE IF EXISTS ClientCompagnie;
DROP TABLE IF EXISTS Client;


CREATE TABLE PersonneContact(
  id int NOT NULL,
  nom varchar(255),
  prenom varchar(255),
  courriel varchar(255),
  numero_telephone varchar(15),
  primary key (id)
);

CREATE TABLE Client (
    id int NOT NULL,
    nom varchar(255),
    prenom varchar(255),
    courriel varchar(255),
    tel varchar(255),
    code_postal varchar(255),
    adresse varchar(255),
    pays varchar(255),
    province varchar(255),
    PRIMARY KEY (id)
);

CREATE TABLE ClientIndividu(
   id int NOT NULL,
   nom varchar(255),
   prenom varchar(255),
   courriel varchar(255),
   numero_telephone varchar(15),
   id_client int NOT NULL,
   PRIMARY KEY (id),
   CONSTRAINT fk_client_individu FOREIGN KEY (id_client) REFERENCES Client(id)
);

CREATE TABLE ClientCompagnie(
    id int NOT NULL,
    nom_compagnie VARCHAR(255),
    numero_compagnie INT,
    personne_contact INT,
    id_client INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_client_compagnie FOREIGN KEY (id_client) REFERENCES  Client(id),
    CONSTRAINT fk_personne_contact FOREIGN KEY (personne_contact) REFERENCES PersonneContact(id)
);

CREATE TABLE Dossier (
    id int NOT NULL,
    id_client int NOT NULL,
    nom varchar(255),
    PRIMARY KEY (id),
    CONSTRAINT fk_client FOREIGN KEY (id_client) REFERENCES Client(id)
);

CREATE TABLE Liste_Tache(
    id int NOT NULL,
    nom varchar(255),
    taux_horraire float(6,2),
    PRIMARY KEY (id)
);

CREATE TABLE Tache_Dossier (
    id int NOT NULL,
    id_dossier int NOT NULL,
    id_tache int NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_dossier FOREIGN KEY (id_dossier) REFERENCES Dossier(id),
    CONSTRAINT fk_liste_tache FOREIGN KEY (id_tache) REFERENCES Liste_Tache(id)
 );

 CREATE TABLE Role (
    id int NOT NULL,
    nom varchar(255),
    PRIMARY KEY (id)
 );

 CREATE TABLE Employe (
    id int NOT NULL,
    nom varchar(255),
    prenom varchar(255),
    mdp varchar(255),
    id_role int,
    courriel varchar(255),
    PRIMARY KEY (id),
    CONSTRAINT fk_role FOREIGN KEY (id_role) REFERENCES Role(id)
 );

CREATE TABLE Tache_Effectuee(
    id int NOT NULL,
    id_tache int NOT NULL,
    id_employe int NOT NULL,
    date datetime,
    heure_debut datetime,
    heure_fin datetime,
    taux_horraire float(6,2),
    PRIMARY KEY (id),
    CONSTRAINT fk_tache FOREIGN KEY (id_tache) REFERENCES Tache_Dossier(id),
    CONSTRAINT fk_employe FOREIGN KEY (id_employe) REFERENCES Employe(id)
);


