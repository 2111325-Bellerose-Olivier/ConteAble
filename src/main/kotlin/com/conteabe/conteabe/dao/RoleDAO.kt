package com.conteabe.conteabe.dao

import com.conteabe.conteabe.modele.Employe
import com.conteabe.conteabe.modele.Role
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
class RoleDAO(serviceBD: ServiceBD) : DAOAbstraite<Role>(serviceBD) {

    override fun enregistrer(entite: Role) {
        enregistrerEntite(
                "INSERT INTO Role (nom) VALUES (?);",
                "UPDATE Role SET nom = ? WHERE id = ?;",
                entite)
        { requete ->
            requete.setString(1, entite.nom)
        }
    }


    override fun chargerTout(): MutableList<Role> {
        val connexion = serviceBD.ouvrirConnexion()
        val requete: PreparedStatement =
                connexion.prepareStatement("SELECT id, nom FROM Role")
        val resultats: ResultSet = requete.executeQuery()
        val employes: MutableList<Role> = mutableListOf()

        while (resultats.next()) {
            employes.add(
                    Role(
                            resultats.getInt("id"),
                            resultats.getString("nom"),
                    )
            )
        }
        serviceBD.fermerConnexion()
        return employes
    }

    override fun chargerParId(id: Int): Role? {
        val connexion = serviceBD.ouvrirConnexion()
        val requete: PreparedStatement =
                connexion.prepareStatement("SELECT nom FROM Role where id = ?")
        requete.setInt(1, id)
        val resultats: ResultSet = requete.executeQuery()

        val employe: Role? = if (resultats.next()) Role(
                id,
                resultats.getString("nom")
        ) else null
        serviceBD.fermerConnexion()
        return employe
    }

    override fun supprimer(id: Int): Boolean? {
        return false;
    }

}
