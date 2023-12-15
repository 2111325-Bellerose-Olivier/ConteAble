package com.conteabe.conteabe.modele

class ClientIndividu (
    var idIndividu: Int?,
    adresse_civil: String,
    code_postal: String,
    ville: String,
    province: String,
    pays: String,
    var nom: String,
    var prenom: String,
    var courriel: String,
    var numero_telephone: String,
    idClient: Int?
) : Client(idClient, adresse_civil, code_postal, ville, province, pays)
