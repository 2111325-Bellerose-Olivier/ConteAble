package com.conteabe.conteabe.dao

import com.conteabe.conteabe.modele.Client
import com.conteabe.conteabe.modele.ClientCompagnie
import com.conteabe.conteabe.modele.PersonneContact
import com.conteabe.conteabe.service.ServiceBD
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

class ClientCompagnieDAO(serviceBD: ServiceBD) : DAOAbstraite<ClientCompagnie>(serviceBD) {

    // * Insert et Update * //

    private fun enregistrerClient(connexion: Connection, entite: ClientCompagnie) {
        val insert = "INSERT INTO Client (adresse_civil, code_postal, ville, province, pays) VALUES (?, ?, ?, ?, ?);"
        val update = "UPDATE Client SET adresse_civi=?, code_postal=?, ville=?, province=?, pays=? WHERE id = ?;"
        var requete: PreparedStatement

        if (entite.id == null) {
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

    private fun enregistrerPersonneContact(connexion: Connection, entite: PersonneContact) {
        val insert = "INSERT INTO Personne_Contact (nom, prenom, courriel, numero_telephone) VALUES (?, ?, ?, ?);"
        val update = "UPDATE Personne_Contact SET nom=?, prenom=?, courriel=?, numero_telephone=? WHERE id = ?;"
        var requete: PreparedStatement

        if (entite.id == null)
            requete = connexion.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)
        else {
            requete = connexion.prepareStatement(update)
            requete.setInt(5, entite.id!!)
        }

        requete.setString(1, entite.nom)
        requete.setString(2, entite.prenom)
        requete.setString(3, entite.courriel)
        requete.setString(4, entite.numero_telephone)

        if (requete.executeUpdate() > 0) {
            val rowId = connexion.prepareStatement("SELECT last_insert_rowid()").executeQuery()
            rowId.next()
            entite.id = rowId.getInt(1)
        }
    }

    private fun enregistrerClientCompagnie(connexion: Connection, entite: ClientCompagnie) {
        val insert = "INSERT INTO Client_Compagnie (nom_compagnie, numero_compagnie, id_client, personne_contact) VALUES (?, ?, ?, ?);"
        val update = "UPDATE Client_Compagnie SET nom_compagnie=?, numero_compagnie=?, personne_contact=? WHERE id=?;"
        var requete: PreparedStatement

        if (entite.idCompagnie == null)
            requete = connexion.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)
        else {
            requete = connexion.prepareStatement(update)
            requete.setInt(3, entite.personne_contact.id!!)
            requete.setInt(4, entite.idCompagnie!!)
        }

        requete.setString(1, entite.nom_compagnie)
        requete.setInt(2, entite.numero_compagnie)
        requete.setInt(3, entite.id!!)
        requete.setInt(4, entite.personne_contact.id!!)

        if (requete.executeUpdate() > 0) {
            val rowId = connexion.prepareStatement("SELECT last_insert_rowid()").executeQuery()
            rowId.next()
            entite.idCompagnie = rowId.getInt(1)
        }
    }

    override fun enregistrer(entite: ClientCompagnie) {
        val estInsertion = entite.id == null
        val connexion = serviceBD.ouvrirConnexion()

        enregistrerClient(connexion, entite)
        enregistrerPersonneContact(connexion, entite.personne_contact)
        enregistrerClientCompagnie(connexion, entite)

        serviceBD.fermerConnexion()
    }

    // * Select * //

    private fun chargerPersonneContactParId(connexion: Connection, id: Int) : PersonneContact? {
        val requete: PreparedStatement =
                connexion.prepareStatement("SELECT * FROM Personne_Contact WHERE id = ?;")
                requete.setInt(1, id)
        val resultats: ResultSet = requete.executeQuery()

        val personneContact: PersonneContact? =
        if (resultats.next())
            PersonneContact(
                resultats.getInt("id"),
                resultats.getString("nom"),
                resultats.getString("prenom"),
                resultats.getString("courriel"),
                resultats.getString("numero_telephone")
            )
        else null

        return personneContact
    }

