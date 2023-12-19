package com.conteabe.conteabe.dao

import com.conteabe.conteabe.modele.Dossier
import com.conteabe.conteabe.modele.Tache
import com.conteabe.conteabe.service.ServiceBD
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

class DossierDAO(serviceBD: ServiceBD) : DAOAbstraite<Dossier>(serviceBD) {
    override fun enregistrer(entite: Dossier) {
//        val connexion = serviceBD.ouvrirConnexion()
//        val estInsertion: Boolean = entite.id == null
//
//        var requete: PreparedStatement = if (estInsertion) {
//            connexion.prepareStatement(
//                "INSERT INTO List_Tache (nom, tauxHorraire) VALUES (?, ?);",
//                Statement.RETURN_GENERATED_KEYS
//            )
//        } else {
//            connexion.prepareStatement("UPDATE List_Tache SET nom = ?, taucHorraire = ?")
//        }
//
//        requete.setString(1, entite.nom)
//        requete.setFloat(2, entite.taucHorraire)
//
//        requete.executeUpdate()
//
//        if (estInsertion) {
//            val cleGenere: ResultSet = requete.generatedKeys
//
//            if (cleGenere.next()) {
//                entite.id = cleGenere.getInt(1)
//            }
//        }
//
//        serviceBD.fermerConnexion()
    }


    override fun chargerTout(): MutableList<Dossier> {
        val connexion = serviceBD.ouvrirConnexion()
        val requete: PreparedStatement =
            connexion.prepareStatement("SELECT id, id_client, nom FROM Dossier")
        val resultats: ResultSet = requete.executeQuery()
        val dossiers: MutableList<Dossier> = mutableListOf()

        while (resultats.next()) {
            dossiers.add(
                Dossier(
                    resultats.getInt("id"),
                    resultats.getInt("id_client"),
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
            connexion.prepareStatement("SELECT id, id_client, nom FROM Dossier where id = ?")
        requete.setInt(1, id)
        val resultats: ResultSet = requete.executeQuery()

        val dossier: Dossier? = if (resultats.next()) Dossier(
            resultats.getInt("id"),
            resultats.getInt("id_client"),
            resultats.getString("nom"),
        ) else null
        serviceBD.fermerConnexion()
        return dossier
    }

    override fun supprimer(id: Int): Boolean{
        return false;
    }
}