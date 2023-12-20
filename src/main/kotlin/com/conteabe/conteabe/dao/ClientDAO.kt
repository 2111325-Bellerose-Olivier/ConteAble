package com.conteabe.conteabe.dao

import com.conteabe.conteabe.modele.Client
import com.conteabe.conteabe.service.ServiceBD
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

class ClientDAO(serviceBD: ServiceBD) : DAOAbstraite<Client>(serviceBD) {

    override fun enregistrer(entite: Client) {
        enregistrerEntite(
            "INSERT INTO Client (adresse_civil, code_postal, ville, province, pays, nom, prenom, courriel, numero_telephone) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);",
            "UPDATE Client SET adresse_civil=?, code_postal=?, ville=?, province=?, pays=?, nom=?, prenom=?, courriel=?, numero_telephone=? WHERE id = ?;",
            10,
            entite
        )
        { requete ->
            requete.setString(1, entite.adresse_civil)
            requete.setString(2, entite.code_postal)
            requete.setString(3, entite.ville)
            requete.setString(4, entite.province)
            requete.setString(5, entite.pays)
            requete.setString(6, entite.nom)
            requete.setString(7, entite.prenom)
            requete.setString(8, entite.courriel)
            requete.setString(9, entite.numero_telephone)
        }
    }


    override fun chargerTout(): MutableList<Client> {
        val connexion = serviceBD.ouvrirConnexion()

        val requete: PreparedStatement = connexion.prepareStatement("SELECT * FROM Client;")
        val resultats: ResultSet = requete.executeQuery()
        val clients: MutableList<Client> = mutableListOf()

        while (resultats.next()) {
            clients.add(
                Client(
                    resultats.getInt("id"),
                    resultats.getString("adresse_civil"),
                    resultats.getString("code_postal"),
                    resultats.getString("ville"),
                    resultats.getString("province"),
                    resultats.getString("pays"),
                    resultats.getString("nom"),
                    resultats.getString("prenom"),
                    resultats.getString("courriel"),
                    resultats.getString("numero_telephone")
                )
            )
        }

        serviceBD.fermerConnexion()
        return clients
    }

    override fun chargerParId(id: Int): Client? {
        val connexion = serviceBD.ouvrirConnexion()

        val requete: PreparedStatement =
            connexion.prepareStatement("SELECT * FROM Client WHERE id = ?;")
        requete.setInt(1, id)

        val resultats: ResultSet = requete.executeQuery()

        val client: Client? =
            if (resultats.next())
                Client(
                    resultats.getInt("id"),
                    resultats.getString("adresse_civil"),
                    resultats.getString("code_postal"),
                    resultats.getString("ville"),
                    resultats.getString("province"),
                    resultats.getString("pays"),
                    resultats.getString("nom"),
                    resultats.getString("prenom"),
                    resultats.getString("courriel"),
                    resultats.getString("numero_telephone")
                )
            else null

        serviceBD.fermerConnexion()
        return client
    }

    // * Delete * //

    override fun supprimer(id: Int): Boolean {
        val connexion = serviceBD.ouvrirConnexion()
        val requete = connexion.prepareStatement("DELETE FROM Client WHERE id=?;")
        requete.setInt(1, id)

        requete.executeUpdate()

        serviceBD.fermerConnexion()
        return true
    }
}
