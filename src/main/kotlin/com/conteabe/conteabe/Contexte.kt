package com.conteabe.conteabe

import com.conteabe.conteabe.modele.Employe
import com.conteabe.conteabe.service.ConteneurService
import javafx.stage.Stage


/**
 * Définit des données propre au contexte d'exécution du système
 *
 * @author Alexandre
 * @since 06/05/2023
 */
class Contexte(stage: Stage) {
    val services: ConteneurService = ConteneurService()
    var employeConnecte: Employe? = null
    var stage: Stage = stage
}