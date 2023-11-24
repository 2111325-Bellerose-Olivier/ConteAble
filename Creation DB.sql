DROP TABLE IF EXISTS Tache_Effectuee;
DROP TABLE IF EXISTS Employe;
DROP TABLE IF EXISTS Role;
DROP TABLE IF EXISTS Tache_Dossier;
DROP TABLE IF EXISTS List_Tache;
DROP TABLE IF EXISTS Dossier;
DROP TABLE IF EXISTS Client;

CREATE TABLE Client (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nom varchar(255),
    prenom varchar(255),
    courriel varchar(255),
    tel varchar(255),
    code_postal varchar(255),
    adresse varchar(255),
    pays varchar(255),
    province varchar(255)
);

CREATE TABLE Dossier (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_client INTEGER,
    nom varchar(255),
    CONSTRAINT fk_client FOREIGN KEY (id_client) REFERENCES Client(id)
);

CREATE TABLE List_Tache (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nom varchar(255),
    taux_horraire float(6,2)
);

CREATE TABLE Tache_Dossier (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_dossier INTEGER,
    id_tache INTEGER,
    CONSTRAINT fk_dossier FOREIGN KEY (id_dossier) REFERENCES Dosser(id),
    CONSTRAINT fk_liste_tache FOREIGN KEY (id_tache) REFERENCES List_Tache(id)
);

CREATE TABLE Role (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nom varchar(255)
);
INSERT INTO Role(nom) VALUES ("Superviseur");

CREATE TABLE Employe (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nom varchar(255),
    prenom varchar(255),
    mdp varchar(255),
    id_role INTEGER,
    courriel varchar(255),
    CONSTRAINT fk_role FOREIGN KEY (id_role) REFERENCES Role(id)
);

CREATE TABLE Tache_Effectuee(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_tache INTEGER,
    id_employe INTEGER,
    date datetime,
    heure_debut datetime,
    heure_fin datetime,
    taux_horraire float(6,2),
    CONSTRAINT fk_tache FOREIGN KEY (id_tache) REFERENCES Tache_Dossier(id),
    CONSTRAINT fk_employe FOREIGN KEY (id_employe) REFERENCES Employe(id)
);