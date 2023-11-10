package com.conteabe.conteabe

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class RapportHeures : Application() {
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(RapportHeures::class.java.getResource("rapport-heures.fxml"))
        val scene = Scene(fxmlLoader.load(), 640.0, 360.0)
        stage.title = "Hello!"
        stage.scene = scene
        stage.show()
    }
}

fun main() {
    Application.launch(RapportHeures::class.java)
}