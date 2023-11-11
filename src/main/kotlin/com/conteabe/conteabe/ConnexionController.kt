package com.conteabe.conteabe

import com.conteabe.conteabe.dao.EmployeDAO
import com.conteabe.conteabe.modele.Employe
import com.conteabe.conteabe.service.ServiceBD
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.control.Label
import javafx.scene.control.TextField


class ConnexionController(contexte: Contexte) : ControleurAbstrait(contexte) {

    @FXML
    private lateinit var validation: Label

    @FXML
    private lateinit var identifiant: TextField

    @FXML
    private lateinit var password: TextField

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

        if (employe.mdp != password.text) {
            validation.text = "Identifiant ou mot de pass invalide."
            return
        }

        contexte.employeConnecte = employe

        val chargeur = FXMLLoader(HelloApplication::class.java.getResource("hello-view.fxml"))
        chargeur.setController(HelloController(contexte))
        val parent : Parent = chargeur.load()
        contexte.stage.scene.root = parent
    }
}