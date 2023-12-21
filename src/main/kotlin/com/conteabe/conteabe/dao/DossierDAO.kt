package com.conteabe.conteabe.dao

import com.conteabe.conteabe.modele.Client
import com.conteabe.conteabe.modele.Dossier
import com.conteabe.conteabe.service.ServiceBD
import java.sql.PreparedStatement
import java.sql.ResultSet

class DossierDAO(serviceBD: ServiceBD) : DAOAbstraite<Dossier>(serviceBD) {
    override fun enregistrer(entite: Dossier) {
        ClientDAO(serviceBD).enregistrer(entite.client)
        enregistrerEntite(
            "INSERT INTO Dossier (id_client, nom) VALUES (?, ?);",
            "UPDATE Dossier SET id_client=?, nom=? WHERE id=?;",
            3,
            entite
        ) { requete ->
            requete.setInt(1, entite.client.id!!)
            requete.setString(2, entite.nom)
        }
    }


    override fun chargerTout(): MutableList<Dossier> {
        val connexion = serviceBD.ouvrirConnexion()
        val requete: PreparedStatement =
            connexion.prepareStatement("SELECT d.id, d.id_client, c.adresse_civil, c.code_postal, c.ville, c.province, c.pays, c.nom as nom_client, c.prenom, c.courriel, c.numero_telephone, d.nom FROM Dossier d INNER JOIN Client c ON c.id = d.id_client;")
        val resultats: ResultSet = requete.executeQuery()
        val dossiers: MutableList<Dossier> = mutableListOf()

        while (resultats.next()) {
            dossiers.add(
                Dossier(
                    resultats.getInt("id"),
                    Client(
                        resultats.getInt("id_client"),
                        resultats.getString("adresse_civil"),
                        resultats.getString("code_postal"),
                        resultats.getString("ville"),
                        resultats.getString("province"),
                        resultats.getString("pays"),
                        resultats.getString("nom_client"),
                        resultats.getString("prenom"),
                        resultats.getString("courriel"),
                        resultats.getString("numero_telephone"),
                    ),
                    resultats.getString("nom"),
                )
            )
        }
        serviceBD.fermerConnexion()
        return dossiers
    }

    override fun chargerParId(id: Int): Dossier? {
        val connexion = serviceBD.ouvrirConnexion()
        val requete: PreparedStatement =
            connexion.prepareStatement("SELECT d.id_client, c.adresse_civil, c.code_postal, c.ville, c.province, c.pays, c.nom as nom_client, c.prenom, c.courriel, c.numero_telephone, d.nom FROM Dossier d INNER JOIN Client c ON c.id = d.id_client where d.id = ?")
        requete.setInt(1, id)
        val resultats: ResultSet = requete.executeQuery()

        val dossier: Dossier? = if (resultats.next()) Dossier(
            id,
            Client(
                resultats.getInt("id_client"),
                resultats.getString("adresse_civil"),
                resultats.getString("code_postal"),
                resultats.getString("ville"),
                resultats.getString("province"),
                resultats.getString("pays"),
                resultats.getString("nom_client"),
                resultats.getString("prenom"),
                resultats.getString("courriel"),
                resultats.getString("numero_telephone"),
            ),
            resultats.getString("nom"),
        ) else null
        serviceBD.fermerConnexion()
        return dossier
    }

    override fun supprimer(id: Int): Boolean {
        return false;
    }
}
