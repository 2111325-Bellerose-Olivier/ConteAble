package com.conteabe.conteabe.dao

import com.conteabe.conteabe.modele.Client
import com.conteabe.conteabe.modele.ClientCompagnie
import com.conteabe.conteabe.modele.ClientIndividu
import com.conteabe.conteabe.service.ServiceBD
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

class ClientIndividuDAO(serviceBD: ServiceBD) : DAOAbstraite<ClientIndividu>(serviceBD) {
    // * Insert ou Update * //

    private fun enregistrerClientIndividu(connexion: Connection, entite: ClientIndividu) {
        val insert = "INSERT INTO Client_Individu (nom, prenom, courriel, numero_telephone, id_client) VALUES (?, ?, ?, ?, ?);"
        val update = "UPDATE Client_Individu SET nom=?, prenom=?, courriel=?, numero_telephone=?, id_client=? WHERE id=?;"
        var requete: PreparedStatement

        if (entite.idIndividu == null)
            requete = connexion.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)
        else {
            requete = connexion.prepareStatement(update)
            requete.setInt(5, entite.id!!)
            requete.setInt(6, entite.idIndividu!!)
        }

        requete.setString(1, entite.nom)
        requete.setString(2, entite.prenom)
        requete.setString(3, entite.courriel)
        requete.setString(4, entite.numero_telephone)
        requete.setInt(5, entite.id!!)

        if (requete.executeUpdate() > 0) {
            val rowId = connexion.prepareStatement("SELECT last_insert_rowid()").executeQuery()
            rowId.next()
            entite.idIndividu = rowId.getInt(1)
        }
    }

    override fun enregistrer(entite: ClientIndividu) {
        val connexion = serviceBD.ouvrirConnexion()

        ClientDAO().enregistrer(connexion, entite)
        enregistrerClientIndividu(connexion, entite)

        serviceBD.fermerConnexion()
    }

    // * Select * //

    private fun chargerClientIndividuParId(connexion: Connection, id: Int) : ClientIndividu? {
        val requete: PreparedStatement =
                connexion.prepareStatement("SELECT * FROM Client_Individu WHERE id=?;")
                requete.setInt(1, id)

        val resultats: ResultSet = requete.executeQuery()

        val clientIndividu: ClientIndividu? =
                if(resultats.next()) {
                    val client: Client? = ClientDAO().chargerParId(connexion, resultats.getInt("id_client"))

                    ClientIndividu(
                            resultats.getInt("id"),
                            client!!.adresse_civil,
                            client.code_postal,
                            client.ville,
                            client.province,
                            client.pays,
                            resultats.getString("nom"),
                            resultats.getString("prenom"),
                            resultats.getString("courriel"),
                            resultats.getString("numero_telephone"),
                            client.id!!,
                    )
                } else null

        return clientIndividu
    }

    private fun chargerClientIndividu(connexion: Connection) : MutableList<ClientIndividu> {
        val requete: PreparedStatement =
                connexion.prepareStatement("SELECT * FROM Client_Individu;")
        val resultats: ResultSet = requete.executeQuery()
        val clientIndividus: MutableList<ClientIndividu> = mutableListOf()

        while (resultats.next()) {
            val client: Client? = ClientDAO().chargerParId(connexion, resultats.getInt("id_client"))

            clientIndividus.add(
                    ClientIndividu(
                        resultats.getInt("id"),
                        client!!.adresse_civil,
                        client.code_postal,
                        client.ville,
                        client.province,
                        client.pays,
                        resultats.getString("nom"),
                        resultats.getString("prenom"),
                        resultats.getString("courriel"),
                        resultats.getString("numero_telephone"),
                        client.id!!
                    )
            )
        }

        return clientIndividus
    }

    override fun chargerTout(): MutableList<ClientIndividu> {
        val connexion = serviceBD.ouvrirConnexion()
        val client = chargerClientIndividu(connexion)

        serviceBD.fermerConnexion()
        return client
    }

    override fun chargerParId(id: Int): ClientIndividu? {
        val connexion = serviceBD.ouvrirConnexion()
        val client = chargerClientIndividuParId(connexion, id)

        serviceBD.fermerConnexion()
        return client
    }

    private fun chargerIdClient(connexion: Connection, id: Int?) : Int {
        val requete = connexion.prepareStatement("SELECT id_client FROM Client_Compagnie WHERE id=?;")
        requete.setInt(1, id!!)
        val resultat: ResultSet = requete.executeQuery()

        return resultat.getInt("id_client")
    }

    private fun supprimerClientIndividu(connexion: Connection, id: Int?) {
        val requete = connexion.prepareStatement("DELETE FROM Client_Individu WHERE id=?;")
        requete.setInt(1, id!!)

        requete.executeUpdate()
    }

    override fun supprimer(id: Int): Boolean {
        val connexion = serviceBD.ouvrirConnexion()
        val idClient = chargerIdClient(connexion, id);

        ClientDAO().supprimer(connexion, idClient)
        supprimerClientIndividu(connexion, id)

        serviceBD.fermerConnexion()


        return false
    }

}
