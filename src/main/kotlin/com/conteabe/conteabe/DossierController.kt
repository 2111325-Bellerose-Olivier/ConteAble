package com.conteabe.conteabe

import com.conteabe.conteabe.dao.DossierDAO
import com.conteabe.conteabe.modele.Client
import com.conteabe.conteabe.modele.Dossier
import com.conteabe.conteabe.service.ServiceBD
import javafx.collections.FXCollections
import javafx.collections.transformation.FilteredList
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory

class ListeDossier(var id: Int?, var client: Client, var nom_client: String, var nom_dossier: String) {
}

class DossierController(private val contexte: Contexte) {
    @FXML
    private lateinit var listeDossiers: TableView<ListeDossier>

    @FXML
    private lateinit var nomClients: TableColumn<ListeDossier, String>

    @FXML
    private lateinit var nomDossiers: TableColumn<ListeDossier, String>

    @FXML
    private lateinit var messageErreur: Label

    private lateinit var dossiers: FilteredList<ListeDossier>

    private fun trouverNomClient(client: Client): String {
        return "${client.nom} ${client.prenom}"
    }

    fun initialize() {
        val mutableListDossier: MutableList<Dossier> = DossierDAO(contexte.services.getService<ServiceBD>() as ServiceBD).chargerTout()
        val listeTemporaire: List<ListeDossier> = mutableListDossier.map {
           ListeDossier(
               id = it.id,
               client = it.client,
               nom_client = trouverNomClient(it.client),
               nom_dossier = it.nom,
           )
       }

        dossiers = FilteredList<ListeDossier>(FXCollections.observableList(listeTemporaire))

        nomDossiers.cellValueFactory = PropertyValueFactory("nom_dossier")
        nomClients.cellValueFactory = PropertyValueFactory("nom_client")

        listeDossiers.items = dossiers
    }

    @FXML
    private fun AjouterBoutton() {
        contexte.SetPage(Page.AjouterDossier)
    }

    @FXML
    private fun ModifierBoutton() {
        val selectedDossier: ListeDossier? = listeDossiers.selectionModel.selectedItem

        if (!messageErreur.isVisible || selectedDossier == null) {
            messageErreur.isVisible = true
            return
        }

        val currentDossier = Dossier(
            selectedDossier.id,
            selectedDossier.client,
            selectedDossier.nom_client,

            )

        messageErreur.isVisible = false
        contexte.SetPage(Page.ModifierDossier, currentDossier)
    }

}
