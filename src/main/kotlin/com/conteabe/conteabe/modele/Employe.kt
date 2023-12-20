package com.conteabe.conteabe.modele;

import com.conteabe.conteabe.dao.Entite
import java.util.*

/**
 * Utilisateur du système.
 *
 * Classe placeholder avec uniquement un nom et un id d'utilisateur. TODO : bonifier la définition de l'utilisateur
 *
 * @author Alexandre
 * @since 06/05/2023
 */
class Employe(
    id: Int?,
    var nom: String,
    var prenom: String,
    var mdp: String,
    var role: Role,
    var courriel: String
) :
    Entite(id) {

    override fun hashCode(): Int {
        return Objects.hash(id, nom, prenom, mdp, role, courriel)
    }

    override fun equals(other: Any?): Boolean {
        return other is Employe && equals(other)
    }

    fun equals(other: Employe): Boolean {
        return id == other.id && nom == other.nom &&
                prenom == other.prenom && mdp == other.mdp &&
                role.equals(other.role) && courriel == other.courriel
    }
}
