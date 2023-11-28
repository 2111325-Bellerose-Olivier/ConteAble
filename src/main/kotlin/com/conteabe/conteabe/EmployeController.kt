package com.conteabe.conteabe

import com.conteabe.conteabe.dao.EmployeDAO
import com.conteabe.conteabe.modele.Employe
import com.conteabe.conteabe.service.ServiceBD
import com.sun.javafx.scene.control.IntegerField
import javafx.collections.FXCollections
import javafx.collections.transformation.FilteredList
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import java.lang.reflect.Field
import java.util.function.Predicate
import kotlin.properties.Delegates

class EmployeController (private val contexte: Contexte) {
    @FXML
    private lateinit var welcomeText: Label

    @FXML
    private lateinit var listeEmployes: TableView<Employe>

    @FXML
    private lateinit var idEmploye: TableColumn<Employe, Int>

    @FXML
    private lateinit var idEmployes: IntegerField

    @FXML
    private lateinit var nomsEmploye: TableColumn<Employe, String>

    @FXML
    private lateinit var prenomsEmploye: TableColumn<Employe, String>

    @FXML
    private lateinit var courrielsEmploye: TableColumn<Employe, String>

    private lateinit var employes: FilteredList<Employe>

    fun initialize() {
        employes = FilteredList<Employe>(
            FXCollections.observableList(
                EmployeDAO(
                    contexte.services.getService<ServiceBD>() as ServiceBD
                ).chargerTout()
            )
        )

        // Définition des colonnes de la table
        idEmploye.cellValueFactory = PropertyValueFactory("id")
        nomsEmploye.cellValueFactory = PropertyValueFactory("nom")
        prenomsEmploye.cellValueFactory = PropertyValueFactory("prenom")
        courrielsEmploye.cellValueFactory = PropertyValueFactory("courriel")

        employes.predicate = Predicate { true }
        listeEmployes.items = employes
    }

    @FXML
    private fun ajouter() {
        contexte.SetPage(Page.Hello)
    }

    @FXML
    private fun supprimer() {
        val selectedEmploye = listeEmployes.selectionModel.selectedItem
        if (selectedEmploye != null) {

            val idToDelete = selectedEmploye.id
            val confirmationDialog = Alert(Alert.AlertType.CONFIRMATION)
            confirmationDialog.title = "Confirmation"
            confirmationDialog.headerText = "Voulez-vous vraiment supprimer l'employée ${idToDelete}"

            val result = confirmationDialog.showAndWait()

            if (result.isPresent && result.get() == ButtonType.OK) {
                if (idToDelete != null) {
                    EmployeDAO(
                        contexte.services.getService<ServiceBD>() as ServiceBD
                    ).supprimer(idToDelete)
                    listeEmployes.items.remove(selectedEmploye)
                }
            }

        } else {
            val confirmationDialog = Alert(Alert.AlertType.ERROR)
            confirmationDialog.title = "ERREUR !!!"
            confirmationDialog.headerText = "Veuillez sélectionner un élément dans le tableau plus haut en premier."
            confirmationDialog.showAndWait()
        }

    }

    @FXML
    private fun modifier() {
        contexte.SetPage(Page.Hello)
    }
}
