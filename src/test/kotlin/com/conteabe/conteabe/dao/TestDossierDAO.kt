package com.conteabe.conteabe.dao

import com.conteabe.conteabe.modele.Client
import com.conteabe.conteabe.modele.Dossier
import com.conteabe.conteabe.service.ConteneurService
import com.conteabe.conteabe.service.ServiceBD
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class TestDossierDAO {
    companion object {
        lateinit var dossierDAO: DossierDAO

        @BeforeAll
        @JvmStatic
        fun initialiser() {
            dossierDAO = DossierDAO(ConteneurService().getService<ServiceBD>() as ServiceBD)
        }
    }

    @Test
    fun testEnregistrerId() {
        val client = Client(null, "145 rue lupien", "123", "123", "123", "123", "123", "123", "123", "123")
        val dossier = Dossier(id = null, nom = "Test", client = client)

        dossierDAO.enregistrer(dossier)

        Assertions.assertNotNull(client.id, "L'id devrait pas être null après l'insertion")
    }

    @Test
    fun TestEnregistrement() {
        val client = Client(null, "145 rue lupien", "123", "123", "123", "123", "123", "123", "123", "123")
        val dossier = Dossier(id = null, nom = "Test", client = client)

        dossierDAO.enregistrer(dossier)

        val loadedDossier = dossierDAO.chargerParId(client.id!!)

        Assertions.assertNotEquals(client, loadedDossier, "Les données entrées ne sont pas exact")
    }

    @Test
    fun TestUpdate() {
        val client = Client(null, "145 rue lupien", "123", "123", "123", "123", "123", "123", "123", "123")
        val dossier = Dossier(id = null, nom = "Test", client = client)

        dossierDAO.enregistrer(dossier)

        dossier.nom = "Test2"
        dossier.client.ville = "test"

        dossierDAO.enregistrer(dossier)

        val loadedDossier = dossierDAO.chargerParId(dossier.id!!)

        Assertions.assertEquals(dossier, loadedDossier, "Les données entrées n'ont pas été modifiées")
    }

    @Test
    fun testChargerTout() {
        val dossier = dossierDAO.chargerTout()

        Assertions.assertNotNull(dossier, "Des données devraient apparaître")
    }

    @Test
    fun testSupprimer() {
        val client = Client(null, "145 rue lupien", "123", "123", "123", "123", "123", "123", "123", "123")
        val dossier = Dossier(id = null, nom = "Test", client = client)

        dossierDAO.enregistrer(dossier)

        dossierDAO.supprimer(dossier.id!!)

        val loadedDossier = dossierDAO.chargerParId(dossier.id!!)

        Assertions.assertEquals(null, loadedDossier, "Le dossier n'a pas été suprimer")
    }
}