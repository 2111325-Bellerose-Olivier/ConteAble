package com.conteabe.conteabe.dao

import com.conteabe.conteabe.modele.Employe
import com.conteabe.conteabe.modele.Role
import com.conteabe.conteabe.service.ServiceBD
import java.sql.PreparedStatement
import java.sql.ResultSet


/**
 * DAO spécialiser pour les projets.
 *
 * @param serviceBD le service de base de données utilisé.
 *
 * @author Alexandre
 * @since 06/05/2023
 */
class EmployeDAO(serviceBD: ServiceBD) : DAOAbstraite<Employe>(serviceBD) {

    override fun enregistrer(entite: Employe) {
        RoleDAO(serviceBD).enregistrer(entite.role)

        enregistrerEntite(
                "INSERT INTO Employe (nom, prenom, mdp, id_role, courriel) VALUES (?, ?, ?, ? ,?);",
                "UPDATE Employe SET nom = ?, prenom = ?, mdp = ?, id_role = ?, courriel = ? WHERE id = ?;",
                6,
                entite)
        { requete ->
            requete.setString(1, entite.nom)
            requete.setString(2, entite.prenom)
            requete.setString(3, entite.mdp)
            requete.setInt(4, entite.role.id!!)
            requete.setString(5, entite.courriel)
        }
    }


    override fun chargerTout(): MutableList<Employe> {
        val connexion = serviceBD.ouvrirConnexion()
        val requete: PreparedStatement =
                connexion.prepareStatement("SELECT e.id, e.nom, e.prenom, e.mdp, r.id AS role_id, r.nom AS role_nom, e.courriel FROM Employe e INNER JOIN Role r ON e.id_role = r.id")
        val resultats: ResultSet = requete.executeQuery()
        val employes: MutableList<Employe> = mutableListOf()

        while (resultats.next()) {
            employes.add(
                    Employe(
                            resultats.getInt("id"),
                            resultats.getString("nom"),
                            resultats.getString("prenom"),
                            resultats.getString("mdp"),
                            Role(
                                    resultats.getInt("role_id"),
                                    resultats.getString("role_nom")
                            ),
                            resultats.getString("courriel")
                    )
            )
        }
        serviceBD.fermerConnexion()
        return employes
    }

    override fun chargerParId(id: Int): Employe? {
        val connexion = serviceBD.ouvrirConnexion()
        val requete: PreparedStatement =
                connexion.prepareStatement("SELECT e.nom, e.prenom, e.mdp, r.id AS role_id, r.nom AS role_nom, e.courriel FROM Employe e INNER JOIN Role r ON e.id_role = r.id WHERE e.id = ?")
        requete.setInt(1, id)
        val resultats: ResultSet = requete.executeQuery()

        val employe: Employe? = if (resultats.next()) Employe(
                id,
                resultats.getString("nom"),
                resultats.getString("prenom"),
                resultats.getString("mdp"),
                Role(
                        resultats.getInt("role_id"),
                        resultats.getString("role_nom")
                ),
                resultats.getString("courriel")
        ) else null
        serviceBD.fermerConnexion()
        return employe
    }

    override fun supprimer(id: Int): Boolean{
        val connexion = serviceBD.ouvrirConnexion()
        val requete: PreparedStatement =
            connexion.prepareStatement("DELETE FROM Employe WHERE id= ?")
        requete.setInt(1, id)

        requete.executeUpdate()

        serviceBD.fermerConnexion()

        return false

    }
}
