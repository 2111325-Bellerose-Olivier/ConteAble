package com.conteabe.conteabe

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

class HelloApplication : Application() {
    override fun start(stage: Stage) {
        val chargeur = FXMLLoader(HelloApplication::class.java.getResource("hello-view.fxml"))
        chargeur.setController(HelloController(Contexte(stage)))
        val parent : Parent = chargeur.load()


        stage.title = "Hello!"
        stage.scene = Scene(parent, 800.0, 800.0)
        stage.show()
    }
}

fun main(@Suppress("UNUSED_PARAMETER") args: Array<String>) {
    Application.launch(HelloApplication::class.java, *args)
}