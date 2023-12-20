package com.conteabe.conteabe

import com.conteabe.conteabe.dao.ClientDAO
import com.conteabe.conteabe.modele.Client
import com.conteabe.conteabe.service.ServiceBD
import javafx.collections.transformation.FilteredList
import javafx.fxml.FXML
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory

class ListeDossier(id: Int?, id_client: Int?, nom_client: String, nom_dossier: String) {
    var id = id
    var id_client = id_client
    var nom_client = nom_client
    var nom_dossier = nom_dossier
}

class DossierController(private val contexte: Contexte) {
    @FXML
    private lateinit var listeDossiers: TableView<ListeDossier>

    @FXML
    private lateinit var nomClients: TableColumn<ListeDossier, String>

    @FXML
    private lateinit var nomDossiers: TableColumn<ListeDossier, String>

    private lateinit var listeDossier: FilteredList<ListeDossier>

    private fun trouverNomClient(id_client: Int?): String {
        val clients: MutableList<Client> =
            ClientDAO(contexte.services.getService<ServiceBD>() as ServiceBD).chargerTout()

        val matchingClient = clients.find { it.id == id_client }

        return if (matchingClient == null) {
            "Client Inconnu"
        } else {
            "${matchingClient.nom} ${matchingClient.prenom}"
        }
    }

    fun initialize() {
//        val mutableListDossier: MutableList<Dossier> = DossierDAO(contexte.services.getService<ServiceBD>() as ServiceBD).chargerTout()
//        val listeTemporaire: List<ListeDossier> = mutableListDossier.map {
//            ListeDossier(
//                id = it.id,
//                id_client = it.id_client,
//                nom_client = trouverNomClient(it.id_client),
//                nom_dossier = it.nom,
//            )
//        }

        //listeDossier = FilteredList<ListeDossier>(FXCollections.observableList(listeTemporaire))

        nomDossiers.cellValueFactory = PropertyValueFactory("nom_dossier")
        nomClients.cellValueFactory = PropertyValueFactory("nom_client")

        listeDossiers.items = listeDossier
    }

    @FXML
    private fun ChangerPage() {
        contexte.SetPage(Page.AjouterDossier)
    }

}
