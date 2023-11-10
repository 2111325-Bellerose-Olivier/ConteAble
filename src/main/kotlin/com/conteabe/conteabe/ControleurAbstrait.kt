package com.conteabe.conteabe;

import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

/**
 * Controleur abstrait commun aux différents controleurs du système.
 *
 * @author Alexandre
 * @since 06/05/2023
 */
abstract class ControleurAbstrait(contexte: Contexte) {

    protected var contexte: Contexte = contexte

    /**
     * Charge une vue FXML et l'affiche dans la scène.
     *
     * @param cheminVue le chemin duquel charger la vue.
     * @param memeFenetre indique si l'affichage se fait dans la fenêtre actuelle.
     * @param nomFenetre le nom de la fenêtre après le rechargement. Si null, le nom n'est pas changé.
     * @param init fonction qui s'exécute sur le contrôleur avant l'affichage de la fenêtre.
     */
    protected fun <T : ControleurAbstrait> chargerVue(cheminVue: String, controleur: T, memeFenetre: Boolean = true, nomFenetre: String? = null) {
        val chargeur = FXMLLoader(ControleurAbstrait::class.java.getResource(cheminVue))
        chargeur.setController(controleur)

        val parent: Parent = chargeur.load()

        // TODO: gérer le paramètre memeFentre

        if (nomFenetre != null) {
            contexte.stage.title = nomFenetre
        }
        contexte.stage.scene = Scene(parent, 800.0, 800.0)
        contexte.stage.show()
    }

}
