package com.conteabe.conteabe.GererClient

import com.conteabe.conteabe.Contexte
import com.conteabe.conteabe.Page
import com.conteabe.conteabe.StrategyPattern.*
import com.conteabe.conteabe.dao.ClientDAO
import com.conteabe.conteabe.modele.Client
import com.conteabe.conteabe.service.ServiceBD
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.input.KeyEvent
import javafx.scene.layout.VBox

/*
* Classe qui charge le ficher FXML ajouter-client.fxml
* et qui s'ocuppe d'enregistrer le nouveau client
*/
class AjouterClientController(private val contexte: Contexte) {

    @FXML
    private lateinit var adresse_civil: TextField
    @FXML
    private lateinit var code_postal: TextField
    @FXML
    private lateinit var ville: TextField
    @FXML
    private lateinit var province: TextField
    @FXML
    private lateinit var pays: TextField

    @FXML
    private lateinit var nom: TextField
    @FXML
    private lateinit var prenom: TextField
    @FXML
    private lateinit var courriel: TextField
    @FXML
    private lateinit var numero_telephone: TextField

    @FXML
    private lateinit var ajouterClient: Button

    @FXML
    private lateinit var messageSucces: Label

    private var erreurForm: BooleanProperty = SimpleBooleanProperty(true);

    /*
    * Constructeur de la classe AjouterClientController
    * S'occupe de lier le bool erreurForm à la propriété disable du boutton
    */
    fun initialize() {
        ajouterClient.disableProperty().bind(erreurForm)
    }

    /*
    * Fonction qui retourne à la page d'accueil de client
    */
    @FXML
    private fun RetourClient() {
        contexte.SetPage(Page.Client)
    }

    /*
    * Fonction qui enregistre le nouveau client
    */
    @FXML
    private fun Enregistrer() {
            val newClient = Client(
            adresse_civil = adresse_civil.text,
            code_postal = code_postal.text,
            ville = ville.text,
            province = province.text,
            pays = pays.text,

            nom = nom.text,
            prenom = prenom.text,
            courriel = courriel.text,
            numero_telephone = numero_telephone.text,
            id = null
        )
        try {
            ClientDAO(contexte.services.getService<ServiceBD>() as ServiceBD).enregistrer(newClient)
            messageSucces.text = "Le client a bien été enregistrer."
        } catch (error: Exception) {
            messageSucces.text = "Une erreur est survenue lors de l'enregistrement"
            println(error)
        }
    }

    /*
    * Vérifie les valeures du form Client
    * Return: Retourne un booléen s'il y a une erreure
    */
    private fun verifierValeursForm(): Boolean {
        val textFields: MutableList<TextField> = mutableListOf(adresse_civil, code_postal, ville, province, pays,
            nom, prenom, courriel, numero_telephone)

        return textFields.any { it.text.isNullOrEmpty() } || textFields.any {it.styleClass.contains("error-text-field")}
    }

    /*
    * Fonction qui valide un champ, et qui désactive le boutton s'il y a une erreur
    */
    private fun validerChamp(event: KeyEvent, strategy: Strategy) {
        val textField = event.source as TextField
        val parent = textField.parent as VBox
        val error = parent.children.find { it is Label } as Label

        strategy.Executer(textField, error)
        erreurForm.set(verifierValeursForm())
    }

    /*
    * Fonction qui reçoit une valeure lorsque le textField change
    * Pour ensuite valider le champ
    */
    @FXML
    private fun onChangeTextField(event: KeyEvent) {
        val strategy: Strategy = StrategyValidationTexte()

        validerChamp(event, strategy)
    }

    /*
    * Fonction qui reçoit le courriel lorsque la valeure du textField change
    * Pour ensuite valider le courriel
    */

    @FXML
    private fun onChangeCourrielField(event: KeyEvent) {
        val strategy: Strategy = StrategyValidationCourriel()

        validerChamp(event, strategy)
    }

    /*
    * Fonction qui reçoit le numéro de téléphone lorsque la valeure du textField change
    * Pour ensuite valider le numéro de téléphone
    */
    @FXML
    private fun onChangeTelephoneField(event: KeyEvent) {
        val strategy: Strategy = StrategyValidationTelephone()

        validerChamp(event, strategy)
    }

    /*
    * Fonction qui reçoit le code postal lorsque la valeure du textField change
    * Pour ensuite valider le code postal
    */
    @FXML
    private fun onChangeCodePostalField(event: KeyEvent) {
        val strategy: Strategy = StrategyValidationCodePostal()

        validerChamp(event, strategy)
    }
}