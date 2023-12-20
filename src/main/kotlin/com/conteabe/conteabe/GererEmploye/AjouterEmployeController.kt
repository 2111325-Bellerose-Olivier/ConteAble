package com.conteabe.conteabe

import com.conteabe.conteabe.dao.EmployeDAO
import com.conteabe.conteabe.dao.RoleDAO
import com.conteabe.conteabe.modele.Employe
import com.conteabe.conteabe.modele.Role
import com.conteabe.conteabe.service.ServiceBD
import javafx.collections.FXCollections
import javafx.collections.transformation.FilteredList
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import org.mindrot.jbcrypt.BCrypt
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;
import java.net.URL;
import java.util.ResourceBundle;
import com.sun.javafx.scene.control.IntegerField

/**
 * j'ai juste copié "CreationEmployeController" pour l'instant j'ai rien changé encore
 */
class AjouterEmployeController(private val contexte: Contexte) {

    @FXML
    private lateinit var validation: Label

    @FXML
    private lateinit var nom: TextField

    @FXML
    private lateinit var prenom: TextField

    @FXML
    private lateinit var courriel: TextField

    @FXML
    private lateinit var role: ComboBox<Role>

    @FXML
    private lateinit var password: TextField

    fun initialize() {
        role.items = FXCollections.observableList(
            RoleDAO(
                contexte.services.getService<ServiceBD>() as ServiceBD
            ).chargerTout()
        )
    }

    @FXML
    private fun placeholder() {
        contexte.SetPage(Page.Connection)
    }

    @FXML
    private fun ajouter() {
        val role_id = role.selectionModel.selectedItem?.id
        if (role_id == null) {
            validation.text = "Il faut selectionner son role"
            return
        }

        val employe =
            Employe(null, nom.text, prenom.text, BCrypt.hashpw(password.text, BCrypt.gensalt()), role_id, courriel.text)

        EmployeDAO(
            contexte.services.getService<ServiceBD>() as ServiceBD
        ).enregistrer(employe)

        if (employe.id == null) {
            validation.text = "Erreur de la creation de l'employe"
            return
        }

        contexte.employeConnecte = employe
        contexte.SetPage(Page.Hello)
    }
}

