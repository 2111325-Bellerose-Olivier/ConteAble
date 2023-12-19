DROP TABLE IF EXISTS Personne_Contact;
DROP TABLE IF EXISTS Client_Individu;
DROP TABLE IF EXISTS Client;
DROP TABLE IF EXISTS Tache_Dossier;
DROP TABLE IF EXISTS Employe;
DROP TABLE IF EXISTS Role;
DROP TABLE IF EXISTS Liste_Tache;
DROP TABLE IF EXISTS Dossier;
DROP TABLE IF EXISTS Tache_Dossier;
DROP TABLE IF EXISTS List_Tache;
DROP TABLE IF EXISTS Dossier;
DROP TABLE IF EXISTS Client;

CREATE TABLE Personne_Contact(
  id integer PRIMARY KEY AUTOINCREMENT,
  nom varchar(255),
  prenom varchar(255),
  courriel varchar(255),
  numero_telephone varchar(15)
);

CREATE TABLE Client (
  id integer PRIMARY KEY AUTOINCREMENT,
  adresse_civil varchar(255),
  code_postal varchar(255),
  ville varchar(255),
  province varchar(255),
  pays varchar(255)
);

CREATE TABLE Client_Individu(
  id integer PRIMARY KEY AUTOINCREMENT,
  nom varchar(255),
  prenom varchar(255),
  courriel varchar(255),
  numero_telephone varchar(15),
  id_client int,
  CONSTRAINT fk_client_individu FOREIGN KEY (id_client) REFERENCES Client(id)
);

CREATE TABLE Client_Compagnie(
  id integer PRIMARY KEY AUTOINCREMENT,
  nom_compagnie VARCHAR(255),
  numero_compagnie INT,
  personne_contact INT,
  id_client INT,
  CONSTRAINT fk_client_compagnie FOREIGN KEY (id_client) REFERENCES Client(id),
  CONSTRAINT fk_personne_contact FOREIGN KEY (personne_contact) REFERENCES PersonneContact(id)
);

CREATE TABLE Dossier (
  id integer PRIMARY KEY AUTOINCREMENT,
  id_client int NOT NULL,
  nom varchar(255),
  CONSTRAINT fk_client FOREIGN KEY (id_client) REFERENCES Client(id)
);

CREATE TABLE Liste_Tache(
  id integer PRIMARY KEY AUTOINCREMENT,
  nom varchar(255),
  taux_horraire float(6,2)
  );

CREATE TABLE Role (
  id integer PRIMARY KEY AUTOINCREMENT,
  nom varchar(255)
);
INSERT INTO Role(nom) VALUES ("Superviseur");

CREATE TABLE Employe (
  id integer PRIMARY KEY AUTOINCREMENT,
  nom varchar(255),
  prenom varchar(255),
  mdp varchar(255),
  id_role int,
  courriel varchar(255),
  CONSTRAINT fk_role FOREIGN KEY (id_role) REFERENCES Role(id)
);

CREATE TABLE Tache_Dossier (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  id_dossier INTEGER,
  id_employe INTEGER,
  nom_tache varchar(255),
  duree time,
  montant float(6,2),
  CONSTRAINT fk_dossier FOREIGN KEY (id_dossier) REFERENCES Dosser(id),
  CONSTRAINT fk_employe FOREIGN KEY (id_employe) REFERENCES Employe(id)
);