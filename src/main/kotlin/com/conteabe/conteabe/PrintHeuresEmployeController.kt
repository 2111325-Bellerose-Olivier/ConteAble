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
import java.sql.Date

class Heure(
    val debut: String,
    val duree: String,
    val tache: String,
    val taux: String,
    val montant: String
);

class PrintHeuresEmployeController(
    private val contexte: Contexte,
    private val employe: Employe,
    private val debut: Date,
    private val fin: Date
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
    private lateinit var debuts: TableColumn<Heure, String>

    @FXML
    private lateinit var durees: TableColumn<Heure, String>

    @FXML
    private lateinit var taches: TableColumn<Heure, String>

    @FXML
    private lateinit var tauxs: TableColumn<Heure, String>

    @FXML
    private lateinit var montants: TableColumn<Heure, String>

    fun initialize() {
        val tachesDossier = TacheDossierDAO(contexte.services.getService<ServiceBD>() as ServiceBD)
            .chargerParDate(employe.id!!, debut, fin)

        nomEmploye.text = employe.nom

        val duree = tachesDossier.sumOf { tache -> tache.duree.time }
        heureTotal.text =
            "%02d:%02d:%02d".format(duree / 3_600_000, (duree / 60_000) % 60, (duree / 1_000) % 60)
        montantTotal.text = "%.2f$".format(tachesDossier.sumOf { tache -> tache.montant.toDouble() })

        debuts.cellValueFactory = PropertyValueFactory("debut")
        durees.cellValueFactory = PropertyValueFactory("duree")
        taches.cellValueFactory = PropertyValueFactory("tache")
        tauxs.cellValueFactory = PropertyValueFactory("taux")
        montants.cellValueFactory = PropertyValueFactory("montant")

        listeHeures.items = FXCollections.observableList(tachesDossier.map { tache ->
            Heure(
                tache.debut.toString(),
                "%02d:%02d:%02d".format(
                    tache.duree.time / 3_600_000,
                    (tache.duree.time / 60_000) % 60,
                    (tache.duree.time / 1_000) % 60
                ),
                tache.nom_tache,
                "%.2f$/h".format((3_600_000f * tache.montant) / tache.duree.time),
                "%.2f$".format(tache.montant)
            )
        })
    }
}