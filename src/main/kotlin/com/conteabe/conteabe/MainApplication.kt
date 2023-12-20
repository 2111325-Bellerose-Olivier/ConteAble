package com.conteabe.conteabe

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

class MainApplication : Application() {
    override fun start(stage: Stage) {

        val chargeur = FXMLLoader(MainApplication::class.java.getResource(Page.Client.view))
        chargeur.setController(Page.Client.constructor.call(Contexte(stage)))
        val parent : Parent = chargeur.load()


        stage.title = "Conte-Abe"
        stage.scene = Scene(parent, 800.0, 800.0)
        stage.show()
    }
}

fun main(args: Array<String>) {
    Application.launch(MainApplication::class.java, *args)
}