package com.conteabe.conteabe.modele;

import com.conteabe.conteabe.dao.Entite
import java.sql.Time

class TacheDossier(
        id: Int?,
        val dossier: Dossier,
        val employe: Employe,
        var nom_tache: String,
        var duree: Time,
        var montant: Float
) :
        Entite(id)

class SqlTacheDossier(
        id: Int?,
        val dossier: Int,
        val employe: Int,
        var nom_tache: String,
        var duree: Time,
        var montant: Float
) :
        Entite(id)
