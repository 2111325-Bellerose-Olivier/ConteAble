package com.conteabe.conteabe

import com.conteabe.conteabe.dao.EmployeDAO
import com.conteabe.conteabe.modele.Employe
import com.conteabe.conteabe.service.ServiceBD
import javafx.application.Application
import javafx.beans.binding.Bindings
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.FXCollections
import javafx.collections.transformation.FilteredList
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.print.PrinterJob
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.control.cell.CheckBoxListCell
import javafx.stage.Stage
import java.time.LocalDate
import java.util.function.Predicate


class RapportHeuresController(private val contexte: Contexte) {
    @FXML
    private lateinit var beginDate: DatePicker

    @FXML
    private lateinit var beginValidation: Label

    @FXML
    private lateinit var endDate: DatePicker

    @FXML
    private lateinit var endValidation: Label

    @FXML
    private lateinit var genere: Button

    @FXML
    private lateinit var employeeFilter: TextField

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
        val today = LocalDate.now()
        var diffVendredi = (today.dayOfWeek.value.toLong() + 2) % 7

        if (diffVendredi == 0L) {//Si vendredi prendre le vendredi passé
            diffVendredi = 7
        }

        val endOfPreviousWeek = today.minusDays(diffVendredi)
        val startOfPreviousWeek = endOfPreviousWeek.minusDays(4)

        beginDate.value = startOfPreviousWeek
        endDate.value = endOfPreviousWeek


        beginDate.valueProperty().addListener { _, _, newValue ->
            if (newValue != null) {
                if (newValue.isBefore(endDate.value)) {
                    beginValidation.text = ""
                } else {
                    beginValidation.text = "La date de debut est après la date de fin."
                }
                updateGenerateEnabality()
            }
        }
        endDate.valueProperty().addListener { _, _, newValue ->
            if (newValue != null) {
                if (beginDate.value.isBefore(newValue)) {
                    beginValidation.text = ""
                } else {
                    beginValidation.text = "La date de debut est après la date de fin."
                }

                if (newValue.isBefore(LocalDate.now())) {
                    endValidation.text = ""
                } else {
                    endValidation.text = "La date de fin n'est pas encore passée."
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
                { createFilterPredicate(employeeFilter.text) },
                employeeFilter.textProperty()
            )
        )

        employees.cellFactory = CheckBoxListCell.forListView()
        { item -> item.selectedProperty() }
        employees.selectionModel.selectedItemProperty().addListener()
        { obemployee, _, _ ->
            val employee = obemployee.value
            employee ?: return@addListener
            val value = !employee.isSelected()
            employee.setSelected(value)
        }

        employees.items = filteredEmployee
    }

    private fun createFilterPredicate(filterText: String): Predicate<Employee> {
        return Predicate { item ->
            filterText.isEmpty() || item.toString().contains(filterText, ignoreCase = true)
        }
    }

    internal fun updateGenerateEnabality() {
        genere.isDisable = beginValidation.text != "" ||
                endValidation.text != "" ||
                getEmploye() == null
    }

    private fun getEmploye(): Employe? {
        val employe: Employee? = selectedEmploye
        employe ?: return null
        if (!employees.items.contains(employe))
            return null
        return employe.employe
    }

    @FXML
    private fun generate() {
        val employe: Employe = getEmploye() ?: return
        contexte.PrintScreen(Page.__PrintHeuresEmploye, employe)
    }
}


class Employee(private val controller: RapportHeuresController, val employe: Employe) {
    private val selected: BooleanProperty

    init {
        selected = SimpleBooleanProperty(false)
        selected.addListener { _, _, newValue ->
            if (newValue) {
                controller.selectedEmploye?.setSelected(false)
                controller.selectedEmploye = this
            } else {
                controller.selectedEmploye = null
            }

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