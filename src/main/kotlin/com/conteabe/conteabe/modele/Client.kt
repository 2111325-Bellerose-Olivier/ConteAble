package com.conteabe.conteabe.modele

import com.conteabe.conteabe.dao.Entite

class Client(
    id: Int,
    var adresse_civil: String,
    var code_postal: String,
    var pays: String,
    var province: String
) : Entite(id)

