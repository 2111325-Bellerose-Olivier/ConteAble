package com.conteabe.conteabe.dao

import com.conteabe.conteabe.modele.Client
import com.conteabe.conteabe.service.ConteneurService
import com.conteabe.conteabe.service.ServiceBD
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class TestClientDAO {
    companion object {
        lateinit var clientDAO: ClientDAO

        @BeforeAll
        @JvmStatic
        fun initialiser() {
            clientDAO = ClientDAO(ConteneurService().getService<ServiceBD>() as ServiceBD)
        }
    }

    @Test
    fun testEnregistrer() {
        val client = Client(null, "145 rue lupien", "123", "123", "123", "123", "123", "123", "123", "123")

        clientDAO.enregistrer(client)

        val loadedClient = clientDAO.chargerParId(client.id!!)

        Assertions.assertEquals(client, loadedClient, "Les données entrées ne sont pas exact")
    }

    @Test
    fun TestEnregistrementId() {
        val client = Client(null, "145 rue lupien", "123", "123", "123", "123", "123", "123", "123", "123")

        clientDAO.enregistrer(client)

        Assertions.assertNotNull(client.id, "L'id devrait pas être null après l'insertion")
    }

    @Test
    fun TestUpdate() {
        val client: Client = Client(
            null, "adresse", "code_postal",
            "ville", "province", "pays", "nom", "prenom",
            "courriel", "numero_telephone"
        )

        clientDAO.enregistrer(client)

        client.adresse_civil = "145"
        client.code_postal = "145"
        client.ville = "145"
        client.province = "145"
        client.pays = "145"
        client.nom = "145"
        client.prenom = "145"
        client.courriel = "145"
        client.numero_telephone = "145"


        clientDAO.enregistrer(client)

        val loadedClient = clientDAO.chargerParId(client.id!!)


        Assertions.assertEquals(client, loadedClient, "Les données entrées n'ont pas été modifiées")
    }

    @Test
    fun testChargerTout() {
        val clients = clientDAO.chargerTout()

        Assertions.assertNotNull(clients, "Des données devraient apparaître")
    }

    @Test
    fun testSupprimer() {
        val client = Client(null, "145 rue lupien", "123", "123", "123", "123", "123", "123", "123", "123")

        clientDAO.enregistrer(client)

        clientDAO.supprimer(client.id!!)

        val loadedDossier = clientDAO.chargerParId(client.id!!)

        Assertions.assertEquals(null, loadedDossier, "Le dossier n'a pas été suprimer")
    }
}