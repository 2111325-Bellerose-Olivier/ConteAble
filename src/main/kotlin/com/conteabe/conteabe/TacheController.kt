package com.conteabe.conteabe

import com.conteabe.conteabe.dao.EmployeDAO
import com.conteabe.conteabe.dao.TacheDAO
import com.conteabe.conteabe.modele.Employe
import com.conteabe.conteabe.modele.Tache
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
    private lateinit var listeTaches: TableView<Tache>

    @FXML
    private lateinit var idTache: TableColumn<Employe, Int>

    @FXML
    private lateinit var nomsTache: TableColumn<Employe, String>

    @FXML
    private lateinit var tauxHorraire: TableColumn<Employe, String>

    private lateinit var taches: FilteredList<Tache>

    fun initialize() {
        taches = FilteredList<Tache>(
            FXCollections.observableList(
                TacheDAO(
                    contexte.services.getService<ServiceBD>() as ServiceBD
                ).chargerTout()
            )
        )

        // Définition des colonnes de la table
        idTache.cellValueFactory = PropertyValueFactory("id")
        nomsTache.cellValueFactory = PropertyValueFactory("nom")
        tauxHorraire.cellValueFactory = PropertyValueFactory("tauxHorraire")

        taches.predicate = Predicate { true }
        listeTaches.items = taches
    }

    @FXML
    private fun ajouter() {
        contexte.SetPage(Page.Hello)
    }

    @FXML
    private fun modifier() {
        contexte.SetPage(Page.Hello)
    }
}
