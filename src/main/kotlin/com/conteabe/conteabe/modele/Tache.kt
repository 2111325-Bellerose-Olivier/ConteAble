package com.conteabe.conteabe.modele;

import com.conteabe.conteabe.dao.Entite
class Tache(id: Int?, nom:String, taux:Float) : Entite(id) {

    var nom: String = nom
    var tauxHorraire: Float = taux

}
