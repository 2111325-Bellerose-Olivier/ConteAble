package com.conteabe.conteabe

<<<<<<< HEAD
import com.conteabe.conteabe.GererClient.AjouterClientController
import com.conteabe.conteabe.GererClient.ModifierClientController
=======
import AjoutHeure
>>>>>>> feat/CU-A01-C
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
    Tache(
        "tache.fxml",
        TacheController::class.primaryConstructor!!
    ),
    RapportHeures(
        "rapport-heures.fxml",
        RapportHeuresController::class.primaryConstructor!!
    ),
    __PrintHeuresEmploye(
        "print-heures-employe.fxml",
        PrintHeuresEmployeController::class.primaryConstructor!!
    ),
    Dossier(
        "dossier-view.fxml",
        DossierController::class.primaryConstructor!!
    ),
    AjouterDossier(
        "dossier-ajouter.fxml",
        DosserAjouterController::class.primaryConstructor!!
    ),
    AjoutHeures(
        "ajout-heure.fxml",
        AjoutHeure::class.primaryConstructor!!
    ),
    Client(
        "client-view.fxml",
        ClientController::class.primaryConstructor!!
    ),
    AjouterClient(
    "client-ajouter.fxml",
    AjouterClientController::class.primaryConstructor!!
    ),
    ModifierClient(
        "client-modifier.fxml",
        ModifierClientController::class.primaryConstructor!!
    ),



}
