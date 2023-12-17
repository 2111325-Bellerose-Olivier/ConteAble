package com.conteabe.conteabe

import com.conteabe.conteabe.dao.ClientCompagnieDAO
import com.conteabe.conteabe.dao.ClientIndividuDAO
import com.conteabe.conteabe.dao.DossierDAO
import com.conteabe.conteabe.modele.Client
import com.conteabe.conteabe.modele.ClientCompagnie
import com.conteabe.conteabe.modele.ClientIndividu
import com.conteabe.conteabe.modele.Dossier
import com.conteabe.conteabe.service.ServiceBD
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.util.StringConverter

class DosserAjouterController(private val contexte: Contexte) {
    @FXML
    private lateinit var clientField: ComboBox<Client>

    @FXML
    private lateinit var clientErreur: Label

    @FXML
    private lateinit var bouttonEnregister: Button

    @FXML
    private lateinit var dossierField: TextField

    @FXML
    private lateinit var dossierErreur: Label

    @FXML
    private lateinit var messageSucces: Label

    private var erreurForm: BooleanProperty = SimpleBooleanProperty(true);

    fun initialize() {
        fillComboBox()
        bouttonEnregister.disableProperty().bind(erreurForm)
    }

    private fun afficherErreur(label: Label, texteErreur: String) {
        label.text = texteErreur
        label.isVisible = true
        label.isManaged = true
    }

    private fun enleverErreur(label: Label) {
        label.isVisible = false
        label.isManaged = false
    }

    private fun chargerTousLesClients() : MutableList<Client> {
        val clientCompagnie: MutableList<ClientCompagnie> = ClientCompagnieDAO(contexte.services.getService<ServiceBD>() as ServiceBD).chargerTout()
        val clientIndividu: MutableList<ClientIndividu> = ClientIndividuDAO(contexte.services.getService<ServiceBD>() as ServiceBD).chargerTout()

        var clients: MutableList<Client> = mutableListOf()
        clients.addAll(clientCompagnie)
        clients.addAll(clientIndividu)

        return clients
    }

    private fun trouverNomClient(client: Client) : String {
        return when {
            client is ClientCompagnie -> client.nom_compagnie
            client is ClientIndividu -> "${client.prenom} ${client.nom}"
            else -> "Client Inconnu"
        }
    }

    private fun fillComboBox() {
        val clients: MutableList<Client> = chargerTousLesClients()

        if (clients.size == 0) {
            afficherErreur(clientErreur, "Aucun client n'a été trouver dans la base de donnée.")
            erreurForm.set(true)
        }

        clientField.items.addAll(clients)
        clientField.visibleRowCount = clients.size

        clientField.converter = object : StringConverter<Client>() {
            override fun toString(client: Client?): String {
                if (client != null)
                    return trouverNomClient(client)

                return ""
            }

            override fun fromString(p0: String?): Client? {
                return null
            }
        }

        clientField.value = clients.firstOrNull()

    }

    private fun verifierNomDossier(texte: String) {
        if (texte.isEmpty()) {
            afficherErreur(dossierErreur, "Le nom de dossier est requis.")
            erreurForm.set(true)
        }

        else if (texte.length > 255) {
            afficherErreur(dossierErreur, "Le nom du dossier ne peut pas faire plus de 255 caractères")
            erreurForm.set(true)
        }
        else {
            erreurForm.set(false)
            enleverErreur(dossierErreur)
        }
    }

    @FXML
    private fun retourBouttonClicked() {
        //contexte.SetPage(Page.Dossier)
    }

    @FXML
    private fun nomDossierOnChange() {
        verifierNomDossier(dossierField.text)

        println(dossierField.text)

        if (erreurForm.value && !dossierField.styleClass.contains("error-text-field")) {
            dossierField.styleClass.add("error-text-field")
        } else if (dossierField.styleClass.contains("error-text-field")) {
            dossierField.styleClass.remove("error-text-field")
        }
    }

    @FXML
    private fun enregistrerDossier() {
        try {
            messageSucces.isVisible = true

            if (!erreurForm.value && clientField.value != null) {
                val dossier = Dossier(
                    id = null,
                    nom = dossierField.text,
                    id_client = clientField.value.id!!
                )

                DossierDAO(contexte.services.getService<ServiceBD>() as ServiceBD).enregistrer(dossier)
            }

            messageSucces.text = "Le dossier à bien été enregistrer."
        } catch (error: Exception) {
            messageSucces.text = "Une erreur est survenue lors de l'enregistrement"
            println(error)
        }
    }
}
