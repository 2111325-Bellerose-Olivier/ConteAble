package com.conteabe.conteabe.modele

import com.conteabe.conteabe.dao.Entite
class Dossier(id: Int?, id_client:Int, nom:String) : Entite(id){
    var idClient: Int = id_client
    var nom: String = nom
}
