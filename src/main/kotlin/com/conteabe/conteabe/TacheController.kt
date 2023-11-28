package com.conteabe.conteabe

import com.conteabe.conteabe.dao.EmployeDAO
import com.conteabe.conteabe.modele.Employe
import com.conteabe.conteabe.service.ServiceBD
import javafx.collections.FXCollections
import javafx.collections.transformation.FilteredList
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.control.cell.PropertyValueFactory
import java.util.function.Predicate

class TacheController (private val contexte: Contexte) {
    @FXML
    private lateinit var welcomeText: Label

    @FXML
    private lateinit var listeEmployes: TableView<Employe>

    @FXML
    private lateinit var idEmploye: TableColumn<Employe, Int>

    @FXML
    private lateinit var idEmployes: TextField

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

        contexte.SetPage(Page.Hello)
    }

    @FXML
    private fun modifier() {
        contexte.SetPage(Page.Hello)
    }
}