    private fun chargerClientParId(connexion: Connection, id_client: Int?) : Client? {
        val requete: PreparedStatement =
                connexion.prepareStatement("SELECT * FROM Client WHERE id = ?;")
                requete.setInt(1, id_client!!)
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

    private fun chargerClientCompagnieParId(connexion: Connection, id: Int): ClientCompagnie? {
        val requete: PreparedStatement =
                connexion.prepareStatement("SELECT * FROM Client_Compagnie WHERE id=?;")
                requete.setInt(1, id)
        val resultats: ResultSet = requete.executeQuery()

        val clientCompagnie: ClientCompagnie? =
            if (resultats.next()) {
                val client: Client?  = chargerClientParId(connexion, resultats.getInt("id_client"))
                val personneContact: PersonneContact? = chargerPersonneContactParId(connexion, resultats.getInt("personne_contact"))

                ClientCompagnie(
                        resultats.getInt("id"),
                        client!!.adresse_civil,
                        client.code_postal,
                        client.ville,
                        client.province,
                        client.pays,
                        resultats.getString("nom_compagnie"),
                        resultats.getInt("numero_compagnie"),
                        personneContact!!,
                        client.id!!,
                )
            } else null



        return clientCompagnie
    }

    private fun chargerClientCompagnie(connexion: Connection) : MutableList<ClientCompagnie> {
        val requete: PreparedStatement =
                connexion.prepareStatement("SELECT * FROM Client_Compagnie;")
        val resultats: ResultSet = requete.executeQuery()
        val clientCompagnies: MutableList<ClientCompagnie> = mutableListOf()

        while (resultats.next()) {
            val client: Client?  = chargerClientParId(connexion, resultats.getInt("id_client"))
            val personneContact: PersonneContact? = chargerPersonneContactParId(connexion, resultats.getInt("personne_contact"))

            clientCompagnies.add(
                ClientCompagnie(
                   resultats.getInt("id"),

                   client!!.adresse_civil,
                   client.code_postal,
                   client.ville,
                   client.province,
                   client.pays,

                   resultats.getString("nom_compagnie"),
                   resultats.getInt("numero_compagnie"),
                   personneContact!!,
                   client.id!!,

                )
            )
        }

        return clientCompagnies
    }

    override fun chargerTout(): MutableList<ClientCompagnie> {
        val connexion = serviceBD.ouvrirConnexion()
        val clients = chargerClientCompagnie(connexion)

        serviceBD.fermerConnexion()

        return clients
    }

    override fun chargerParId(id: Int): ClientCompagnie? {
        val connexion = serviceBD.ouvrirConnexion()
        val client = chargerClientCompagnieParId(connexion, id)

        serviceBD.fermerConnexion()

        return client
    }

    // * Delete * //

    private fun supprimerPersonne_Contact(connexion: Connection, id: Int): Boolean {
        var requete: PreparedStatement =
            connexion.prepareStatement("DELETE FROM Personne_Contact WHERE id = ?;")
            requete.setInt(1, id)

        val rows = requete.executeUpdate()

        return rows > 0
    }

    private fun supprimerClient(connexion: Connection, id: Int): Boolean {
        var requete: PreparedStatement =
                connexion.prepareStatement("DELETE FROM Client WHERE id = ?;")
                requete.setInt(1, id)

        val rows = requete.executeUpdate()

        return  rows > 0;
    }

    private fun supprimerClientCompagnie(connexion: Connection, id: Int): Boolean {
        val requete: PreparedStatement =
                connexion.prepareStatement("DELETE FROM Client_Compagnie WHERE id = ?;")
                requete.setInt(1, id)
        val rows = requete.executeUpdate()

        return rows > 0
    }

    override fun supprimer(id: Int): Boolean {
        val connexion = serviceBD.ouvrirConnexion()
        val sql = "SELECT personne_contact, id_client FROM Client_Compagnie WHERE id = ?;"
        var aReussi: Boolean = true

        val requete: PreparedStatement = connexion.prepareStatement(sql)
            requete.setInt(1, id)
        val resultat = requete.executeQuery()

        var personneContactId: Int? = null
        var clientId: Int? = null

        if (resultat.next()) {
            personneContactId = resultat.getInt("personne_contact")
            clientId = resultat.getInt("id_client")

            aReussi = supprimerPersonne_Contact(connexion, personneContactId)
            aReussi = supprimerClient(connexion, clientId) && aReussi
        }

        aReussi = supprimerClientCompagnie(connexion, id) && aReussi

        serviceBD.fermerConnexion()

        return aReussi
    }

}