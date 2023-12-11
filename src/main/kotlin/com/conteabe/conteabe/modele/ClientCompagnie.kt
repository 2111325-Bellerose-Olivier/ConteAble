package com.conteabe.conteabe.modele
import com.conteabe.conteabe.dao.Entite

class ClientCompagnie (
    id: Int,
    var nom_compagnie: String,
    var numero_compagnie: Int,
    val personne_contact: PersonneContact,
    val client: Client,
) : Entite(id)

class PersonneContact (
    id: Int,
    var nom: String,
    var prenom: String,
    var courriel: String,
    var numero_telephone: String
) : Entite(id)

