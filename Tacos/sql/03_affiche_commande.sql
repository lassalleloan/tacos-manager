DROP PROCEDURE IF EXISTS affiche_commande;

DELIMITER //
CREATE PROCEDURE affiche_commande()
BEGIN
	SELECT commande.id, personne.nom, personne.prenom, commande.dateCommande, commande.heureCommande, tacos.nom, asso_commande_tacos.quantite, frite.nom, asso_commande_frite.quantite, boisson.nom, asso_commande_boisson.quantite, commande.prix
	FROM commande
	INNER JOIN personne
		ON commande.personne_fk = personne.id
	INNER JOIN asso_commande_frite
		ON commande.id = asso_commande_frite.commande_pk_fk
	INNER JOIN frite
		ON asso_commande_frite.frite_pk_fk = frite.id
	INNER JOIN asso_commande_boisson
		ON commande.id = asso_commande_boisson.commande_pk_fk
	INNER JOIN boisson
		ON asso_commande_boisson.boisson_pk_fk = boisson.id
	INNER JOIN asso_commande_tacos
		ON commande.id = asso_commande_tacos.commande_pk_fk
	INNER JOIN tacos
		ON asso_commande_tacos.tacos_pk_fk = tacos.id
	GROUP BY commande.id, tacos.id
    ORDER BY commande.heureCommande;
END //

-- CALL affiche_commande();