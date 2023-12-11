package com.conteabe.conteabe

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

class MainApplication : Application() {
    override fun start(stage: Stage) {


    }
}

fun main(@Suppress("UNUSED_PARAMETER") args: Array<String>) {
    Application.launch(MainApplication::class.java, *args)
}
