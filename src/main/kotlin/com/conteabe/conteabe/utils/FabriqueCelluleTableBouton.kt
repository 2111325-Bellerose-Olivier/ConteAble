package com.conteabe.conteabe.utils

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.util.Callback
import javax.swing.Action

/**
 *
 *
 * @author Alexandre
 * @since 07/05/2023
 */
class FabriqueCelluleTableBouton<T>(nomBouton: String, action: EventHandler<ActionEvent>) :
    Callback<TableColumn<T, Button>, TableCell<T, Button>> {

    val nomBouton: String = nomBouton
    val action: EventHandler<ActionEvent> = action

    override fun call(param: TableColumn<T, Button>?): TableCell<T, Button> {
        return BoutonCelluleTable(nomBouton, action)
    }

    inner class BoutonCelluleTable<S>(nomBouton: String, action: EventHandler<ActionEvent>) : TableCell<S, Button>() {

        val bouton: Button = Button(nomBouton)

        init {
            bouton.onAction = action

        }

        override fun updateItem(item: Button?, empty: Boolean) {
            super.updateItem(item, empty)

            if (empty) {
                graphic = null
                bouton.userData = -1
            } else {
                graphic = bouton
                bouton.userData = tableRow!!.index
            }
        }

    }
}

