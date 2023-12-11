package com.conteabe.conteabe.modele

import com.conteabe.conteabe.dao.Entite

class ClientIndividu(
    id: Int,
    var nom: String,
    var prenom: String,
    var courriel: String,
    var numero_telephone: String,
    var client: Client
) : Entite(id)
