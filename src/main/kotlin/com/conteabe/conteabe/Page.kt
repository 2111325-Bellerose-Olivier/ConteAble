package com.conteabe.conteabe

import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor

enum class Page(val view: String, val constructor: KFunction<*>) {
    Client(
        "client-view.fxml",
        ClientController::class.primaryConstructor!!
    ),
}
