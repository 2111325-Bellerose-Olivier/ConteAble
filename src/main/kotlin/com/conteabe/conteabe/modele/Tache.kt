package com.conteabe.conteabe.modele;

import com.conteabe.conteabe.dao.Entite
import java.util.*

class Tache(id: Int?, var nom: String, var taux: Float) : Entite(id) {
    override fun hashCode(): Int {
        return Objects.hash(id, nom, taux)
    }

    override fun equals(other: Any?): Boolean {
        return other is Tache && equals(other)
    }

    fun equals(other: Tache): Boolean {
        return id == other.id && nom == other.nom &&
                taux == other.taux
    }
}
