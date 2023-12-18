package com.conteabe.conteabe.dao

import com.conteabe.conteabe.modele.Dossier
import com.conteabe.conteabe.modele.Employe
import com.conteabe.conteabe.modele.SqlTacheDossier
import com.conteabe.conteabe.service.ServiceBD
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement
import com.conteabe.conteabe.modele.TacheDossier
import java.sql.Time

class TacheDossierDAO(serviceBD: ServiceBD) : DAOAbstraite<TacheDossier>(serviceBD) {
    override fun enregistrer(entite: TacheDossier) {
        DossierDAO(serviceBD).enregistrer(entite.dossier)
        EmployeDAO(serviceBD).enregistrer(entite.employe)

        val connexion = serviceBD.ouvrirConnexion()
        val estInsertion: Boolean = entite.id == null

        val requete: PreparedStatement
        if (estInsertion) {
            requete = connexion.prepareStatement(
                    "INSERT INTO Tache_Dossier (id_dossier, id_employe, nom_tache, duree, montant) VALUES (?, ?, ?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS
            )
        } else {
            requete =
                    connexion.prepareStatement("UPDATE Tache_Dossier SET id_dossier = ?, id_employe = ?, nom_tache = ?, duree = ?, montant = ? WHERE id = ?")
            requete.setInt(6, entite.id!!)
        }

        requete.setInt(1, entite.dossier.id!!)
        requete.setInt(2, entite.employe.id!!)
        requete.setString(3, entite.nom_tache)
        requete.setTime(4, entite.duree)
        requete.setFloat(5, entite.montant)

        val requeteReussi = requete.executeUpdate() > 0

        if (requeteReussi && estInsertion) {
            val rowId = connexion.prepareStatement("SELECT last_insert_rowid()").executeQuery()
            rowId.next()
            entite.id = rowId.getInt(1)
        }

        serviceBD.fermerConnexion()
    }


    override fun chargerTout(): MutableList<TacheDossier> {
        val connexion = serviceBD.ouvrirConnexion()
        val requete: PreparedStatement =
                connexion.prepareStatement("SELECT id, id_dossier, id_employe, nom_tache, duree, montant FROM Tache_Dossier")
        val resultats: ResultSet = requete.executeQuery()
        val taches: MutableList<SqlTacheDossier> = mutableListOf()

        while (resultats.next()) {
            taches.add(
                    SqlTacheDossier(
                            resultats.getInt("id"),
                            resultats.getInt("id_dossier"),
                            resultats.getInt("id_employe"),
                            resultats.getString("nom_tache"),
                            resultats.getTime("duree"),
                            resultats.getFloat("montant")
                    )
            )
        }
        serviceBD.fermerConnexion()
        return taches.map { tache ->
            TacheDossier(
                    tache.id,
                    DossierDAO(serviceBD).chargerParId(tache.dossier)!!,
                    EmployeDAO(serviceBD).chargerParId(tache.employe)!!,
                    tache.nom_tache,
                    tache.duree,
                    tache.montant
            )
        }.toMutableList()
    }

    override fun chargerParId(id: Int): TacheDossier? {
        val connexion = serviceBD.ouvrirConnexion()
        val requete: PreparedStatement =
                connexion.prepareStatement("SELECT id, id_dossier, id_employe, nom_tache, duree, montant FROM Tache_Dossier where id = ?")
        requete.setInt(1, id)
        val resultats: ResultSet = requete.executeQuery()

        val tache: TacheDossier? = if (resultats.next()) TacheDossier(
                resultats.getInt("id"),
                DossierDAO(serviceBD).chargerParId(resultats.getInt("id_dossier"))!!,
                EmployeDAO(serviceBD).chargerParId(resultats.getInt("id_employe"))!!,
                resultats.getString("nom_tache"),
                resultats.getTime("duree"),
                resultats.getFloat("montant")
        ) else null
        serviceBD.fermerConnexion()
        return tache
    }

    override fun supprimer(id: Int): Boolean {
        return false;
    }
}
