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
class Role(
    id: Int?,
    var nom: String
) :
    Entite(id) {

    public fun estSuperviseur(): Boolean {
        return nom == "Superviseur"
    }

    override fun hashCode(): Int {
        return Objects.hash(id, nom)
    }

    override fun equals(other: Any?): Boolean {
        return other is Role && equals(other)
    }

    fun equals(other: Role): Boolean {
        return id == other.id && nom == other.nom
    }

    override fun toString(): String {
        return nom
    }
}
