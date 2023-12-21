package com.conteabe.conteabe.dao

import com.conteabe.conteabe.modele.*
import com.conteabe.conteabe.service.ServiceBD
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Date

class TacheDossierDAO(serviceBD: ServiceBD) : DAOAbstraite<TacheDossier>(serviceBD) {
    override fun enregistrer(entite: TacheDossier) {
        DossierDAO(serviceBD).enregistrer(entite.dossier)
        EmployeDAO(serviceBD).enregistrer(entite.employe)

        enregistrerEntite(
            "INSERT INTO Tache_Dossier (id_dossier, id_employe, nom_tache, debut, duree, montant) VALUES (?, ?, ?, ?, ?, ?);",
            "UPDATE Tache_Dossier SET id_dossier = ?, id_employe = ?, nom_tache = ?, debut = ?, duree = ?, montant = ? WHERE id = ?",
            7,
            entite
        )
        { requete ->
            requete.setInt(1, entite.dossier.id!!)
            requete.setInt(2, entite.employe.id!!)
            requete.setString(3, entite.nom_tache)
            requete.setTimestamp(4, entite.debut)
            requete.setTime(5, entite.duree)
            requete.setFloat(6, entite.montant)
        }
    }

    fun convertRow(resultats: ResultSet): TacheDossier {
        return TacheDossier(
            resultats.getInt(1),
            Dossier(
                resultats.getInt(8),
                Client(
                    resultats.getInt(11),
                    resultats.getString(12),
                    resultats.getString(13),
                    resultats.getString(14),
                    resultats.getString(15),
                    resultats.getString(16),
                    resultats.getString(17),
                    resultats.getString(18),
                    resultats.getString(19),
                    resultats.getString(20),
                ),
                resultats.getString(10),
            ),
            Employe(
                resultats.getInt(21),
                resultats.getString(22),
                resultats.getString(23),
                resultats.getString(24),
                Role(
                    resultats.getInt(27),
                    resultats.getString(28)
                ),
                resultats.getString(26)
            ),
            resultats.getString(4),
            resultats.getTimestamp(5),
            resultats.getTime(6),
            resultats.getFloat(7)
        )
    }

    override fun chargerTout(): MutableList<TacheDossier> {
        val connexion = serviceBD.ouvrirConnexion()
        val requete: PreparedStatement =
            connexion.prepareStatement("SELECT * FROM Tache_Dossier t INNER JOIN Dossier d ON d.id = t.id_dossier INNER JOIN Client c ON c.id = d.id_client INNER JOIN Employe e ON e.id = t.id_employe INNER JOIN Role r ON r.id = e.id_role")
        val resultats: ResultSet = requete.executeQuery()
        val taches: MutableList<TacheDossier> = mutableListOf()

        while (resultats.next()) {
            taches.add(
                convertRow(resultats)
            )
        }
        serviceBD.fermerConnexion()
        return taches
    }

    override fun chargerParId(id: Int): TacheDossier? {
        val connexion = serviceBD.ouvrirConnexion()
        val requete: PreparedStatement =
            connexion.prepareStatement("SELECT * FROM Tache_Dossier t INNER JOIN Dossier d ON d.id = t.id_dossier INNER JOIN Client c ON c.id = d.id_client INNER JOIN Employe e ON e.id = t.id_employe INNER JOIN Role r ON r.id = e.id_role where t.id = ?")
        requete.setInt(1, id)
        val resultats: ResultSet = requete.executeQuery()

        val tache: TacheDossier? = if (resultats.next()) convertRow(resultats) else null
        serviceBD.fermerConnexion()
        return tache
    }

    public fun chargerParDate(employe_id: Int, debut: Date, fin: Date): MutableList<TacheDossier> {
        val connexion = serviceBD.ouvrirConnexion()
        val requete: PreparedStatement =
            connexion.prepareStatement("SELECT * FROM Tache_Dossier t INNER JOIN Dossier d ON d.id = t.id_dossier INNER JOIN Client c ON c.id = d.id_client INNER JOIN Employe e ON e.id = t.id_employe INNER JOIN Role r ON r.id = e.id_role where e.id = ? and t.debut >= ? and t.debut + t.duree <= ?")
        requete.setInt(1, employe_id)
        requete.setDate(2, debut)
        requete.setDate(3, fin)
        val resultats: ResultSet = requete.executeQuery()
        val taches: MutableList<TacheDossier> = mutableListOf()

        while (resultats.next()) {
            taches.add(
                convertRow(resultats)
            )
        }
        serviceBD.fermerConnexion()
        return taches
    }

    override fun supprimer(id: Int): Boolean {
        return false;
    }
}
