package com.conteabe.conteabe

import com.conteabe.conteabe.StrategyPattern.*
import com.conteabe.conteabe.dao.ClientCompagnieDAO
import com.conteabe.conteabe.dao.ClientIndividuDAO
import com.conteabe.conteabe.modele.ClientCompagnie
import com.conteabe.conteabe.modele.ClientIndividu
import com.conteabe.conteabe.modele.PersonneContact
import com.conteabe.conteabe.service.ServiceBD
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.CheckBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.input.KeyEvent
import javafx.scene.layout.VBox

data class FieldSet(
    val field: TextField,
    val error: Label
)

class CreationClientController(private val contexte: Contexte) {
    @FXML
    private lateinit var clientIndividue: CheckBox
    @FXML
    private lateinit var clientCompagnie: CheckBox

    @FXML
    private lateinit var clientCompagnieContainer: VBox
    @FXML
    private lateinit var personneContactContainer: VBox
    @FXML
    private lateinit var clientIndividuContainer: VBox

    @FXML
    private lateinit var enregistrerClient: Button

    @FXML
    private lateinit var messageSucces: Label

    // Clients

    @FXML
    lateinit var adresseField: TextField
    @FXML
    lateinit var codePostalField: TextField
    @FXML
    lateinit var villeField: TextField
    @FXML
    lateinit var provinceField: TextField
    @FXML
    lateinit var paysField: TextField

    // Client Individu

    @FXML
    lateinit var nomIndividuField: TextField
    @FXML
    lateinit var prenomIndividuField: TextField
    @FXML
    lateinit var courrielIndividuField: TextField
    @FXML
    lateinit var numeroIndividuField: TextField


    // Client Contact

    @FXML
    lateinit var nomCompagnieField: TextField
    @FXML
    lateinit var numeroCompagnieField: TextField

    @FXML
    lateinit var nomContactField: TextField
    @FXML
    lateinit var prenomContactField: TextField
    @FXML
    lateinit var courrielContactField: TextField
    @FXML
    lateinit var numeroContactField: TextField


    private var erreurForm: BooleanProperty = SimpleBooleanProperty(true);


    fun initialize() {
        enregistrerClient.disableProperty().bind(erreurForm);
    }

    private fun selectClientType(compagnieSelected: Boolean, individuSelected: Boolean) {
        clientCompagnie.isSelected = compagnieSelected
        clientIndividue.isSelected = individuSelected

        clientCompagnieContainer.isVisible = compagnieSelected
        personneContactContainer.isVisible = compagnieSelected
        clientIndividuContainer.isVisible = individuSelected

        clientCompagnieContainer.isManaged = compagnieSelected
        personneContactContainer.isManaged = compagnieSelected
        clientIndividuContainer.isManaged = individuSelected
    }

    private fun verifierValeursForm(): Boolean {
        val textFields: MutableList<TextField> = mutableListOf(adresseField, codePostalField, villeField, provinceField, paysField)

        if (clientIndividue.isSelected)
            textFields.addAll(listOf(nomIndividuField, prenomIndividuField, courrielIndividuField, numeroIndividuField))
        else
            textFields.addAll(listOf(nomCompagnieField, numeroCompagnieField, nomContactField, prenomContactField, courrielContactField, numeroContactField))

        return textFields.any { it.text.isNullOrEmpty() } || textFields.any {it.styleClass.contains("error-text-field")}
    }

    private fun validerChamp(event: KeyEvent, strategy: Strategy) {
        val textField = event.source as TextField
        val parent = textField.parent as VBox
        val error = parent.children.find { it is Label } as Label

        strategy.Executer(textField, error)
        erreurForm.set(verifierValeursForm())
    }

    private fun creerClientCompagnie() : ClientCompagnie {
        val personne = PersonneContact(
                id = null,
                nom = nomContactField.text,
                prenom = prenomContactField.text,
                courriel = courrielContactField.text,
                numero_telephone = numeroContactField.text
        )

        return ClientCompagnie(
            idCompagnie = null,
            adresse_civil = adresseField.text,
            code_postal = codePostalField.text,
            ville = villeField.text,
            province = provinceField.text,
            pays = paysField.text,
            nom_compagnie = nomCompagnieField.text,
            numero_compagnie = numeroCompagnieField.text.toInt(),
            personne_contact = personne,
            idClient = null,
        )
    }

    private fun creerClientIndividuel() : ClientIndividu {
        return ClientIndividu(
            idIndividu = null,
            adresse_civil = adresseField.text,
            code_postal = codePostalField.text,
            ville = villeField.text,
            province = provinceField.text,
            pays = paysField.text,
            nom = nomIndividuField.text,
            prenom = prenomIndividuField.text,
            courriel = courrielIndividuField.text,
            numero_telephone = numeroIndividuField.text,
            idClient = null,
        )
    }

    @FXML
    private fun enregistrerDonner() {
        messageSucces.isVisible = true

        try {
            if (clientCompagnie.isSelected) {
                val client: ClientCompagnie = creerClientCompagnie()
                ClientCompagnieDAO(contexte.services.getService<ServiceBD>() as ServiceBD).enregistrer(client)
            } else {
                val client: ClientIndividu = creerClientIndividuel()
                ClientIndividuDAO(contexte.services.getService<ServiceBD>() as ServiceBD).enregistrer(client)
            }

            messageSucces.text = "Le client a bien été enregistrer."
        } catch (error: Exception) {
            messageSucces.text = "Une erreur est survenue lors de l'enregistrement"
            println(error)
        }
    }

    @FXML
    private fun onChangeTextField(event: KeyEvent) {
        val strategy: Strategy = StrategyValidationTexte()

        validerChamp(event, strategy)
    }

    @FXML
    private fun onChangeCourrielField(event: KeyEvent) {
        val strategy: Strategy = StrategyValidationCourriel()

        validerChamp(event, strategy)
    }

    @FXML
    private fun onChangeTelephoneField(event: KeyEvent) {
        val strategy: Strategy = StrategyValidationTelephone()

        validerChamp(event, strategy)
    }

    @FXML
    private fun onChangeNumeroCompagnieField(event: KeyEvent) {
        val strategy: Strategy = StrategyValidationNumeroCompagnie()

        validerChamp(event, strategy)
    }


    @FXML
    private fun onCheckboxIndividuClick() {
        selectClientType(false, true)
        erreurForm.set(verifierValeursForm())
    }

    @FXML
    private fun onCheckboxCompagnieClick() {
        selectClientType(true, false)
        erreurForm.set(verifierValeursForm())
    }

    @FXML
    private fun onRetourClicked() {
        contexte.SetPage(Page.Client)
    }
 }