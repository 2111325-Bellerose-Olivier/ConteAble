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
    fun testEnregistrer() {
        val role = Role(null, "testEmployeEnregistrer")
        val employe = Employe(null, "nom", "prenom", "mdp", role, "courriel")

        employeDAO.enregistrer(employe)

        if (employe.id == null) {
            Assertions.fail<TestEmployeDAO>()
        }

        val other = employeDAO.chargerParId(employe.id!!)

        assert(employe == other)
    }
}