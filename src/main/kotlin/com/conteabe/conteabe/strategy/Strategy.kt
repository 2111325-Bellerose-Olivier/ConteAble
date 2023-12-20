package com.conteabe.conteabe.StrategyPattern

import javafx.scene.control.Label
import javafx.scene.control.TextField

abstract class Strategy {
    abstract fun Executer(field: TextField, error: Label)

    /*
    * Vérifie si un text field est vide
    */
    protected fun verifierSiTextFieldEstVide(field: TextField): Boolean {
        return field.text.isEmpty()
    }

    /*
    * Vérifie si un text field est plus haut que la limite de caractères
    */
    protected fun verifierSiTextFieldPlusLimite(field: TextField, limite: Int): Boolean {
        return field.text.length > limite
    }

    /*
    * Ajoute le style erreur, lorsqu'il y a une erreur
    */
    protected fun ajouterStyle(textField: TextField, field: Label) {
        if (textField.styleClass.contains("error-text-field")) {
            return
        }

        field.isVisible = true
        field.isManaged = true

        textField.styleClass.add("error-text-field")
    }

    /*
    * Enlève le style erreur, lorsqu'il n'y a plus d'erreur
    */
    protected fun enleverStyle(textField: TextField, field: Label) {
        if (!textField.styleClass.contains("error-text-field")) {
            return
        }

        field.isVisible = false
        field.isManaged = false
        textField.styleClass.remove("error-text-field")
    }
}