package com.conteabe.conteabe.dao

import com.conteabe.conteabe.modele.Employe
import com.conteabe.conteabe.service.ServiceBD
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

/**
 * DAO spécialiser pour les projets.
 *
 * @param serviceBD le service de base de données utilisé.
 *
 * @author Alexandre
 * @since 06/05/2023
 */
class EmployeDAO(serviceBD: ServiceBD) : DAOAbstraite<Employe>(serviceBD) {

    override fun enregistrer(entite: Employe) {
        val connexion = serviceBD.ouvrirConnexion()
        val estInsertion: Boolean = entite.id == null

        var requete: PreparedStatement = if (estInsertion) {
            connexion.prepareStatement(
                "INSERT INTO employe (nom, prenom, mdp, id_role, courriel) VALUES (?, ?, ?, ? ,?);",
                Statement.RETURN_GENERATED_KEYS
            )
        } else {
            connexion.prepareStatement("UPDATE employe SET nom = ?, prenom = ?, mdp = ?, id_role = ?, courriel = ?;")
        }

        requete.setString(1, entite.nom)
        requete.setString(2, entite.prenom)
        requete.setString(3, entite.mdp)
        requete.setInt(4, entite.id_role)
        requete.setString(5, entite.courriel)

        requete.executeUpdate()

        if (estInsertion) {
            val cleGenere: ResultSet = requete.generatedKeys

            if (cleGenere.next()) {
                entite.id = cleGenere.getInt(1)
            }
        }

        serviceBD.fermerConnexion()
    }


    override fun chargerTout(): MutableList<Employe> {
        val connexion = serviceBD.ouvrirConnexion()
        val requete: PreparedStatement =
            connexion.prepareStatement("SELECT id, nom, prenom, mdp, id_role, courriel FROM Employe")
        val resultats: ResultSet = requete.executeQuery()
        val employes: MutableList<Employe> = mutableListOf()

        while (resultats.next()) {
            employes.add(
                Employe(
                    resultats.getInt("id"),
                    resultats.getString("nom"),
                    resultats.getString("prenom"),
                    resultats.getString("mdp"),
                    resultats.getInt("id_role"),
                    resultats.getString("courriel")
                )
            )
        }
        serviceBD.fermerConnexion()
        return employes
    }

    override fun chargerParId(id: Int): Employe {
        TODO("Not yet implemented")
    }


}