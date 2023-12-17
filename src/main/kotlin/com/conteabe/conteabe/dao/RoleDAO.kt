package com.conteabe.conteabe.dao

import com.conteabe.conteabe.modele.Employe
import com.conteabe.conteabe.modele.Role
import com.conteabe.conteabe.service.ServiceBD
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
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
        val connexion = serviceBD.ouvrirConnexion()
        val estInsertion: Boolean = entite.id == null

        val requete: PreparedStatement = if (estInsertion) {
            connexion.prepareStatement(
                "INSERT INTO Role (nom) VALUES (?)"
            )
        } else {
            connexion.prepareStatement("UPDATE Role SET nom = ?")
        }

        requete.setString(1, entite.nom)

        if (requete.executeUpdate() > 0) {
            val rowId = connexion.prepareStatement("SELECT last_insert_rowid()").executeQuery()
            rowId.next()
            entite.id = rowId.getInt(1)
        }

        serviceBD.fermerConnexion()
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
            connexion.prepareStatement("SELECT id, nom FROM Role where id = ?")
        requete.setInt(1, id)
        val resultats: ResultSet = requete.executeQuery()

        val employe: Role? = if (resultats.next()) Role(
            resultats.getInt("id"),
            resultats.getString("nom")
        ) else null
        serviceBD.fermerConnexion()
        return employe
    }

    override fun supprimer(id: Int): Boolean? {
        TODO("Not yet implemented")
    }
}