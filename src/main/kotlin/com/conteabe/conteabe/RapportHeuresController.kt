package com.conteabe.conteabe

import com.conteabe.conteabe.dao.DossierDAO
import com.conteabe.conteabe.dao.EmployeDAO
import com.conteabe.conteabe.dao.TacheDossierDAO
import com.conteabe.conteabe.modele.Employe
import com.conteabe.conteabe.modele.TacheDossier
import com.conteabe.conteabe.service.ServiceBD
import javafx.beans.binding.Bindings
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.FXCollections
import javafx.collections.transformation.FilteredList
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.control.cell.CheckBoxListCell
import java.sql.Time
import java.time.LocalDate
import java.util.function.Predicate


class RapportHeuresController(private val contexte: Contexte) {
    @FXML
    private lateinit var dateDebut: DatePicker

    @FXML
    private lateinit var validationDebut: Label

    @FXML
    private lateinit var dateFin: DatePicker

    @FXML
    private lateinit var validationFin: Label

    @FXML
    private lateinit var genere: Button

    @FXML
    private lateinit var filtreIdEmployee: TextField

    @FXML
    private lateinit var employees: ListView<Employee>

    private lateinit var employeesList: FilteredList<Employee>

    internal var selectedEmploye: Employee? = null

    fun initialize() {
        initializeDates()
        initializeEmployees()
        updateGenerateEnabality()
    }

    private fun initializeDates() {
        val aujourdhui = LocalDate.now()

        var jourApresVendredi = (aujourdhui.dayOfWeek.value + 2) % 7
        if (jourApresVendredi == 0) { // Si vendredi, prendre le vendredi passé
            jourApresVendredi = 7
        }

        val endOfPreviousWeek = aujourdhui.minusDays(jourApresVendredi.toLong())
        val startOfPreviousWeek = endOfPreviousWeek.minusDays(4)

        dateDebut.value = startOfPreviousWeek
        dateFin.value = endOfPreviousWeek


        dateDebut.valueProperty().addListener { _, _, newValue ->
            if (newValue != null) {
                if (newValue.isBefore(dateFin.value)) {
                    validationDebut.text = ""
                } else {
                    validationDebut.text = "La date de debut est après la date de fin."
                }
                updateGenerateEnabality()
            }
        }
        dateFin.valueProperty().addListener { _, _, newValue ->
            if (newValue != null) {
                if (dateDebut.value.isBefore(newValue)) {
                    validationDebut.text = ""
                } else {
                    validationDebut.text = "La date de debut est après la date de fin."
                }

                if (newValue.isBefore(LocalDate.now())) {
                    validationFin.text = ""
                } else {
                    validationFin.text = "La date de fin n'est pas encore passée."
                }
                updateGenerateEnabality()
            }
        }
    }

    private fun initializeEmployees() {
        employeesList = FilteredList<Employee>(
                FXCollections.observableList(
                        EmployeDAO(
                                contexte.services.getService<ServiceBD>() as ServiceBD
                        ).chargerTout().map { employe -> Employee(this, employe) }
                )
        )

        val filteredEmployee = FilteredList(employeesList)

        filteredEmployee.predicateProperty().bind(
                Bindings.createObjectBinding(
                        { predicatFiltreEmploye(filtreIdEmployee.text) },
                        filtreIdEmployee.textProperty()
                )
        )

        employees.cellFactory = CheckBoxListCell.forListView()
        { item -> item.selectedProperty() }
        employees.selectionModel.selectedItemProperty().addListener()
        { observedEmlpoye, _, _ ->
            val employee = observedEmlpoye.value
            employee ?: return@addListener
            employee.setSelected(!employee.isSelected())
        }

        employees.items = filteredEmployee
    }

    private fun predicatFiltreEmploye(textFiltre: String): Predicate<Employee> {
        return Predicate { item ->
            textFiltre.isEmpty() || item.toString().contains(textFiltre, ignoreCase = true)
        }
    }

    internal fun updateGenerateEnabality() {
        genere.isDisable = validationDebut.text != "" ||
                validationFin.text != "" ||
                employeSelectionne() == null
    }

    private fun employeSelectionne(): Employe? {
        val employe: Employee? = selectedEmploye
        employe ?: return null
        if (!employees.items.contains(employe))
            return null
        return employe.employe
    }

    @FXML
    private fun generate() {
        val employe: Employe = employeSelectionne() ?: return
        contexte.PrintScreen(Page.__PrintHeuresEmploye, employe)
    }
}


class Employee(private val controller: RapportHeuresController, val employe: Employe) {
    private val selected: BooleanProperty

    init {
        selected = SimpleBooleanProperty(false)
        selected.addListener { _, _, newValue ->
            controller.selectedEmploye?.setSelected(false)
            controller.selectedEmploye = if (newValue) this else null
            controller.updateGenerateEnabality()
        }
    }

    fun selectedProperty(): BooleanProperty {
        return selected
    }

    fun isSelected(): Boolean {
        return selected.get()
    }

    fun setSelected(selected: Boolean) {
        this.selected.set(selected)
    }

    override fun toString(): String {
        return employe.nom + " " + employe.prenom
    }
}