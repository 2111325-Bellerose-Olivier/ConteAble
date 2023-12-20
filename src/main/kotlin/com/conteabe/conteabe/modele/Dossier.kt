package com.conteabe.conteabe.modele

import com.conteabe.conteabe.dao.Entite
import java.util.*

class Dossier(id: Int?, var client: Client, var nom: String) : Entite(id) {
    override fun hashCode(): Int {
        return Objects.hash(id, client, nom)
    }

    override fun equals(other: Any?): Boolean {
        return other is Dossier && equals(other)
    }

    fun equals(other: Dossier): Boolean {
        return id == other.id && client == other.client &&
                nom == other.nom
    }
}
