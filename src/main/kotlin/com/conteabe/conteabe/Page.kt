package com.conteabe.conteabe

import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor

enum class Page(val view: String, val constructor: KFunction<*>) {
    Connection(
        "connexion.fxml",
        ConnexionController::class.primaryConstructor!!
    ),
    Hello(
        "hello-view.fxml",
        HelloController::class.primaryConstructor!!
    ),
    Employe(
        "tache.fxml",
        TacheController::class.primaryConstructor!!
    ),
}
