package com.conteabe.conteabe.dao

import com.conteabe.conteabe.modele.Client
import com.conteabe.conteabe.modele.ClientIndividu
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
class ClientIndividuDAO(serviceBD: ServiceBD) : DAOAbstraite<ClientIndividu>(serviceBD) {

    private fun enregistrerClient(entite: Client) {
        val connexion = serviceBD.ouvrirConnexion()

        val estInsertion: Boolean = entite.id == null
        val requete: PreparedStatement

        if (estInsertion) {
            requete = connexion.prepareStatement(
                "INSERT INTO Client (adresse_civil, code_postal, pays, province) VALUES (?,?,?,?);",
                Statement.RETURN_GENERATED_KEYS)
        } else {
            requete = connexion.prepareStatement("UPDATE Client SET adresse_civil = ?, code_postal = ?, pays = ?, province = ? WHERE id = ?")
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

    private fun enregistrerClientIndividu(entite: ClientIndividu) {
        val connexion = serviceBD.ouvrirConnexion()
        val estInsertion: Boolean = entite.id == null
        val requete: PreparedStatement


         if(estInsertion) {
            requete = connexion.prepareStatement(
                "INSERT INTO ClientIndividu (nom, prenom, courriel, numero_telephone, id_client) VALUES (?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            )
        } else {
            requete = connexion.prepareStatement("UPDATE ClientIndividu SET nom = ?, prenom = ?, courriel = ?, numero_telephone = ?, id_client = ? WHERE id = ?")
            requete.setInt(6, entite.id!!)
        }

        requete.setString(1, entite.nom)
        requete.setString(2, entite.prenom)
        requete.setString(3, entite.courriel)
        requete.setString(4, entite.numero_telephone)
        requete.setInt(5, entite.client.id!!)

        if (estInsertion) {
            val cleGenere: ResultSet = requete.generatedKeys

            if (cleGenere.next()) {
                entite.id = cleGenere.getInt(1)
            }
        }


        serviceBD.fermerConnexion()
    }

    override fun enregistrer(entite: ClientIndividu) {
        enregistrerClient(entite.client)
        enregistrerClientIndividu(entite)
    }

    private fun chargerClientParId(id: Int): Client {
        val connexion = serviceBD.ouvrirConnexion()
        val requete: PreparedStatement =
            connexion.prepareStatement("SELECT * FROM Client WHERE id = ?;")

        requete.setInt(1, id)
        val resultats: ResultSet = requete.executeQuery();

        return Client(
            resultats.getInt("id"),
            resultats.getString("adresse_civil"),
            resultats.getString("code_postal"),
            resultats.getString("pays"),
            resultats.getString("province"),
        )
    }

    override fun chargerTout(): MutableList<ClientIndividu> {
        val connexion = serviceBD.ouvrirConnexion()
        val requete: PreparedStatement =
            connexion.prepareStatement("SELECT * FROM ClientIndividu ORDER BY id_client")
        val resultats: ResultSet = requete.executeQuery()
        val clients: MutableList<ClientIndividu> = mutableListOf()

        while (resultats.next()) {
            val id: Int = resultats.getInt("id_client")
            clients.add(
                ClientIndividu(
                    resultats.getInt("id"),
                    resultats.getString("nom"),
                    resultats.getString("prenom"),
                    resultats.getString("courriel"),
                    resultats.getString("numero_telephone"),
                    chargerClientParId(id),
                )
            )
        }
        serviceBD.fermerConnexion()
        return clients
    }

    override fun chargerParId(id: Int): ClientIndividu? {
        val connexion = serviceBD.ouvrirConnexion()
        val requete: PreparedStatement =
            connexion.prepareStatement("SELECT id, nom, prenom, mdp, id_role, courriel FROM ClientIndividu where id = ?")
        requete.setInt(1, id)
        val resultats: ResultSet = requete.executeQuery()
        val idClient = resultats.getInt("id_client")

        val client: ClientIndividu? = if (resultats.next()) ClientIndividu(
            resultats.getInt("id"),
            resultats.getString("nom"),
            resultats.getString("prenom"),
            resultats.getString("courriel"),
            resultats.getString("numero_telephone"),
            chargerClientParId(idClient)
        ) else null

        serviceBD.fermerConnexion()
        return client
    }

}
