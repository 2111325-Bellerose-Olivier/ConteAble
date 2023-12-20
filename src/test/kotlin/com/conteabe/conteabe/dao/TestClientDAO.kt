package com.conteabe.conteabe.dao

import com.conteabe.conteabe.modele.Client
import com.conteabe.conteabe.service.ConteneurService
import com.conteabe.conteabe.service.ServiceBD
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class TestClientDAO {
    companion object {
        lateinit var clientDao: ClientDAO

        @BeforeAll
        @JvmStatic
        fun initialiser() {
            clientDao = ClientDAO(ConteneurService().getService<ServiceBD>() as ServiceBD)
        }
    }

    @Test
    fun testEnregistrerClient() {
        val client: Client = Client(1, "adresse", "code_postal",
            "ville", "province", "pays", "nom", "prenom",
            "courriel", "numero_telephone")

        clientDao.enregistrer(client)

        if (client.id == null) {
            Assertions.fail<TestClientDAO>()
        }

        val other = clientDao.chargerParId(client.id!!)

        assert(client == other)
    }

    @Test
    fun testListerClient() {
        val nombreEntree: Int = clientDao.chargerTout().size

        val client: Client = Client(1, "adresse", "code_postal",
            "ville", "province", "pays", "nom", "prenom",
            "courriel", "numero_telephone")

        clientDao.enregistrer(client)

        val nouveauNombreEntree: Int = clientDao.chargerTout().size

        if (nombreEntree + 1 != nouveauNombreEntree) {
            Assertions.fail<TestClientDAO>()
        }
    }

    @Test
    fun testChargerClientParId() {
        val client: Client = Client(1, "adresse", "code_postal",
            "ville", "province", "pays", "nom", "prenom",
            "courriel", "numero_telephone")

        clientDao.enregistrer(client)

        val other = clientDao.chargerParId(client.id!!)

        if (client != other) {
            Assertions.fail<TestClientDAO>()
        }

    }

    @Test
    fun testSupprimerClient() {
        val client: Client = Client(1, "adresse", "code_postal",
            "ville", "province", "pays", "nom", "prenom",
            "courriel", "numero_telephone")

        clientDao.enregistrer(client)
        clientDao.supprimer(client.id!!)
        val other = clientDao.chargerParId(client.id!!)

        if (other != null) {
            Assertions.fail<TestClientDAO>()
        }
    }
}