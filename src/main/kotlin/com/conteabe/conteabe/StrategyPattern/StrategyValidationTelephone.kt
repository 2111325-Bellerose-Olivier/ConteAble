package com.conteabe.conteabe.StrategyPattern

import javafx.scene.control.Label
import javafx.scene.control.TextField

class StrategyValidationTelephone : Strategy() {
    private var regex: Regex = Regex("^[(][\\d]{3}[)] [\\d]{3}-[\\d]{4}\$")

    override fun Executer(field: TextField, error: Label) {
        if (super.verifierSiTextFieldEstVide(field)) {
            super.ajouterStyle(field, error)
            error.text = "Le champ est obligatoire."
        } else if (!field.text.matches(regex)) {
            super.ajouterStyle(field, error)
            error.text = "Format Numéro de Téléphone: (###) ###-####"
        } else {
            super.enleverStyle(field, error)
        }
    }

}