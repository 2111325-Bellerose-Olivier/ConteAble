package com.conteabe.conteabe

import com.conteabe.conteabe.modele.Employe
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory

class Heure(
    val debut: String,
    val fin: String,
    val duree: String,
    val tache: String,
    val taux: String,
    val montant: String
);

class PrintHeuresEmployeController(
    private val contexte: Contexte,
    private val employe: Employe
) {
    @FXML
    private lateinit var listeHeures: TableView<Heure>

    @FXML
    private lateinit var debuts: TableColumn<Heure, String>

    @FXML
    private lateinit var fins: TableColumn<Heure, String>

    @FXML
    private lateinit var durees: TableColumn<Heure, String>

    @FXML
    private lateinit var taches: TableColumn<Heure, String>

    @FXML
    private lateinit var tauxs: TableColumn<Heure, String>

    @FXML
    private lateinit var montants: TableColumn<Heure, String>

    fun initialize() {
        debuts.cellValueFactory = PropertyValueFactory("debut")
        fins.cellValueFactory = PropertyValueFactory("fin")
        durees.cellValueFactory = PropertyValueFactory("duree")
        taches.cellValueFactory = PropertyValueFactory("tache")
        tauxs.cellValueFactory = PropertyValueFactory("taux")
        montants.cellValueFactory = PropertyValueFactory("montant")

        listeHeures.items = FXCollections.observableList(
            listOf(
                Heure("10:00", "11:00", "1:00", "appelle", "4$/h", "4")
            )
        )
    }
}