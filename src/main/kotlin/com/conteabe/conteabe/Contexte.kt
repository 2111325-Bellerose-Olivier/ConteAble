package com.conteabe.conteabe

import com.conteabe.conteabe.service.ConteneurService
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.stage.Stage


/**
 * Définit des données propre au contexte d'exécution du système
 *
 * @author Alexandre
 * @since 06/05/2023
 */
class Contexte(private val stage: Stage) {
    val services: ConteneurService = ConteneurService()
    var employeConnecte: Employe? = null

    fun SetPage(page: Page) {
        val chargeur = FXMLLoader(MainApplication::class.java.getResource(page.view))
        chargeur.setController(page.constructor.call(this))
        val parent: Parent = chargeur.load()
        stage.scene.root = parent
    }
}
