package com.conteabe.conteabe

import com.conteabe.conteabe.GererEmploye.ModifierEmployeController
import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor

enum class Page(val view: String, val constructor: KFunction<*>) {
    Connection(
        "connexion.fxml",
        ConnexionController::class.primaryConstructor!!
    ),
    CreationEmploye(
        "creation-employe.fxml",
        CreationEmployeController::class.primaryConstructor!!
    ),
    Hello(
        "hello-view.fxml",
        HelloController::class.primaryConstructor!!
    ),
    AjouterEmploye(
      "./GererEmployeFXML/ajouter-employe.fxml",
        AjouterEmployeController::class.primaryConstructor!!
    ),

    SupprimerEmploye(
        "./GererEmployeFXML/supprimer-employe.fxml",
        SupprimerEmployeController::class.primaryConstructor!!
    ),

    ModifierEmploye(
      "./GererEmployeFXML/modifier-employe.fxml",
        ModifierEmployeController::class.primaryConstructor!!
    )
}
