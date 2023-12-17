package com.conteabe.conteabe.dao

import com.conteabe.conteabe.modele.Client
import com.conteabe.conteabe.service.ServiceBD
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

class ClientDAO() {

    fun enregistrer(connexion: Connection, entite: Client) {
        val estInsertion = entite.id == null

        val insert = "INSERT INTO Client (adresse_civil, code_postal, ville, province, pays) VALUES (?, ?, ?, ?, ?);"
        val update = "UPDATE Client SET adresse_civil=?, code_postal=?, ville=?, province=?, pays=? WHERE id = ?;"
        var requete: PreparedStatement

        if (estInsertion) {
            requete = connexion.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)
        } else {
            requete = connexion.prepareStatement(update)
            requete.setInt(6, entite.id!!)
        }

        requete.setString(1, entite.adresse_civil)
        requete.setString(2, entite.code_postal)
        requete.setString(3, entite.ville)
        requete.setString(4, entite.province)
        requete.setString(5, entite.pays)

        if (requete.executeUpdate() > 0) {
            val rowId = connexion.prepareStatement("SELECT last_insert_rowid()").executeQuery()
            rowId.next()
            entite.id = rowId.getInt(1)
        }
    }

    // * Select * //

     fun chargerTout(connexion: Connection): MutableList<Client> {
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
                    )
            )
        }

        return clients
    }

    fun chargerParId(connexion: Connection, id: Int): Client? {
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
            )
        else null


        return client
    }

    // * Delete * //

    fun supprimer(connexion: Connection, id: Int): Boolean {
        val requete = connexion.prepareStatement("DELETE FROM Client WHERE id=?;")
        requete.setInt(1, id)

        requete.executeUpdate()

        return true
    }
}
