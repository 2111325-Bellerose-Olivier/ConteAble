package com.conteabe.conteabe

import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor

enum class Page(val view: String, val constructor: KFunction<*>) {
    Dossier(
        "dossier-view.fxml",
        DossierController::class.primaryConstructor!!
    ),
    AjouterDossier(
        "dossier-ajouter.fxml",
        DosserAjouterController::class.primaryConstructor!!
    )
}
