package com.conteabe.conteabe.StrategyPattern

import javafx.scene.control.Label
import javafx.scene.control.TextField

class StrategyValidationTexte : Strategy() {
    override fun Executer(field: TextField, error: Label) {
        if (super.verifierSiTextFieldEstVide(field)) {
            super.ajouterStyle(field, error)
            error.text = "Ce champ est obligatoire."
        } else if (super.verifierSiTextFieldEstPlusDe255Characteres(field)) {
            super.ajouterStyle(field, error)
            error.text = "Le maximum de caractères est de 255."
        } else {
            super.enleverStyle(field, error)
        }
    }

}