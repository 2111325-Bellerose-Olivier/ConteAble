package com.conteabe.conteabe.modele;

import com.conteabe.conteabe.dao.Entite
import java.sql.Time
import java.util.Objects

class TacheDossier(
    id: Int?,
    val dossier: Dossier,
    val employe: Employe,
    var nom_tache: String,
    var duree: Time,
    var montant: Float
) :
    Entite(id) {
    override fun hashCode(): Int {
        return Objects.hash(id, dossier, employe, nom_tache, duree, montant)
    }

    override fun equals(other: Any?): Boolean {
        return other is TacheDossier && equals(other)
    }

    fun equals(other: TacheDossier): Boolean {
        return id == other.id && dossier == other.dossier &&
                employe == other.employe && nom_tache == other.nom_tache &&
                duree == other.duree && montant == other.montant
    }
}
