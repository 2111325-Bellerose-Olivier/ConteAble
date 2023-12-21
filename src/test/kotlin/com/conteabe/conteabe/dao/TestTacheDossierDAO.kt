package com.conteabe.conteabe.dao

import com.conteabe.conteabe.modele.*
import com.conteabe.conteabe.service.ConteneurService
import com.conteabe.conteabe.service.ServiceBD
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.sql.Time
import java.sql.Timestamp

class TestTacheDossierDAO {
    companion object {
        lateinit var tacheDossierDAO: TacheDossierDAO

        @BeforeAll
        @JvmStatic
        fun initialiser() {
            tacheDossierDAO = TacheDossierDAO(ConteneurService().getService<ServiceBD>() as ServiceBD)
        }
    }

    @Test
    fun testEnregistrer() {
        val role = Role(null, "testEmployeEnregistrer")
        val employe = Employe(null, "nom", "prenom", "mdp", role, "courriel")
        val client =
            Client(null, "adress", "codePostal", "ville", "province", "pays", "nom", "prenom", "courriel", "tel")
        val dossier = Dossier(null, client, "nomDossier")
        val tacheDossier = TacheDossier(null, dossier, employe, "nomTache", Timestamp(0), Time(2345), 0.0f)

        tacheDossierDAO.enregistrer(tacheDossier)

        // Cinquième test
        Assertions.assertNotNull(tacheDossier.id, "L'id devrait pas être null après l'insertion")

        var loadedTacheDossier = tacheDossierDAO.chargerParId(tacheDossier.id!!)

        // Sixième test
        Assertions.assertEquals(tacheDossier, loadedTacheDossier, "Les données entrées ne sont pas exact")

        tacheDossier.nom_tache = "nomTache34"
        tacheDossier.duree = Time(234235)
        tacheDossier.montant = 1.0f

        // Enregistrer la tâcheDossier mise à jour
        tacheDossierDAO.enregistrer(tacheDossier)

        loadedTacheDossier = tacheDossierDAO.chargerParId(tacheDossier.id!!)

        // Septième test
        Assertions.assertEquals(tacheDossier, loadedTacheDossier, "Les données entrées n'ont pas été modifiées")

        val taches = tacheDossierDAO.chargerTout()

        // Huitième test
        Assertions.assertNotNull(taches, "Des données devraient apparaître")
    }
}
