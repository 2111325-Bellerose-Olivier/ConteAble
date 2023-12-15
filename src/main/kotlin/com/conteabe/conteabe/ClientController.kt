package com.conteabe.conteabe

import com.conteabe.conteabe.dao.ClientCompagnieDAO
import com.conteabe.conteabe.dao.ClientIndividuDAO
import com.conteabe.conteabe.modele.Client
import com.conteabe.conteabe.modele.ClientCompagnie
import com.conteabe.conteabe.modele.ClientIndividu
import com.conteabe.conteabe.service.ServiceBD
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.transformation.FilteredList
import javafx.fxml.FXML
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import javafx.util.Callback
import java.util.function.Predicate

class ClientController(private val contexte: Contexte) {
    @FXML
    private lateinit var listeClients: TableView<Client>

    @FXML
    private lateinit var idClient: TableColumn<Client, Int>

    @FXML
    private lateinit var nomClients: TableColumn<Client, String>

    @FXML
    private lateinit var addresseClients: TableColumn<Client, String>

    @FXML
    private lateinit var codePostalClients: TableColumn<Client, String>

    @FXML
    private lateinit var paysClients: TableColumn<Client, String>

    @FXML
    private lateinit var provinceClients: TableColumn<Client, String>

    private lateinit var clientsCompagnie: FilteredList<ClientCompagnie>
    private lateinit var clientIndividus: FilteredList<ClientIndividu>
    private lateinit var clients: FilteredList<Client>
    fun initialize() {
        clientsCompagnie = FilteredList<ClientCompagnie>(
                FXCollections.observableList(
                        ClientCompagnieDAO(
                                contexte.services.getService<ServiceBD>() as ServiceBD
                        ).chargerTout()
                )
        )

        clientIndividus = FilteredList<ClientIndividu>(
                FXCollections.observableList(
                        ClientIndividuDAO(
                                contexte.services.getService<ServiceBD>() as ServiceBD
                        ).chargerTout()
                )
        )

        val mutableClients = mutableListOf<Client>()
        mutableClients.addAll(clientsCompagnie)
        mutableClients.addAll(clientIndividus)
        val clientList = mutableClients.sortedBy { client: Client -> client.id }

        clients = FilteredList(FXCollections.observableList(clientList))

        idClient.cellValueFactory = PropertyValueFactory("id")

        nomClients.cellValueFactory = Callback { param ->
            when (val value = param.value) {
                is ClientCompagnie -> SimpleStringProperty(value.nom_compagnie)
                is ClientIndividu -> SimpleStringProperty(value.prenom + " " + value.nom)
                else -> SimpleStringProperty("")
            }
        }

        addresseClients.cellValueFactory = Callback { param ->
            SimpleStringProperty(param.value.adresse_civil + " " + param.value.ville)
        }
        codePostalClients.cellValueFactory = PropertyValueFactory("code_postal")
        paysClients.cellValueFactory = PropertyValueFactory("pays")
        provinceClients.cellValueFactory = PropertyValueFactory("province")

        clients.predicate = Predicate { true }
        listeClients.items = clients
    }

    @FXML
    private fun onAjouterButtonClick() {
        contexte.SetPage(Page.AjouterClient)
    }
}

