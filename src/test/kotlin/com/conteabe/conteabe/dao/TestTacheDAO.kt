package com.conteabe.conteabe.dao

import com.conteabe.conteabe.modele.Tache
import com.conteabe.conteabe.service.ConteneurService
import com.conteabe.conteabe.service.ServiceBD
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
class TestTacheDAO {

    companion object {
        lateinit var tacheDAO: TacheDAO

        @BeforeAll
        @JvmStatic
        fun initialiser() {
            tacheDAO = TacheDAO(ConteneurService().getService<ServiceBD>() as ServiceBD)
        }
    }

    @Test
    fun testEnregistrer() {
        val tache = Tache(null, "nom_tache", 0.0f)

        tacheDAO.enregistrer(tache)

        //Premier test
        assertNotNull(tache.id, "L'id devrait pas être null après l'insertion")

        var other = tacheDAO.chargerParId(tache.id!!)

        //Deuxième test
        assertEquals(tache, other, "Les données entrées ne sont pas exact")

        tacheDAO.enregistrer(Tache(tache.id, "nom_tache_modifier", 1.0f))

        other = tacheDAO.chargerParId(tache.id!!)

        //Troisième test
        assertEquals(tache, other, "Les données entrées n'ont pas été modifié")

        var taches = tacheDAO.chargerTout()

        //Quatrième test
        assertNotNull(taches, "Des données devraient apparaitre")
    }
}
