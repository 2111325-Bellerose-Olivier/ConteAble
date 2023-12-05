package com.conteabe.conteabe

import com.conteabe.conteabe.dao.EmployeDAO
import com.conteabe.conteabe.modele.Employe
import com.conteabe.conteabe.service.ServiceBD
import javafx.collections.FXCollections
import javafx.collections.transformation.FilteredList
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import java.util.function.Predicate

class SupprimerEmployeController(private val contexte: Contexte) {
    @FXML
    private lateinit var welcomeText: Label

    @FXML
    private lateinit var listeEmployes: TableView<Employe>

    @FXML
    private lateinit var id: TableColumn<Employe, String>

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
        id.cellValueFactory = PropertyValueFactory("id")
        nomsEmploye.cellValueFactory = PropertyValueFactory("nom")
        prenomsEmploye.cellValueFactory = PropertyValueFactory("prenom")
        courrielsEmploye.cellValueFactory = PropertyValueFactory("courriel")

        employes.predicate = Predicate { true }
        listeEmployes.items = employes
    }

    @FXML
    private fun supprimer(){
        val selectedEmploye = listeEmployes.selectionModel.selectedItem
        if (selectedEmploye != null)
        {
            val idToDelete = selectedEmploye.id
            val confirmationDialog = Alert(Alert.AlertType.CONFIRMATION)
            confirmationDialog.title = "Confirmation"
            confirmationDialog.headerText = "Voulez-vous vraiment supprimer l'employée ${idToDelete}"

            val result = confirmationDialog.showAndWait()

            if(result.isPresent && result.get() == ButtonType.OK)
            {
                if(idToDelete != null)
                {
                    EmployeDAO(
                        contexte.services.getService<ServiceBD>() as ServiceBD
                    ).supprimer(idToDelete)
                    listeEmployes.items.remove(selectedEmploye)
                }
            }
        } else {

        }
    }

    @FXML
    private fun onHelloButtonClick() {
        welcomeText.text = "Welcome to JavaFX Application!"
    }
}