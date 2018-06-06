USE los_tacos;

-- ================================================================================

INSERT INTO viande (nom, provenance)
VALUES ("Boeuf", "Argentine");

INSERT INTO viande (nom, provenance)
VALUES ("Poulet", "Suisse");

INSERT INTO viande (nom, provenance)
VALUES ("Agneau", "Suisse");

INSERT INTO viande (nom, provenance)
VALUES ("Veau", "Suisse");

INSERT INTO viande (nom, provenance)
VALUES ("Merguez agneau", "Canada");

INSERT INTO sauce (nom, puissance)
VALUES ("Algérienne", "2");

INSERT INTO sauce (nom, puissance)
VALUES ("Marocaine", "2");

INSERT INTO sauce (nom, puissance)
VALUES ("Samouraï", "3");

INSERT INTO sauce (nom, puissance)
VALUES ("Mayonnaise", "1");

INSERT INTO sauce (nom, puissance)
VALUES ("Moutarde de Dijon", "2");

INSERT INTO sauce (nom, puissance)
VALUES ("Ketchup", "1");

INSERT INTO sauce (nom, puissance)
VALUES ("Biggy", "1");

-- ================================================================================

INSERT INTO ingredient (nom, provenance)
VALUES ("Maïs", "Suisse");

INSERT INTO ingredient (nom, provenance)
VALUES ("Salade", "France");

-- ================================================================================

INSERT INTO tacos (nom, prix)
VALUES ("Poulet samouraï", 7.0);

INSERT INTO tacos (nom, prix)
VALUES ("Poulet marocaine", 7.0);

INSERT INTO tacos (nom, prix)
VALUES ("Poulet mayo ketchup", 7.5);

INSERT INTO tacos (nom, prix)
VALUES ("Boeuf moutarde de Dijon", 7.0);

INSERT INTO tacos (nom, prix)
VALUES ("Boeuf mayo ketchup", 7.5);

INSERT INTO tacos (nom, prix)
VALUES ("Agneau samouraï", 7.0);

INSERT INTO tacos (nom, prix)
VALUES ("Veau biggy", 7.0);

INSERT INTO tacos (nom, prix)
VALUES ("Veau mayo", 7.0);

INSERT INTO tacos (nom, prix)
VALUES ("Veau ketchup", 7.0);

INSERT INTO tacos (nom, prix)
VALUES ("Veau mayo ketchup avec maïs", 8.0);

INSERT INTO tacos (nom, prix)
VALUES ("Boeuf merguez agneau samouraï marocaine avec salade et maïs", 8.7);

-- ================================================================================

INSERT INTO asso_viande_tacos (viande_pk_fk, tacos_pk_fk)
VALUES (
	(SELECT id
    FROM viande
    WHERE nom = "Poulet"),
    (SELECT id
    FROM tacos
    WHERE nom = "Poulet samouraï")
);

INSERT INTO asso_viande_tacos (viande_pk_fk, tacos_pk_fk)
VALUES (
	(SELECT id
    FROM viande
    WHERE nom = "Poulet"),
    (SELECT id
    FROM tacos
    WHERE nom = "Poulet marocaine")
);

INSERT INTO asso_viande_tacos (viande_pk_fk, tacos_pk_fk)
VALUES (
	(SELECT id
    FROM viande
    WHERE nom = "Poulet"),
    (SELECT id
    FROM tacos
    WHERE nom = "Poulet mayo ketchup")
);

INSERT INTO asso_viande_tacos (viande_pk_fk, tacos_pk_fk)
VALUES (
	(SELECT id
    FROM viande
    WHERE nom = "Boeuf"),
    (SELECT id
    FROM tacos
    WHERE nom = "Boeuf moutarde de dijon")
);

INSERT INTO asso_viande_tacos (viande_pk_fk, tacos_pk_fk)
VALUES (
	(SELECT id
    FROM viande
    WHERE nom = "Boeuf"),
    (SELECT id
    FROM tacos
    WHERE nom = "Boeuf mayo ketchup")
);

INSERT INTO asso_viande_tacos (viande_pk_fk, tacos_pk_fk)
VALUES (
	(SELECT id
    FROM viande
    WHERE nom = "Agneau"),
    (SELECT id
    FROM tacos
    WHERE nom = "Agneau samouraï")
);

