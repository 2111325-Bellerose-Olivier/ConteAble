package com.conteabe.conteabe.utils

import javafx.beans.property.ReadOnlyStringWrapper
import javafx.beans.value.ObservableValue
import javafx.scene.control.TableColumn
import javafx.util.Callback
import kotlin.reflect.KProperty1

/**
 * Construit une observablevalue pour une table à partir d'un chemin de référence d'une propriété. Les différentes
 * propriétés sur le chemin sont séparé par des points.
 *
 * L'information est retournée comme une chaîne de caractères en lecture seule.
 *
 * @param cheminPropriete le chemin de la propriété à accéder à partir de l'objet affiché.
 */
class PropertyValueFactoryStringAgregeeLectureSeule<T>(cheminPropriete: String) :
    Callback<TableColumn.CellDataFeatures<T, String>, ObservableValue<String>> where T : Any {

    /**
     * Chemin d'accès de la propriété
     */
    private val cheminPropriete: List<String> = cheminPropriete.split(".")

    override fun call(param: TableColumn.CellDataFeatures<T, String>): ObservableValue<String> {
        var objet: Any = param.value
        for (propriete in cheminPropriete) {
            objet = (objet::class.members.first { it.name == propriete } as KProperty1<Any, *>).get(objet)!!
        }

        return ReadOnlyStringWrapper(objet.toString()).readOnlyProperty
    }
}