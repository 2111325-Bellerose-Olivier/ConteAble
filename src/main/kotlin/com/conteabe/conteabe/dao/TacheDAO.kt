package com.conteabe.conteabe.dao

import com.conteabe.conteabe.modele.Employe
import com.conteabe.conteabe.service.ServiceBD
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import com.conteabe.conteabe.modele.Tache

class TacheDAO(serviceBD: ServiceBD) : DAOAbstraite<Tache>(serviceBD) {
    override fun enregistrer(entite: Tache) {
        val connexion = serviceBD.ouvrirConnexion()
        val estInsertion: Boolean = entite.id == null

        var requete: PreparedStatement = if (estInsertion) {
            connexion.prepareStatement(
                "INSERT INTO List_Tache (nom, tauxHorraire) VALUES (?, ?);",
                Statement.RETURN_GENERATED_KEYS
            )
        } else {
            connexion.prepareStatement("UPDATE List_Tache SET nom = ?, taucHorraire = ?")
        }

        requete.setString(1, entite.nom)
        requete.setFloat(2, entite.taucHorraire)

        requete.executeUpdate()

        if (estInsertion) {
            val cleGenere: ResultSet = requete.generatedKeys

            if (cleGenere.next()) {
                entite.id = cleGenere.getInt(1)
            }
        }

        serviceBD.fermerConnexion()
    }


    override fun chargerTout(): MutableList<Tache> {
        val connexion = serviceBD.ouvrirConnexion()
        val requete: PreparedStatement =
            connexion.prepareStatement("SELECT id, nom, taux_horraire FROM Liste_Tache")
        val resultats: ResultSet = requete.executeQuery()
        val taches: MutableList<Tache> = mutableListOf()

        while (resultats.next()) {
            taches.add(
                Tache(
                    resultats.getInt("id"),
                    resultats.getString("nom"),
                    resultats.getFloat("taux_horraire"),
                )
            )
        }
        serviceBD.fermerConnexion()
        return taches
    }

    override fun chargerParId(id: Int): Tache? {
        val connexion = serviceBD.ouvrirConnexion()
        val requete: PreparedStatement =
            connexion.prepareStatement("SELECT id, nom, taux_horraire FROM Liste_Tache where id = ?")
        requete.setInt(1, id)
        val resultats: ResultSet = requete.executeQuery()

        val tache: Tache? = if (resultats.next()) Tache(
            resultats.getInt("id"),
            resultats.getString("nom"),
            resultats.getFloat("taux_horraire"),
        ) else null
        serviceBD.fermerConnexion()
        return tache
    }

    override fun supprimer(id: Int): Boolean{
        return false;
    }
}