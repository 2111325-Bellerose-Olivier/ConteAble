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
    Tache(
        "tache.fxml",
        TacheController::class.primaryConstructor!!
    ),
    Dossier(
        "dossier-view.fxml",
        DossierController::class.primaryConstructor!!
    ),
    AjouterDossier(
        "dossier-ajouter.fxml",
        DosserAjouterController::class.primaryConstructor!!
    )
}
