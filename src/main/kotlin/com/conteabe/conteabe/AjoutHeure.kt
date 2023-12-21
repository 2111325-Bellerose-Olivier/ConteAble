import com.conteabe.conteabe.Contexte
import com.conteabe.conteabe.Page
import com.conteabe.conteabe.dao.DossierDAO
import com.conteabe.conteabe.dao.EmployeDAO
import com.conteabe.conteabe.dao.TacheDAO
import com.conteabe.conteabe.dao.TacheDossierDAO
import com.conteabe.conteabe.modele.Dossier
import com.conteabe.conteabe.modele.Employe
import com.conteabe.conteabe.modele.Tache
import com.conteabe.conteabe.modele.TacheDossier
import com.conteabe.conteabe.service.ServiceBD
import com.sun.javafx.scene.control.DoubleField
import com.sun.javafx.scene.control.IntegerField
import javafx.collections.FXCollections
import javafx.collections.transformation.FilteredList
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import java.sql.Time
import java.sql.Timestamp
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.util.Date
import java.util.function.Predicate

class AjoutHeure(private val contexte: Contexte) {
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

    private lateinit var dossiers: FilteredList<Triple<Int, String, Int>>

    @FXML
    private lateinit var dateDebut: DatePicker

    @FXML
    private lateinit var heureDebutHourSpinner: Spinner<Int>

    @FXML
    private lateinit var heureDebutMinuteSpinner: Spinner<Int>

    @FXML
    private lateinit var heureFinHourSpinner: Spinner<Int>

    @FXML
    private lateinit var heureFinMinuteSpinner: Spinner<Int>

    @FXML
    private lateinit var Ajouter: Button

    @FXML
    fun initialize() {
        dateDebut.value = LocalDate.now()

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

        listeDossier.selectionModel.selectedItemProperty().addListener { _, _, _ -> updateAjoutEnable() }
        listeTaches.selectionModel.selectedItemProperty().addListener { _, _, _ -> updateAjoutEnable() }

        updateAjoutEnable()
    }

    private fun updateAjoutEnable() {
        Ajouter.isDisable = listeDossier.selectionModel.selectedItems.isEmpty() ||
                listeTaches.selectionModel.selectedItems.isEmpty()
    }

    @FXML
    fun ajouter() {
        val heureDebutHour = heureDebutHourSpinner.value
        val heureDebutMinute = heureDebutMinuteSpinner.value
        val HeureFinHour = heureFinHourSpinner.value
        val HeureFinMinute = heureFinMinuteSpinner.value
        val idDossier = listeDossier.selectionModel.selectedItem.first
        val tache = listeTaches.selectionModel.selectedItem

        val heureDebut = LocalTime.of(heureDebutHour, heureDebutMinute)
        val heureFin = LocalTime.of(HeureFinHour, HeureFinMinute)

        val duree = Duration.between(heureDebut, heureFin)

        val tacheDossierDAO = TacheDossierDAO(contexte.services.getService<ServiceBD>() as ServiceBD)
        val dossierDAO = DossierDAO(contexte.services.getService<ServiceBD>() as ServiceBD)
        val employeDAO = EmployeDAO(contexte.services.getService<ServiceBD>() as ServiceBD)

        val dossier: Dossier = dossierDAO.chargerParId(idDossier)!!;
        val employeId = contexte.employeConnecte?.id

        if (employeId == null) {
            println("employeId is null.")
            return
        }

        val employe = employeDAO.chargerParId(employeId)

        if (employe == null) {
            println("Employe is null.")
            return
        }

        val tacheDossier = TacheDossier(
            null, dossier, employe, tache.nom, Timestamp.valueOf(
                heureDebut.atDate(
                    dateDebut.value
                )
            ), Time(duree.toMillis()), duree.seconds.toFloat() / 3600f * tache.taux
        )
        tacheDossierDAO.enregistrer(tacheDossier)
    }

    @FXML
    private fun retour() {
        contexte.SetPage(Page.Tache)
    }
}
