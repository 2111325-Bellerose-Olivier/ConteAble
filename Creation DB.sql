DROP TABLE IF EXISTS Tache_Dossier;
DROP TABLE IF EXISTS Employe;
DROP TABLE IF EXISTS Role;
DROP TABLE IF EXISTS Liste_Tache;
DROP TABLE IF EXISTS Dossier;
DROP TABLE IF EXISTS Client;

CREATE TABLE Client (
  id integer PRIMARY KEY AUTOINCREMENT,
  adresse_civil varchar(255),
  code_postal varchar(255),
  ville varchar(255),
  province varchar(255),
  pays varchar(255),

  nom varchar(255),
  prenom varchar(255),
  courriel varchar(255),
  numero_telephone varchar(15)
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
  debut timestamp,
  duree time,
  montant float(6,2),
  CONSTRAINT fk_dossier FOREIGN KEY (id_dossier) REFERENCES Dossier(id),
  CONSTRAINT fk_employe FOREIGN KEY (id_employe) REFERENCES Employe(id)
);

INSERT INTO Role(nom) VALUES ("Superviseur");
INSERT INTO Role(nom) VALUES ("Employe");

INSERT INTO Client(adresse_civil,  code_postal, ville, province, pays, nom, prenom, courriel, numero_telephone) VALUES ("123 9 Avenue", "A1B 2C3", "Victoriaville", "Québec", "Canada", "Le petit curieux", "George", "banane@gmail.com", "123-456-7890");

INSERT INTO Liste_Tache(nom, taux_horraire) VALUES ("Calculation temps", 20.50);
INSERT INTO Dossier(id_client, nom) VALUES (1, "Entreprise principale");

INSERT INTO Employe(nom, prenom, mdp, id_role, courriel) VALUES ("test", "test", "$2a$10$48e9p/yZH3fvSMLypKiRseW/bTfXMK55N7PKBV9PiwcgUrtqgYWBC", 1, "test");