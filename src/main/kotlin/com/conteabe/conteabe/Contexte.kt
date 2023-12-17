package com.conteabe.conteabe

import com.conteabe.conteabe.modele.Employe
import com.conteabe.conteabe.service.ConteneurService
import javafx.fxml.FXMLLoader
import javafx.print.PageOrientation
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

        val job: PrinterJob = PrinterJob.createPrinterJob() ?: return false
        if (!job.showPrintDialog(stage)) {
            return false
        }

        try {
            val paper = job.jobSettings.pageLayout.paper
            stage.width = paper.width
            stage.height = paper.height
            SetPage(page, *args)
            if (!job.printPage(
                    job.printerProperty().get().createPageLayout(
                        paper, PageOrientation.PORTRAIT, 0.0, 0.0, 0.0, 0.0
                    ), stage.scene.root
                )
            ) {
                return false
            }
            return job.endJob()
        } finally {
            stage.width = width
            stage.height = height
            stage.scene.root = current_scene
        }
    }
}