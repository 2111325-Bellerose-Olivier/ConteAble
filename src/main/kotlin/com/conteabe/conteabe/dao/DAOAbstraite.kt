package com.conteabe.conteabe.dao

import com.conteabe.conteabe.dao.Entite
import com.conteabe.conteabe.service.ServiceBD

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

}
