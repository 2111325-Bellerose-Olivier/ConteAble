package com.conteabe.conteabe.service

import java.sql.Connection
import java.sql.DriverManager

/**
 * Service de la base de données. Assure la connexion et la déconnexion au SGBD.
 *
 * @author Alexandre
 * @since 06/05/2023
 */
class ServiceBD : IService {

    /**
     * Contient la connexion à la base de données
     */
    private lateinit var connexion: Connection

    /**
     * Ouvre une connexion avec le SGBD. Si une connexion est déjà ouverte au moment de l'appel, la connexion
     * précédente est fermée, puis réouverte.
     *
     * @return la connexion active
     * TODO : gérer les exceptions
     */
    fun ouvrirConnexion(): Connection {
        if (this::connexion.isInitialized && !connexion.isClosed) {
            throw Exception("Duplicate connexions")
        }
        connexion = DriverManager.getConnection("jdbc:sqlite:db_contable.db")
        return connexion
    }

    /**
     * Ferme la connexion si elle n'est pas déjà fermée.
     */
    fun fermerConnexion() {
        if (!connexion.isClosed) {
            connexion.close()
        }
    }
}