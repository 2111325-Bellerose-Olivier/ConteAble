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
        val connexion = serviceBD.ouvrirConnexion()

        ClientDAO().enregistrer(connexion, entite)
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

    private fun chargerClientCompagnieParId(connexion: Connection, id: Int): ClientCompagnie? {
        val requete: PreparedStatement =
                connexion.prepareStatement("SELECT * FROM Client_Compagnie WHERE id=?;")
                requete.setInt(1, id)
        val resultats: ResultSet = requete.executeQuery()

        val clientCompagnie: ClientCompagnie? =
            if (resultats.next()) {
                val client: Client? = ClientDAO().chargerParId(connexion, resultats.getInt("id_client"))
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
            val client: Client? = ClientDAO().chargerParId(connexion, resultats.getInt("id_client"))
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
    private fun chargerIdPersonneContact(connexion: Connection, id: Int?) : Int {
        val requete = connexion.prepareStatement("SELECT personne_contact FROM Client_Compagnie WHERE id=?;")
        requete.setInt(1, id!!)
        val resultat: ResultSet = requete.executeQuery()

        return resultat.getInt("personne_contact")
    }

    private fun chargerIdClient(connexion: Connection, id: Int?) : Int {
        val requete = connexion.prepareStatement("SELECT id_client FROM Client_Compagnie WHERE id=?;")
        requete.setInt(1, id!!)
        val resultat: ResultSet = requete.executeQuery()

        return resultat.getInt("id_client")
    }

    private fun supprimerPersonneContact(connexion: Connection, id: Int?) {
        val requete = connexion.prepareStatement("DELETE FROM Personne_Contact WHERE id=?;")
        requete.setInt(1, id!!)

        requete.executeUpdate()

    }

    private fun supprimerClientCompagnie(connexion: Connection, id: Int?) {
        val requete = connexion.prepareStatement("DELETE FROM Client_Compagnie WHERE id=?;")
        requete.setInt(1, id!!)

        requete.executeUpdate()
    }

    override fun supprimer(id: Int): Boolean {
        val connexion = serviceBD.ouvrirConnexion()
        val idClient = chargerIdClient(connexion, id)
        val idPersonne = chargerIdPersonneContact(connexion, id)

        supprimerPersonneContact(connexion, idPersonne)
        ClientDAO().supprimer(connexion, idClient)
        supprimerClientCompagnie(connexion, id)

        serviceBD.fermerConnexion()

        return false
    }

}