package com.conteabe.conteabe

import com.conteabe.conteabe.dao.DossierDAO
import com.conteabe.conteabe.dao.TacheDAO
import com.conteabe.conteabe.modele.Client
import com.conteabe.conteabe.modele.Dossier
import com.conteabe.conteabe.modele.Employe
import com.conteabe.conteabe.modele.Tache
import com.conteabe.conteabe.service.ServiceBD
import com.sun.javafx.scene.control.DoubleField
import com.sun.javafx.scene.control.IntegerField
import javafx.collections.FXCollections
import javafx.collections.transformation.FilteredList
import javafx.fxml.FXML
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.control.cell.PropertyValueFactory
import java.util.function.Predicate

class TacheController(private val contexte: Contexte) {

    @FXML
    private lateinit var listeTaches: TableView<Tache>

    @FXML
    private lateinit var idTache: TableColumn<Employe, Int>

    @FXML
    private lateinit var nomsTache: TableColumn<Employe, String>

    @FXML
    private lateinit var tauxHorraire: TableColumn<Employe, String>

    private lateinit var taches: FilteredList<Tache>

    @FXML
    private lateinit var listeDossier: TableView<Triple<Int, String, Int>>

    @FXML
    private lateinit var idDossier: TableColumn<Triple<Int, String, Int>, Int>

    @FXML
    private lateinit var idClient: TableColumn<Triple<Int, String, Int>, Int>

    @FXML
    private lateinit var nomDossier: TableColumn<Triple<Int, String, Int>, String>

    @FXML
    private lateinit var tauxHorraireAjout: DoubleField

    @FXML
    private lateinit var nomDossierAjout: TextField

    @FXML
    private lateinit var tauxHorraireModifier: DoubleField

    @FXML
    private lateinit var idTacheModifier: IntegerField

    @FXML
    private lateinit var nomTacheModifier: TextField

    private lateinit var dossiers: FilteredList<Triple<Int, String, Int>>

    private lateinit var tacheModifier: Tache

    fun initialize() {
        taches = FilteredList<Tache>(
            FXCollections.observableList(
                TacheDAO(
                    contexte.services.getService<ServiceBD>() as ServiceBD
                ).chargerTout()
            )
        )

        dossiers = FilteredList<Triple<Int, String, Int>>(
            FXCollections.observableList(
                DossierDAO(
                    contexte.services.getService<ServiceBD>() as ServiceBD
                ).chargerTout().map
                { dossier ->
                    Triple(dossier.id!!, dossier.nom, dossier.client.id!!)
                }
            )
        )

        // Définition des colonnes de la table
        idTache.cellValueFactory = PropertyValueFactory("id")
        nomsTache.cellValueFactory = PropertyValueFactory("nom")
        tauxHorraire.cellValueFactory = PropertyValueFactory("taux")

        idDossier.cellValueFactory = PropertyValueFactory("first")
        nomDossier.cellValueFactory = PropertyValueFactory("second")
        idClient.cellValueFactory = PropertyValueFactory("third")

        dossiers.predicate = Predicate { true }
        taches.predicate = Predicate { true }

        listeTaches.items = taches
        listeDossier.items = dossiers
    }

    @FXML
    private fun ajouter() {
        val taux = tauxHorraireAjout.value.toFloat()
        val nom = nomDossierAjout.text

        val tacheDAO = TacheDAO(contexte.services.getService<ServiceBD>() as ServiceBD)
        val tache = Tache(null, nom, taux)
        tacheDAO.enregistrer(tache)

        taches = FilteredList(FXCollections.observableList(tacheDAO.chargerTout()))
        listeTaches.items = taches
    }

    @FXML
    private fun modifier() {
        val taux = tauxHorraireModifier.value.toFloat()
        val tacheId = idTacheModifier.value
        val nom = nomTacheModifier.text

        val tacheDAO = TacheDAO(contexte.services.getService<ServiceBD>() as ServiceBD)
        tacheDAO.enregistrer(Tache(tacheId, nom, taux))

        taches = FilteredList(FXCollections.observableList(tacheDAO.chargerTout()))
        listeTaches.items = taches
    }

    @FXML
    private fun ajoutDossier(){
        contexte.SetPage(Page.AjouterDossier)
    }

    @FXML
    private fun ajoutTemps(){
        contexte.SetPage(Page.AjoutHeures)
    }

    @FXML
    private fun rapportTemps(){
        contexte.SetPage(Page.RapportHeures)
    }
}
