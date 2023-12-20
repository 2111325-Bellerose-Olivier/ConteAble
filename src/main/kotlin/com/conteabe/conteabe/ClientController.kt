package com.conteabe.conteabe

import com.conteabe.conteabe.dao.ClientDAO
import com.conteabe.conteabe.modele.Client
import com.conteabe.conteabe.service.ServiceBD
import javafx.collections.FXCollections
import javafx.collections.transformation.FilteredList
import javafx.fxml.FXML
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import java.util.function.Predicate

class ClientController(private val contexte: Contexte) {

    @FXML
    private lateinit var listeClients: TableView<Client>

    @FXML
    private lateinit var nomsClient: TableColumn<Client, String>

    @FXML
    private lateinit var prenomsClient: TableColumn<Client, String>

    @FXML
    private lateinit var courrielsClient: TableColumn<Client, String>

    @FXML
    private lateinit var numeroTelephoneClients: TableColumn<Client, String>

    @FXML
    private lateinit var adresseClients: TableColumn<Client, String>

    @FXML
    private lateinit var codePostalClients: TableColumn<Client, String>

    @FXML
    private lateinit var villeClients: TableColumn<Client, String>

    @FXML
    private lateinit var provinceClients: TableColumn<Client, String>

    @FXML
    private lateinit var paysClients: TableColumn<Client, String>


    private lateinit var clients: FilteredList<Client>

    fun initialize() {
        clients = FilteredList<Client>(
            FXCollections.observableList(
                ClientDAO(
                    contexte.services.getService<ServiceBD>() as ServiceBD
                ).chargerTout()
            )
        )

        // Définition des colonnes de la table
        nomsClient.cellValueFactory = PropertyValueFactory("nom")
        prenomsClient.cellValueFactory = PropertyValueFactory("prenom")
        courrielsClient.cellValueFactory = PropertyValueFactory("courriel")
        numeroTelephoneClients.cellValueFactory = PropertyValueFactory("numero_telephone")

        adresseClients.cellValueFactory = PropertyValueFactory("adresse_civil")
        codePostalClients.cellValueFactory = PropertyValueFactory("code_postal")
        villeClients.cellValueFactory = PropertyValueFactory("ville")
        provinceClients.cellValueFactory = PropertyValueFactory("province")
        paysClients.cellValueFactory = PropertyValueFactory("pays")

        clients.predicate = Predicate { true }
        listeClients.items = clients
    }

    @FXML
    private fun AjouterBoutton() {
        contexte.SetPage(Page.AjouterClient)
    }

}