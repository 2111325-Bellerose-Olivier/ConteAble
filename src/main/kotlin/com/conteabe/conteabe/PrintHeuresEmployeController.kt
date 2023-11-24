package com.conteabe.conteabe

import com.conteabe.conteabe.modele.Employe
import javafx.fxml.FXML
import javafx.scene.text.Text

class PrintHeuresEmployeController(
    private val contexte: Contexte,
    private val employe: Employe
) {
    @FXML
    private lateinit var name: Text

    fun initialize() {
        name.text = employe.prenom
    }
}