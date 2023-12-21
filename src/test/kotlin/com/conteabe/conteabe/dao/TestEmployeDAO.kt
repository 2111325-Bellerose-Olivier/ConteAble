package com.conteabe.conteabe.dao

import com.conteabe.conteabe.modele.Employe
import com.conteabe.conteabe.modele.Role
import com.conteabe.conteabe.service.ConteneurService
import com.conteabe.conteabe.service.ServiceBD
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class TestEmployeDAO {
    companion object {
        lateinit var employeDAO: EmployeDAO

        @BeforeAll
        @JvmStatic
        fun initialiser() {
            employeDAO = EmployeDAO(ConteneurService().getService<ServiceBD>() as ServiceBD)
        }
    }

    @Test
    fun testEnregistrerEmploye() {
        val role = Role(null, "testEmployeEnregistrer")
        val employe = Employe(null, "nom", "prenom", "mdp", role, "courriel")

        employeDAO.enregistrer(employe)

        if (employe.id == null) {
            Assertions.fail<TestEmployeDAO>()
        }

        val other = employeDAO.chargerParId(employe.id!!)

        assert(employe == other)
    }

    @Test
    fun testListerEmploye() {
        val nombreEntree: Int = employeDAO.chargerTout().size

        val role = Role(null, "testEmployeEnregistrer")
        val employe = Employe(null, "nom", "prenom", "mdp", role, "courriel")

        employeDAO.enregistrer(employe)

        val nouveauNombreEntree: Int = employeDAO.chargerTout().size

        if (nombreEntree + 1 != nouveauNombreEntree) {
            Assertions.fail<TestEmployeDAO>()
        }
    }

    @Test
    fun testChargerEmployeParId() {
        val role = Role(null, "testEmployeEnregistrer")
        val employe = Employe(null, "nom", "prenom", "mdp", role, "courriel")

        employeDAO.enregistrer(employe)

        if (employe.id == null) {
            Assertions.fail<TestEmployeDAO>()
        }

        val other = employeDAO.chargerParId(employe.id!!)

        if (employe != other) {
            Assertions.fail<TestEmployeDAO>()
        }
    }

    @Test
    fun testSupprimerClient() {
        val role = Role(null, "testEmployeEnregistrer")
        val employe = Employe(null, "nom", "prenom", "mdp", role, "courriel")

        employeDAO.enregistrer(employe)

        if (employe.id == null) {
            Assertions.fail<TestEmployeDAO>()
        }

        employeDAO.supprimer(employe.id!!)
        val other = employeDAO.chargerParId(employe.id!!)

        if (other != null) {
            Assertions.fail<TestEmployeDAO>()
        }
    }
}