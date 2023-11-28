package com.conteabe.conteabe

import com.conteabe.conteabe.modele.Employe
import com.conteabe.conteabe.service.ConteneurService
import javafx.fxml.FXMLLoader
import javafx.print.PageOrientation
import javafx.print.Paper
import javafx.print.Printer
import javafx.print.Printer.MarginType
import javafx.print.PrinterJob
import javafx.scene.Parent
import javafx.stage.Stage
import javafx.stage.Window


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

        stage.width = Paper.A4.width + 50
        stage.height = Paper.A4.height + 38
        SetPage(page, *args)

        try {
            val job: PrinterJob = PrinterJob.createPrinterJob() ?: return false
            if (!job.printPage(
                    job.printerProperty().get().createPageLayout(
                        Paper.A4, PageOrientation.PORTRAIT, 0.0, 0.0, 0.0, 0.0
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