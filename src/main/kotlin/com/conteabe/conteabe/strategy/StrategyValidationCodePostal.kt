package com.conteabe.conteabe.StrategyPattern

import javafx.scene.control.Label
import javafx.scene.control.TextField

class StrategyValidationCodePostal : Strategy() {
    /*
    * Valide le code postal
    * */
    override fun Executer(field: TextField, error: Label) {
        if (super.verifierSiTextFieldEstVide(field)) {
            super.ajouterStyle(field, error)
            error.text = "Ce champ est obligatoire."
        } else if (super.verifierSiTextFieldPlusLimite(field, 7)) {
            super.ajouterStyle(field, error)
            error.text = "Le maximum de caractères est de 7."
        } else {
            super.enleverStyle(field, error)
        }
    }

}