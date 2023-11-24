package com.conteabe.conteabe

import com.conteabe.conteabe.modele.Employe
import com.conteabe.conteabe.service.ConteneurService
import javafx.fxml.FXMLLoader
import javafx.print.PrinterJob
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

    fun SetPage(page: Page, vararg args: Any?) {
        val chargeur = FXMLLoader(MainApplication::class.java.getResource(page.view))
        chargeur.setController(page.constructor.call(this, *args))
        val parent: Parent = chargeur.load()
        stage.scene.root = parent
    }

    fun PrintScreen(page: Page, vararg args: Any?): Boolean {
        val current_scene = stage.scene.root
        val width = stage.width
        val height = stage.height

        stage.width = 595.0
        stage.height = 842.0
        SetPage(page, *args)

        val result = print()

        stage.width = width
        stage.height = height
        stage.scene.root = current_scene
        return result
    }

    private fun print(): Boolean{
        val job: PrinterJob = PrinterJob.createPrinterJob() ?: return false
        if (!job.showPrintDialog(stage)) {
            return false
        }
        if (!job.printPage(stage.scene.root)) {
            return false
        }
        return job.endJob()
    }
}