INSERT INTO asso_viande_tacos (viande_pk_fk, tacos_pk_fk)
VALUES (
	(SELECT id
    FROM viande
    WHERE nom = "Veau"),
    (SELECT id
    FROM tacos
    WHERE nom = "Veau biggy")
);

INSERT INTO asso_viande_tacos (viande_pk_fk, tacos_pk_fk)
VALUES (
	(SELECT id
    FROM viande
    WHERE nom = "Veau"),
    (SELECT id
    FROM tacos
    WHERE nom = "Veau mayo")
);

INSERT INTO asso_viande_tacos (viande_pk_fk, tacos_pk_fk)
VALUES (
	(SELECT id
    FROM viande
    WHERE nom = "Veau"),
    (SELECT id
    FROM tacos
    WHERE nom = "Veau ketchup")
);

INSERT INTO asso_viande_tacos (viande_pk_fk, tacos_pk_fk)
VALUES (
	(SELECT id
    FROM viande
    WHERE nom = "Veau"),
    (SELECT id
    FROM tacos
    WHERE nom = "Veau mayo ketchup avec maïs")
);

INSERT INTO asso_viande_tacos (viande_pk_fk, tacos_pk_fk)
VALUES (
	(SELECT id
    FROM viande
    WHERE nom = "Boeuf"),
    (SELECT id
    FROM tacos
    WHERE nom = "Boeuf merguez agneau samouraï marocaine avec salade et maïs")
);

INSERT INTO asso_viande_tacos (viande_pk_fk, tacos_pk_fk)
VALUES (
	(SELECT id
    FROM viande
    WHERE nom = "Merguez agneau"),
    (SELECT id
    FROM tacos
    WHERE nom = "Boeuf merguez agneau samouraï marocaine avec salade et maïs")
);

-- ================================================================================

INSERT INTO asso_sauce_tacos (sauce_pk_fk, tacos_pk_fk)
VALUES (
	(SELECT id
    FROM sauce
    WHERE nom = "Samouraï"),
    (SELECT id
    FROM tacos
    WHERE nom = "Poulet samouraï")
);

INSERT INTO asso_sauce_tacos (sauce_pk_fk, tacos_pk_fk)
VALUES (
	(SELECT id
    FROM sauce
    WHERE nom = "Samouraï"),
    (SELECT id
    FROM tacos
    WHERE nom = "Agneau samouraï")
);

INSERT INTO asso_sauce_tacos (sauce_pk_fk, tacos_pk_fk)
VALUES (
	(SELECT id
    FROM sauce
    WHERE nom = "Samouraï"),
    (SELECT id
    FROM tacos
    WHERE nom = "Boeuf merguez agneau samouraï marocaine avec salade et maïs")
);

INSERT INTO asso_sauce_tacos (sauce_pk_fk, tacos_pk_fk)
VALUES (
	(SELECT id
    FROM sauce
    WHERE nom = "Marocaine"),
    (SELECT id
    FROM tacos
    WHERE nom = "Poulet marocaine")
);

INSERT INTO asso_sauce_tacos (sauce_pk_fk, tacos_pk_fk)
VALUES (
	(SELECT id
    FROM sauce
    WHERE nom = "Marocaine"),
    (SELECT id
    FROM tacos
    WHERE nom = "Boeuf merguez agneau samouraï marocaine avec salade et maïs")
);

INSERT INTO asso_sauce_tacos (sauce_pk_fk, tacos_pk_fk)
VALUES (
	(SELECT id
    FROM sauce
    WHERE nom = "Moutarde de Dijon"),
    (SELECT id
    FROM tacos
    WHERE nom = "Boeuf moutarde de Dijon")
);

INSERT INTO asso_sauce_tacos (sauce_pk_fk, tacos_pk_fk)
VALUES (
	(SELECT id
    FROM sauce
    WHERE nom = "Mayonnaise"),
    (SELECT id
    FROM tacos
    WHERE nom = "Poulet mayo ketchup")
);

INSERT INTO asso_sauce_tacos (sauce_pk_fk, tacos_pk_fk)
VALUES (
	(SELECT id
    FROM sauce
    WHERE nom = "Mayonnaise"),
    (SELECT id
    FROM tacos
    WHERE nom = "Boeuf mayo ketchup")
);

INSERT INTO asso_sauce_tacos (sauce_pk_fk, tacos_pk_fk)
VALUES (
	(SELECT id
    FROM sauce
    WHERE nom = "Mayonnaise"),
    (SELECT id
    FROM tacos
    WHERE nom = "Veau mayo")
);

