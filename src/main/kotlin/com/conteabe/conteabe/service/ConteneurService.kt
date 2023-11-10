package com.conteabe.conteabe.service

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

/**
 * Gère l'accès aux services par les différentes classes du système.
 *
 * @author Alexandre
 * @since 06/05/2023
 */
class ConteneurService {

    /**
     * Retourne le service demandé. Si le service n'existe pas, il est créé avant d'être retourné.
     *
     * @param classe la classe du service à créer
     * @return le service qui a été créé
     * @exception Exception si le service n'a pu être créé
     */
    inline fun <reified T> getService(): IService where T : IService {
        for (service in services) {
            if (service::class == T::class) {
                return service
            }
        }

        try {
            val nouveauService: IService = T::class.primaryConstructor!!.call()
            services.add(nouveauService)

            return nouveauService
        } catch (exception: NullPointerException) {
            // TODO: spécialiser l'exception
            throw  Exception("Impossible de créer le service de type : ${T::class.simpleName}")
        }

    }

    companion object {
        /**
         * Liste des services de l'application
         */
        val services: MutableList<IService> = mutableListOf()
    }
}