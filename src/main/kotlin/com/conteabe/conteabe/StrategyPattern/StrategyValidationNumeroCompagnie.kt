package com.conteabe.conteabe.StrategyPattern

import javafx.scene.control.Label
import javafx.scene.control.TextField

class StrategyValidationNumeroCompagnie : Strategy() {
    val regex: Regex = Regex("^\\d+\$")

    override fun Executer(field: TextField, error: Label) {
        if (verifierSiTextFieldEstVide(field)) {
            super.ajouterStyle(field, error)
            error.text = "Le champ ne peut pas être vide."
        } else if (!field.text.matches(regex)) {
            super.ajouterStyle(field, error)
            error.text = "La valeur doit être numérique."
        } else {
            super.enleverStyle(field, error)
        }
    }

}