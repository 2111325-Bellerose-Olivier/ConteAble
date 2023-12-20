package com.conteabe.conteabe.GererEmploye

import com.conteabe.conteabe.Contexte
import com.conteabe.conteabe.dao.EmployeDAO
import com.conteabe.conteabe.modele.Employe
import com.conteabe.conteabe.service.ServiceBD
import javafx.collections.FXCollections
import javafx.collections.transformation.FilteredList
import javafx.fxml.FXML
import javafx.scene.control.TableView
import javafx.scene.control.TableColumn
import javafx.scene.control.TextField
import javafx.scene.control.cell.PropertyValueFactory
import java.util.function.Predicate

class ModifierEmployeController(private val contexte: Contexte) {
    @FXML
    private lateinit var nomField: TextField

    @FXML
    private lateinit var prenomField: TextField

    @FXML
    private lateinit var courrielField: TextField

    @FXML
    private lateinit var listeEmployes: TableView<Employe>

    @FXML
    private lateinit var id: TableColumn<Employe, Int>

    @FXML
    private lateinit var nom: TableColumn<Employe, String>

    @FXML
    private lateinit var prenom: TableColumn<Employe, String>

    @FXML
    private lateinit var courriel: TableColumn<Employe, String>

    private var employeSelectionne: Employe? = null

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
        nom.cellValueFactory = PropertyValueFactory("nom")
        prenom.cellValueFactory = PropertyValueFactory("prenom")
        courriel.cellValueFactory = PropertyValueFactory("courriel")
        employes.predicate = Predicate { true }
        listeEmployes.items = employes
        // Ajouter un listener pour détecter la sélection d'un employé
        listeEmployes.selectionModel.selectedItemProperty().addListener { _, _, nouvelEmploye ->
            employeSelectionne = nouvelEmploye
            afficherDetailsEmploye()
        }
    }

    private fun afficherDetailsEmploye() {
        if (employeSelectionne != null) {
            nomField.text = employeSelectionne!!.nom
            prenomField.text = employeSelectionne!!.prenom
            courrielField.text = employeSelectionne!!.courriel
        } else {
            // Réinitialiser les champs si aucun employé n'est sélectionné
            nomField.text = ""
            prenomField.text = ""
            courrielField.text = ""
        }
    }

    @FXML
    private fun modifierEmploye() {
        // Vérifier si un employé est sélectionné
        if (employeSelectionne != null) {
            // Mettre à jour les propriétés de l'employé avec les nouvelles valeurs
            employeSelectionne!!.nom = nomField.text
            employeSelectionne!!.prenom = prenomField.text
            employeSelectionne!!.courriel = courrielField.text

            EmployeDAO(
                contexte.services.getService<ServiceBD>() as ServiceBD
            ).enregistrer(employeSelectionne!!)
        }
    }
}
