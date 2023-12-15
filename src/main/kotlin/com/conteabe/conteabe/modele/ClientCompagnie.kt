package com.conteabe.conteabe.modele

import com.conteabe.conteabe.dao.Entite

class ClientCompagnie (
        var idCompagnie: Int?,
        adresse_civil: String,
        code_postal: String,
        ville: String,
        province: String,
        pays: String,
        var nom_compagnie: String,
        var numero_compagnie: Int,
        val personne_contact: PersonneContact,
        idClient: Int?
) : Client(idClient, adresse_civil, code_postal, ville, province, pays) {
    val nom: String = personne_contact.nom
    val prenom: String = personne_contact.prenom
    val courrel: String = personne_contact.courriel
    val numero_telephone: String = personne_contact.numero_telephone
}

class PersonneContact (
        id: Int?,
        var nom: String,
        var prenom: String,
        var courriel: String,
        var numero_telephone: String
) : Entite(id)