INSERT INTO asso_sauce_tacos (sauce_pk_fk, tacos_pk_fk)
VALUES (
	(SELECT id
    FROM sauce
    WHERE nom = "Mayonnaise"),
    (SELECT id
    FROM tacos
    WHERE nom = "Veau mayo ketchup avec maïs")
);

INSERT INTO asso_sauce_tacos (sauce_pk_fk, tacos_pk_fk)
VALUES (
	(SELECT id
    FROM sauce
    WHERE nom = "Ketchup"),
    (SELECT id
    FROM tacos
    WHERE nom = "Poulet mayo ketchup")
);

INSERT INTO asso_sauce_tacos (sauce_pk_fk, tacos_pk_fk)
VALUES (
	(SELECT id
    FROM sauce
    WHERE nom = "Ketchup"),
    (SELECT id
    FROM tacos
    WHERE nom = "Boeuf mayo ketchup")
);

INSERT INTO asso_sauce_tacos (sauce_pk_fk, tacos_pk_fk)
VALUES (
	(SELECT id
    FROM sauce
    WHERE nom = "Ketchup"),
    (SELECT id
    FROM tacos
    WHERE nom = "Veau ketchup")
);

INSERT INTO asso_sauce_tacos (sauce_pk_fk, tacos_pk_fk)
VALUES (
	(SELECT id
    FROM sauce
    WHERE nom = "Ketchup"),
    (SELECT id
    FROM tacos
    WHERE nom = "Veau mayo ketchup avec maïs")
);

INSERT INTO asso_sauce_tacos (sauce_pk_fk, tacos_pk_fk)
VALUES (
	(SELECT id
    FROM sauce
    WHERE nom = "Biggy"),
    (SELECT id
    FROM tacos
    WHERE nom = "Veau biggy")
);

-- ================================================================================

INSERT INTO asso_ingredient_tacos (ingredient_pk_fk, tacos_pk_fk)
VALUES (
	(SELECT id
    FROM ingredient
    WHERE nom = "Maïs"),
    (SELECT id
    FROM tacos
    WHERE nom = "Veau mayo ketchup avec maïs")
);

INSERT INTO asso_ingredient_tacos (ingredient_pk_fk, tacos_pk_fk)
VALUES (
	(SELECT id
    FROM ingredient
    WHERE nom = "Maïs"),
    (SELECT id
    FROM tacos
    WHERE nom = "Boeuf merguez agneau samouraï marocaine avec salade et maïs")
);

INSERT INTO asso_ingredient_tacos (ingredient_pk_fk, tacos_pk_fk)
VALUES (
	(SELECT id
    FROM ingredient
    WHERE nom = "Salade"),
    (SELECT id
    FROM tacos
    WHERE nom = "Boeuf merguez agneau samouraï marocaine avec salade et maïs")
);

-- ================================================================================

INSERT INTO boisson (nom, decilitres, prix)
VALUES ("Coca Zéro 5DL", 5, 3.0);

INSERT INTO boisson (nom, decilitres, prix)
VALUES ("Coca Zéro en canette", 3.3, 2.0);

INSERT INTO boisson (nom, decilitres, prix)
VALUES ("Coca 5DL", 5, 3.0);

INSERT INTO boisson (nom, decilitres, prix)
VALUES ("Coca en canette", 3.3, 2.0);

INSERT INTO boisson (nom, decilitres, prix)
VALUES ("Henniez bleue", 3.3, 1.5);

INSERT INTO boisson (nom, decilitres, prix)
VALUES ("Henniez verte", 3.3, 1.5);

INSERT INTO boisson (nom, decilitres, prix)
VALUES ("Henniez rouge", 3.3, 1.5);

INSERT INTO boisson (nom, decilitres, prix)
VALUES ("Valser", 3.3, 1.8);

-- ================================================================================

INSERT INTO frite (nom, portion, prix)
VALUES ("Frites 250gr", 250, 1.0);

INSERT INTO frite (nom, portion, prix)
VALUES ("Frites 500gr", 500, 1.8);

INSERT INTO frite (nom, portion, prix)
VALUES ("Frites 1kg", 1000, 3.5);

INSERT INTO frite (nom, portion, prix)
VALUES ("Potatoes 250gr", 250, 1.2);

INSERT INTO frite (nom, portion, prix)
VALUES ("Potatoes 500gr", 500, 2.1);

