DROP SCHEMA IF EXISTS los_tacos;
CREATE SCHEMA los_tacos CHARACTER SET utf8 COLLATE utf8_general_ci;
USE los_tacos;

CREATE TABLE viande (
	id INT(10) AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
	provenance VARCHAR(32) NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE sauce (
	id INT(10) AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
	puissance INT(3) NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE ingredient (
	id INT(10) AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
	provenance VARCHAR(32) NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE tacos (
	id INT(10) AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    prix DOUBLE NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE asso_viande_tacos (
	viande_pk_fk INT(10),
    tacos_pk_fk INT(10),
    PRIMARY KEY (viande_pk_fk, tacos_pk_fk),
    FOREIGN KEY (viande_pk_fk) REFERENCES viande (id) ON DELETE CASCADE,
    FOREIGN KEY (tacos_pk_fk) REFERENCES tacos (id) ON DELETE CASCADE
);

CREATE TABLE asso_sauce_tacos (
	sauce_pk_fk INT(10),
    tacos_pk_fk INT(10),
    PRIMARY KEY (sauce_pk_fk, tacos_pk_fk),
    FOREIGN KEY (sauce_pk_fk) REFERENCES sauce (id) ON DELETE CASCADE,
    FOREIGN KEY (tacos_pk_fk) REFERENCES tacos (id) ON DELETE CASCADE
);

CREATE TABLE asso_ingredient_tacos (
	ingredient_pk_fk INT(10),
    tacos_pk_fk INT(10),
    PRIMARY KEY (ingredient_pk_fk, tacos_pk_fk),
    FOREIGN KEY (ingredient_pk_fk) REFERENCES ingredient (id) ON DELETE CASCADE,
    FOREIGN KEY (tacos_pk_fk) REFERENCES tacos (id) ON DELETE CASCADE
);

CREATE TABLE boisson (
	id INT(10) AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    decilitres INT(4) NOT NULL,
    prix DOUBLE NOT NULL,
    PRIMARY KEY (id)
    
);

CREATE TABLE frite (
	id INT(10) AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    portion INT(10) NOT NULL, -- en grammes
    prix DOUBLE NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE rolePersonne (
	id INT(4) AUTO_INCREMENT,
	nom VARCHAR(32) NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE personne (
	id INT(12) AUTO_INCREMENT,
    nom VARCHAR(32) NOT NULL,
    prenom VARCHAR(32) NOT NULL,
    telephone VARCHAR(32),
    mail VARCHAR(32),
    login VARCHAR(16) NOT NULL,
    mdp VARCHAR(64) NOT NULL, -- SHA-256
    rolePersonne_fk INT(4) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (rolePersonne_fk) REFERENCES rolePersonne (id)
);

CREATE TABLE commande (
	id INT(20) AUTO_INCREMENT,
    dateCommande CHAR(10),
    heureCommande CHAR(5),
    prix DOUBLE NOT NULL DEFAULT 0.0,
    personne_fk INT(12) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (personne_fk) REFERENCES personne (id)
);


CREATE TABLE asso_commande_tacos (
    commande_pk_fk INT(20),
    tacos_pk_fk INT(10),
    quantite INT(7) NOT NULL DEFAULT 1,
    PRIMARY KEY (commande_pk_fk, tacos_pk_fk),
    FOREIGN KEY (commande_pk_fk) REFERENCES commande (id),
    FOREIGN KEY (tacos_pk_fk) REFERENCES tacos (id) ON DELETE CASCADE
);

CREATE TABLE asso_commande_boisson (
    commande_pk_fk INT(20),
    boisson_pk_fk INT(10),
    quantite INT(7) NOT NULL DEFAULT 1,
    PRIMARY KEY (commande_pk_fk, boisson_pk_fk),
    FOREIGN KEY (commande_pk_fk) REFERENCES commande (id),
    FOREIGN KEY (boisson_pk_fk) REFERENCES boisson (id) ON DELETE CASCADE
);

CREATE TABLE asso_commande_frite (
    commande_pk_fk INT(20),
    frite_pk_fk INT(10),
    quantite INT(7) NOT NULL DEFAULT 1,
    PRIMARY KEY (commande_pk_fk, frite_pk_fk),
    FOREIGN KEY (commande_pk_fk) REFERENCES commande (id),
    FOREIGN KEY (frite_pk_fk) REFERENCES frite (id) ON DELETE CASCADE
);