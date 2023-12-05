package com.conteabe.conteabe.GererEmploye

import javafx.scene.control.TableCell
import javafx.scene.control.Button
import javafx.scene.control.TableColumn
import javafx.util.Callback


class BoutonSupprimer<S> : TableCell<S, String>(), Callback<TableColumn<S, String>, TableCell<S, String>> {
    private val bouton = Button("Supprimer")
    init {
        bouton.setOnAction{
            val index = index
            val personne = tableView.items[index]
        }
    }

    override fun call(param: TableColumn<S, String>): TableCell<S, String> {
        return this
    }
}