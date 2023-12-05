package com.conteabe.conteabe.modele;

import com.conteabe.conteabe.dao.Entite

/**
 * Utilisateur du système.
 *
 * Classe placeholder avec uniquement un nom et un id d'utilisateur. TODO : bonifier la définition de l'utilisateur
 *
 * @author Alexandre
 * @since 06/05/2023
 */
class Employe(id: Int?, var nom: String, var prenom: String, var mdp: String, var id_role: Int, var courriel: String) :
    Entite(id) {
}
