package com.conteabe.conteabe

import com.conteabe.conteabe.dao.EmployeDAO
import com.conteabe.conteabe.modele.Employe
import com.conteabe.conteabe.service.ServiceBD
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.control.Label
import javafx.scene.control.TextField
import org.mindrot.jbcrypt.BCrypt


class ConnexionController(private val contexte: Contexte) {

    @FXML
    private lateinit var validation: Label

    @FXML
    private lateinit var identifiant: TextField

    @FXML
    private lateinit var password: TextField

    @FXML
    private fun cree() {
        contexte.SetPage(Page.CreationEmploye)
    }

    @FXML
    private fun connecter() {
        val id: Int = try {
            identifiant.text.toInt()
        } catch (_: NumberFormatException) {
            validation.text = "Identifiant ou mot de pass invalide."
            return
        }
        val employe: Employe? = EmployeDAO(
            contexte.services.getService<ServiceBD>() as ServiceBD
        ).chargerParId(id)

        if (employe == null) {
            validation.text = "Identifiant ou mot de pass invalide."
            return
        }

        if (!BCrypt.checkpw(password.text, employe.mdp)) {
            validation.text = "Identifiant ou mot de pass invalide."
            return
        }

        contexte.employeConnecte = employe
        contexte.SetPage(Page.Employe)
    }
}
