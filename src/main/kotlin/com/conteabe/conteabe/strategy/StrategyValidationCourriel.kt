package com.conteabe.conteabe.StrategyPattern

import javafx.scene.control.Label
import javafx.scene.control.TextField

class StrategyValidationCourriel : Strategy() {
    val regex: Regex = Regex("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")
    /*
    * Valide le courriel
    * */
    override fun Executer(field: TextField, error: Label) {
        if (super.verifierSiTextFieldEstVide(field)) {
            super.ajouterStyle(field, error)
            error.text = "Le champ ne peut pas être vide."
        }
        else if (!field.text.matches(regex)) {
            super.ajouterStyle(field, error)
            error.text = "Format Courriel : example@example.com"

        } else {
            super.enleverStyle(field, error)
        }
    }
}