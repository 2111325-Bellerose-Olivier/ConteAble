package com.conteabe.conteabe.modele

import com.conteabe.conteabe.dao.Entite

open class Client(
    idClient: Int?,
    var adresse_civil: String,
    var code_postal: String,
    var ville: String,
    var province: String,
    var pays: String,

    var nom: String,
    var prenom: String,
    var courriel: String,
    var numero_telephone: String
) : Entite(idClient) 

