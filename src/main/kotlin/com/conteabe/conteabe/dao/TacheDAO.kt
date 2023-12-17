package com.conteabe.conteabe.dao

import com.conteabe.conteabe.modele.Employe
import com.conteabe.conteabe.service.ServiceBD
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import com.conteabe.conteabe.modele.Tache
import javafx.scene.control.TableColumn
import java.util.logging.Logger

class TacheDAO(serviceBD: ServiceBD) : DAOAbstraite<Tache>(serviceBD) {
    override fun enregistrer(entite: Tache) {
        val connexion = serviceBD.ouvrirConnexion()
        val estInsertion: Boolean = entite.id == null

        var requete: PreparedStatement = if (estInsertion) {
            connexion.prepareStatement(
                "INSERT INTO List_Tache (nom, taux_Horraire) VALUES (?, ?);",
                Statement.RETURN_GENERATED_KEYS
            )
        } else {
            requete = connexion.prepareStatement("UPDATE List_Tache SET nom = ?, taucHorraire = ? WHERE id = ?")
            requete.setInt(3, entite.id!!)
        }

        requete.setString(1, entite.nom)
        requete.setFloat(2, entite.tauxHorraire)

        val requeteReussi = requete.executeUpdate() > 0

        if (requeteReussi && estInsertion) {
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
            connexion.prepareStatement("SELECT id, nom, taux_horraire FROM List_Tache")
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
            connexion.prepareStatement("SELECT id, nom, taux_horraire FROM List_Tache where id = ?")
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

    fun insererTableTacheDossier(idDossier: Int, idTache: Int) {
        val connexion = serviceBD.ouvrirConnexion()

        val requete: PreparedStatement = try {
            connexion.prepareStatement(
                "INSERT INTO Tache_Dossier (id_dossier, id_tache) VALUES (?, ?);",
                Statement.RETURN_GENERATED_KEYS
            )
        } catch (e: SQLException) {
            Logger.getLogger(TacheDAO::class.java.name).severe("Erreur requete: ${e.message}")
            throw e
        }

        requete.setInt(1, idDossier)
        requete.setInt(2, idTache)

        requete.executeUpdate()

        serviceBD.fermerConnexion()
    }

    fun trouverdernierinseree(): Int {
        val connexion = serviceBD.ouvrirConnexion()
        val requete: PreparedStatement =
            connexion.prepareStatement("SELECT id FROM List_Tache ORDER BY id DESC LIMIT 1")
        val resultats: ResultSet = requete.executeQuery()

        val id: Int = resultats.getInt("id");

        serviceBD.fermerConnexion()
        return id
    }
}
