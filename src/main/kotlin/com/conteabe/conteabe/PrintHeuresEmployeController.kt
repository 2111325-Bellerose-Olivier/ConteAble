package com.conteabe.conteabe

import com.conteabe.conteabe.dao.EmployeDAO
import com.conteabe.conteabe.dao.TacheDossierDAO
import com.conteabe.conteabe.modele.Employe
import com.conteabe.conteabe.service.ServiceBD
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory

class Heure(
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
    private lateinit var nomEmploye: Label

    @FXML
    private lateinit var heureTotal: Label

    @FXML
    private lateinit var montantTotal: Label

    @FXML
    private lateinit var listeHeures: TableView<Heure>

    @FXML
    private lateinit var durees: TableColumn<Heure, String>

    @FXML
    private lateinit var taches: TableColumn<Heure, String>

    @FXML
    private lateinit var tauxs: TableColumn<Heure, String>

    @FXML
    private lateinit var montants: TableColumn<Heure, String>

    fun initialize() {
        nomEmploye.text = employe.nom
        heureTotal.text = "1:00"
        montantTotal.text = "4"

        durees.cellValueFactory = PropertyValueFactory("duree")
        taches.cellValueFactory = PropertyValueFactory("tache")
        tauxs.cellValueFactory = PropertyValueFactory("taux")
        montants.cellValueFactory = PropertyValueFactory("montant")

        listeHeures.items = FXCollections.observableList(
                TacheDossierDAO(
                        contexte.services.getService<ServiceBD>() as ServiceBD
                ).chargerTout().map { tache ->
                    Heure(
                            "${"%02d".format(tache.duree.time / 3_600_000)}:${"%02d".format((tache.duree.time / 60_000) % 60)}:${"%02d".format((tache.duree.time / 1_000) % 60)}",
                            tache.nom_tache,
                            "${"%.2f".format(3_600_000f * tache.montant / tache.duree.time)}$/h",
                            "%.2f".format(tache.montant)
                    )
                }
        )
    }
}