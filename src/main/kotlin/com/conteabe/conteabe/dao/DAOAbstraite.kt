package com.conteabe.conteabe.dao

import com.conteabe.conteabe.service.ServiceBD
import java.sql.PreparedStatement

/**
 * Interface commune aux DAO. Elle permet d'enregistrer un objet, charger tous les objets du type
 * ou de charger un des objets par son id.
 *
 * @param le service de BD utilisé pour se connecter.
 *
 * @author Alexandre
 * @since 06/05/2023
 */
abstract class DAOAbstraite<T>(serviceBD: ServiceBD) where T : Entite {

    /**
     * Service de BD utilisé par la DAO
     */
    protected val serviceBD: ServiceBD = serviceBD

    /**
     * Enregistre une entitée dans la BD
     *
     * @param entite l'entitée à enregistrer
     */
    abstract fun enregistrer(entite: T)

    /**
     * Enregistre ou Modifie l'entite dans la bd
     *
     * @param insertQuery Une query SQLite
     * @param updateQuery Une query SQLite (le where id est a la fin)
     * @param entite l'entite a enregistrer ou modifier
     * @param bindParams Applique les parametres dans la requete (excepter l'id)
     */
    fun enregistrerEntite(insertQuery: String, updateQuery: String, entite: T, bindParams: (PreparedStatement) -> Unit) {
        val connexion = serviceBD.ouvrirConnexion()
        val estInsertion: Boolean = entite.id == null

        val requete: PreparedStatement
        if (estInsertion) {
            requete = connexion.prepareStatement(insertQuery)
        } else {
            requete =
                    connexion.prepareStatement(updateQuery)
            requete.setInt(6, entite.id!!)
        }

        bindParams.invoke(requete)

        requete.executeUpdate()

        if (estInsertion) {
            val rowId = connexion.prepareStatement("SELECT last_insert_rowid()").executeQuery()
            rowId.next()
            entite.id = rowId.getInt(1)
        }
        serviceBD.fermerConnexion()
    }

    /**
     * Charge toutes les entitées du type indiqué.
     *
     * @return une liste muable du type d'entité demandé
     */
    abstract fun chargerTout(): MutableList<T>

    /**
     * Charge une entité selon l'identifiant indiqué.
     *
     * @param id l'identifiant de l'entité à charger.
     * @return l'entité chargée.
     */
    abstract fun chargerParId(id: Int): T?

    abstract fun supprimer(id: Int): Boolean?
}