INSERT INTO frite (nom, portion, prix)
VALUES ("Potatoes 1kg", 1000, 4.0);

-- ================================================================================

INSERT INTO rolePersonne (nom)
VALUES ("Administrateur");

INSERT INTO rolePersonne (nom)
VALUES ("Client");

-- ================================================================================

INSERT INTO personne (nom, prenom, telephone, mail, mdp, rolePersonne_fk)
VALUES ("Marco", "Adrien", "0792651478", "adrien.marco@heig-vd.ch", "Burrito4ever!", 
	(SELECT id
    FROM rolePersonne
    WHERE nom = "Administrateur")
);

INSERT INTO personne (nom, prenom, mail, mdp, rolePersonne_fk)
VALUES ("Lassalle", "Loan", "loan.lassalle@heig-vd.ch", "Tacos4ever!", 
	(SELECT id
    FROM rolePersonne
    WHERE nom = "Client")
);

INSERT INTO personne (nom, prenom, mail, mdp, rolePersonne_fk)
VALUES ("Brêchet", "Julien", "julien.brechet@heig-vd.ch", "Miam4ever!", 
	(SELECT id
    FROM rolePersonne
    WHERE nom = "Client")
);

-- ================================================================================

INSERT INTO commande (dateCommande, heureCommande, prix, personne_fk)
VALUES ("2018-05-09", "12:55", 33.9,
	(SELECT id
    FROM personne
    WHERE mail = "loan.lassalle@heig-vd.ch")
);

INSERT INTO commande (dateCommande, heureCommande, prix, personne_fk)
VALUES ("2018-05-09", "11:55", 11.8,
	(SELECT id
    FROM personne
    WHERE mail = "julien.brechet@heig-vd.ch")
);

INSERT INTO commande (dateCommande, heureCommande, prix, personne_fk)
VALUES ("2018-05-09", "13:50", 11.8,
	(SELECT id
    FROM personne
    WHERE mail = "loan.lassalle@heig-vd.ch")
);

-- ================================================================================

INSERT INTO asso_commande_tacos (commande_pk_fk, tacos_pk_fk, quantite)
VALUES (1,
	(SELECT id
    FROM tacos
    WHERE nom = "Veau mayo ketchup avec maïs"),
    1
);

INSERT INTO asso_commande_tacos (commande_pk_fk, tacos_pk_fk, quantite)
VALUES (1,
	(SELECT id
    FROM tacos
    WHERE nom = "Boeuf merguez agneau samouraï marocaine avec salade et maïs"),
    2
);

INSERT INTO asso_commande_boisson (commande_pk_fk, boisson_pk_fk, quantite)
VALUES (1,
	(SELECT id
    FROM boisson
    WHERE nom = "Henniez rouge"),
    3
);

INSERT INTO asso_commande_frite (commande_pk_fk, frite_pk_fk, quantite)
VALUES (1,
	(SELECT id
    FROM frite
    WHERE nom = "Potatoes 1kg"),
    1
);

-- ================================================================================

INSERT INTO asso_commande_tacos (commande_pk_fk, tacos_pk_fk, quantite)
VALUES (2,
	(SELECT id
    FROM tacos
    WHERE nom = "Veau biggy"),
    1
);

INSERT INTO asso_commande_boisson (commande_pk_fk, boisson_pk_fk, quantite)
VALUES (2,
	(SELECT id
    FROM boisson
    WHERE nom = "Henniez verte"),
    2
);

INSERT INTO asso_commande_frite (commande_pk_fk, frite_pk_fk, quantite)
VALUES (2,
	(SELECT id
    FROM frite
    WHERE nom = "Frites 500gr"),
    1
);

-- ================================================================================

-- ================================================================================

INSERT INTO asso_commande_tacos (commande_pk_fk, tacos_pk_fk, quantite)
VALUES (3,
	(SELECT id
    FROM tacos
    WHERE nom = "Veau biggy"),
    1
);

INSERT INTO asso_commande_boisson (commande_pk_fk, boisson_pk_fk, quantite)
VALUES (3,
	(SELECT id
    FROM boisson
    WHERE nom = "Henniez verte"),
    2
);

INSERT INTO asso_commande_frite (commande_pk_fk, frite_pk_fk, quantite)
VALUES (3,
	(SELECT id
    FROM frite
    WHERE nom = "Frites 500gr"),
    1
);