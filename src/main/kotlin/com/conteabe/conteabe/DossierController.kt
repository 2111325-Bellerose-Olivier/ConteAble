package com.conteabe.conteabe

import com.conteabe.conteabe.modele.ClientCompagnie
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

    private fun trouverNomClient(id_client: Int?) : String {
        var clientsCompagnie: MutableList<ClientCompagnie> = (ClientCompagnieDAO(contexte.services.getService<ServiceBD>() as ServiceBD).chargerTout())
        var clientsIndividu: MutableList<ClientIndividu> = (ClientIndividuDAO(contexte.services.getService<ServiceBD>() as ServiceBD).chargerTout())

        val matchingClientCompagnie = clientsCompagnie.find { it.id == id_client }
        val matchingClientIndividu = clientsIndividu.find { it.id == id_client }

        return when {
            matchingClientCompagnie != null -> matchingClientCompagnie.nom_compagnie
            matchingClientIndividu != null -> matchingClientIndividu.prenom + " " + matchingClientIndividu.nom
            else -> "Client Inconnu"
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
