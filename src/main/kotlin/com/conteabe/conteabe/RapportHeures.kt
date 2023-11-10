package com.conteabe.conteabe

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

class RapportHeures : Application() {
    override fun start(stage: Stage) {
        val chargeur = FXMLLoader(RapportHeures::class.java.getResource("rapport-heures.fxml"))
        chargeur.setController(RapportHeuresController(Contexte(stage)))
        val parent : Parent = chargeur.load()
        stage.title = "Hello!"
        stage.scene = Scene(parent, 640.0, 360.0)
        stage.show()
    }
}

fun main() {
    Application.launch(RapportHeures::class.java)
}