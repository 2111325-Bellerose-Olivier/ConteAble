package com.conteabe.conteabe.modele

import com.conteabe.conteabe.dao.Entite
import java.util.*

open class Client(
    id: Int?,
    var adresse_civil: String,
    var code_postal: String,
    var ville: String,
    var province: String,
    var pays: String,

    var nom: String,
    var prenom: String,
    var courriel: String,
    var numero_telephone: String
) : Entite(id) {
    override fun hashCode(): Int {
        return Objects.hash(
            id,
            adresse_civil,
            code_postal,
            ville,
            province,
            pays,
            nom,
            prenom,
            courriel,
            numero_telephone
        )
    }

    override fun equals(other: Any?): Boolean {
        return other is Client && equals(other)
    }

    fun equals(other: Client): Boolean {
        return id == other.id && adresse_civil == other.adresse_civil &&
                code_postal == other.code_postal && ville == other.ville &&
                province == other.province && pays == other.pays &&
                nom == other.nom && prenom == other.prenom &&
                courriel == other.courriel && numero_telephone == other.numero_telephone
    }
}

