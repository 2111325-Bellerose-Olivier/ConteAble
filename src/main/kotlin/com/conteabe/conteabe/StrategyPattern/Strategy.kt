package com.conteabe.conteabe.StrategyPattern

import javafx.scene.control.Label
import javafx.scene.control.TextField

abstract class Strategy {
    abstract fun Executer(field: TextField, error: Label)

    protected fun verifierSiTextFieldEstVide(field: TextField): Boolean {
        return field.text.isEmpty()
    }

    protected fun verifierSiTextFieldEstPlusDe255Characteres(field: TextField): Boolean {
        return field.text.length > 255
    }

    protected fun ajouterStyle(textField: TextField, field: Label) {
        if (textField.styleClass.contains("error-text-field")) {
            return
        }

        field.isVisible = true
        field.isManaged = true

        textField.styleClass.add("error-text-field")
    }

    protected fun enleverStyle(textField: TextField, field: Label) {
        if (!textField.styleClass.contains("error-text-field")) {
            return
        }

        field.isVisible = false
        field.isManaged = false
        textField.styleClass.remove("error-text-field")
    }
}