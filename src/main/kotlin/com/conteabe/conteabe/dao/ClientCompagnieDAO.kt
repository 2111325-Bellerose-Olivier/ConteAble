package com.conteabe.conteabe.dao

import com.conteabe.conteabe.modele.Client
import com.conteabe.conteabe.modele.ClientCompagnie
import com.conteabe.conteabe.modele.PersonneContact
import com.conteabe.conteabe.service.ServiceBD
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

class ClientCompagnieDAO(serviceBD: ServiceBD) : DAOAbstraite<ClientCompagnie>(serviceBD) {

    private fun enregistrerClient(entite: Client) {
        val connexion = serviceBD.ouvrirConnexion()

        val estInsertion: Boolean = entite.id == null
        val requete: PreparedStatement

        if (estInsertion) {
            requete = connexion.prepareStatement(
                "INSERT INTO Client (adresse_civil, code_postal, pays, province) VALUES (?,?,?,?);",
                Statement.RETURN_GENERATED_KEYS)
        } else {
            requete =connexion.prepareStatement("UPDATE Client SET adresse_civil = ?, code_postal = ?, pays = ?, province = ? WHERE id = ?")
            requete.setInt(5, entite.id!!)
        }

        requete.setString(1, entite.adresse_civil)
        requete.setString(2, entite.code_postal)
        requete.setString(3, entite.pays)
        requete.setString(4, entite.province)

        if (estInsertion) {
            val cleGenere: ResultSet = requete.generatedKeys

            if (cleGenere.next()) {
                entite.id = cleGenere.getInt(1)
            }
        }

        serviceBD.fermerConnexion()
    }

    private fun enregistrerPersonneContact(entite: PersonneContact) {
        val connexion = serviceBD.ouvrirConnexion()
        val estInsertion: Boolean = entite.id == null

        var requete: PreparedStatement = if (estInsertion) {
            connexion.prepareStatement(
                "INSERT INTO PersonneContact (nom, prenom, courriel, numero_telephone) VALUES (?, ?, ?, ?);",
                Statement.RETURN_GENERATED_KEYS
            )
        } else {
            connexion.prepareStatement("UPDATE PersonneContact SET nom = ?, prenom = ?, courriel = ?, numero_telephone = ?")
        }

        requete.setString(1, entite.nom)
        requete.setString(2, entite.prenom)
        requete.setString(3, entite.courriel)
        requete.setString(4, entite.numero_telephone)

        if (estInsertion) {
            val cleGenerer: ResultSet = requete.generatedKeys

            if (cleGenerer.next()) {
                entite.id = cleGenerer.getInt(1)
            }
        }

        serviceBD.fermerConnexion()
    }

    fun enregistrerClientCompagnie(entite: ClientCompagnie) {
        val connexion = serviceBD.ouvrirConnexion()
        val estInsertion: Boolean = entite.id == null

        var requete: PreparedStatement = if (estInsertion) {
            connexion.prepareStatement(
            "INSERT INTO ClientCompagnie (nom_compagnie, numero_compagnie, personne_contact, id_client) VALUES (?, ?, ?, ?);",
                Statement.RETURN_GENERATED_KEYS
            )
        } else {
            connexion.prepareStatement("UPDATE ClientCompagnie SET nom_compagnie = ?, numero_compagnie = ?, personne_contact = ?, id_client = ?;")
        }

        requete.setString(1, entite.nom_compagnie)
        requete.setInt(2, entite.numero_compagnie)
        requete.setInt(3, entite.personne_contact.id!!)
        requete.setInt(4, entite.client.id!!)

        if (estInsertion) {
            val cleGenerer: ResultSet = requete.generatedKeys

            if (cleGenerer.next()) {
                entite.id = cleGenerer.getInt(1)
            }
        }

        serviceBD.fermerConnexion()
    }

    override fun enregistrer(entite: ClientCompagnie) {
        enregistrerClient(entite.client)
        enregistrerPersonneContact(entite.personne_contact)
        enregistrerClientCompagnie(entite)
    }

    private fun chargerPersonneContactParId(id: Int) : PersonneContact {
        val connexion = serviceBD.ouvrirConnexion()
        val requete: PreparedStatement =
            connexion.prepareStatement("SELECT * FROM PersonneContact WHERE id = ?;")

        requete.setInt(1, id)
        val resultats: ResultSet = requete.executeQuery();

        val personne_contact = PersonneContact(
            resultats.getInt("id"),
            resultats.getString("nom"),
            resultats.getString("prenom"),
            resultats.getString("courriel"),
            resultats.getString("numero_telephone")
        )

        return personne_contact
    }

    private fun chargerClientParId(id: Int) : Client {
        val connexion = serviceBD.ouvrirConnexion()
        val requete: PreparedStatement =
            connexion.prepareStatement("SELECT * FROM Client WHERE id = ?;")

        requete.setInt(1, id)
        val resultats: ResultSet = requete.executeQuery();

        val client =  Client(
            resultats.getInt("id"),
            resultats.getString("adresse_civil"),
            resultats.getString("code_post"),
            resultats.getString("pays"),
            resultats.getString("province"),
        )

        serviceBD.fermerConnexion()
        return client
    }

    override fun chargerTout(): MutableList<ClientCompagnie> {
        val connexion = serviceBD.ouvrirConnexion()
        val requete: PreparedStatement =
            connexion.prepareStatement("SELECT * FROM ClientCompagnie;")
        val resultats: ResultSet = requete.executeQuery()
        val clientsCompagnie: MutableList<ClientCompagnie> = mutableListOf()

        while (resultats.next()) {
            val personneContactId = resultats.getInt("personne_contact")
            val idClient = resultats.getInt("id_client")

            clientsCompagnie.add(
                ClientCompagnie(
                    resultats.getInt("id"),
                    resultats.getString("nom_compagnie"),
                    resultats.getInt("numero_compagnie"),
                    chargerPersonneContactParId(personneContactId),
                    chargerClientParId(idClient),
                )
            )
        }
        serviceBD.fermerConnexion()
        return  clientsCompagnie
    }

    override fun chargerParId(id: Int): ClientCompagnie? {
        val connexion = serviceBD.ouvrirConnexion()
        val requete: PreparedStatement =
            connexion.prepareStatement("SELECT * FROM ClientCompagnie ORDER BY id_client;")
        val resultats: ResultSet = requete.executeQuery()

        if (!resultats.next())
            return  null

        val personneContactId = resultats.getInt("personne_contact")
        val idClient = resultats.getInt("id_client")

        val clientCompagnie: ClientCompagnie = ClientCompagnie(
            resultats.getInt("id"),
            resultats.getString("nom_compagnie"),
            resultats.getInt("numero_compagnie"),
            chargerPersonneContactParId(personneContactId),
            chargerClientParId(idClient),
        )

        serviceBD.fermerConnexion()
        return  clientCompagnie
    }

}
