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
class Employe(id: Int, nom:String, prenom:String, mdp:String, id_role:Int, courriel:String) : Entite(id) {

    /**
     * Nom d'utilisateur dans le système.
     */
    var nom: String = nom
    var prenom: String = prenom
    var mdp: String = mdp
    var id_role: Int = id_role
    var courriel: String = courriel

}
