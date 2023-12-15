package com.conteabe.conteabe.modele

import com.conteabe.conteabe.dao.Entite
class Dossier(
        id: Int?,
        var id_client: Int,
        var nom: String
) : Entite(id)

